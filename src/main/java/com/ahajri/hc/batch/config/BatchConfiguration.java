package com.ahajri.hc.batch.config;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;

import com.ahajri.hc.batch.beans.BCountry;
import com.ahajri.hc.exception.BusinessException;
import com.ahajri.hc.mongo.cloud.CloudMongoService;
import com.mongodb.MongoOptions;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfiguration {

	private static final String SCANDINAVIAN_COUNTRIES_JSON_FILE = "scandinavian-countries.json";
	private static final String EVENT_COLLECTION_NAME = "event";

	private static final Logger LOG = LoggerFactory.getLogger(BatchConfiguration.class);

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private CloudMongoService cloudMongoService;

	@Bean
	public ResourcelessTransactionManager transactionManager() {
		return new ResourcelessTransactionManager();
	}

	@Bean
	public MapJobRepositoryFactoryBean mapJobRepositoryFactory(ResourcelessTransactionManager txManager)
			throws Exception {
		MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean(txManager);
		factory.afterPropertiesSet();
		return factory;
	}

	@Bean
	public JobRepository jobRepository(MapJobRepositoryFactoryBean factory) throws Exception {
		return (JobRepository) factory.getObject();
	}

	private SimpleJobLauncher jobLauncher;

	@Bean
	public SimpleJobLauncher jobLauncher(JobRepository jobRepository) {
		jobLauncher.setJobRepository(jobRepository);
		return jobLauncher;
	}

	@PostConstruct
	private void initJobLauncher() {
		jobLauncher = new SimpleJobLauncher();
	}

	@Bean
	FlatFileItemReader<List<BCountry>> reader() {
		FlatFileItemReader<List<BCountry>> reader = new FlatFileItemReader<>();
		reader.setName("scandinaviandCountriesReader");
		reader.setResource(new ClassPathResource(SCANDINAVIAN_COUNTRIES_JSON_FILE));
		reader.setLineMapper(new BCountryJsonLineMapper());
		return reader;
	}

	@Bean
	public ItemWriter<List<Document>> writer() {
		return new ItemWriter<List<Document>>() {
			@Override
			public void write(List<? extends List<Document>> items) throws Exception {
				try {
					if (!CollectionUtils.isEmpty(items) && items.size() > 0) {
						List<Document> flatDocs = items.stream().flatMap(List::stream).collect(Collectors.toList());
						cloudMongoService.insertMany(EVENT_COLLECTION_NAME, flatDocs);
					} else {
						LOG.warn("No document to save ....");
					}
				} catch (BusinessException e) {
					throw new RuntimeException(e);
				}
			}
		};
	}

	@Bean
	public BCountryPrayTimeEventItemProcessor processor() {
		return new BCountryPrayTimeEventItemProcessor();
	}

	@Bean
	public Job scandvPrayTimeJob() {
		
		return jobBuilderFactory.get("scandvPrayTimeJob").incrementer(new RunIdIncrementer()).flow(step1()).end()
				.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<List<BCountry>, List<Document>>chunk(10).reader(reader())
				.processor(processor()).writer(writer()).build();
	}

	// end::jobstep[]
	@Scheduled(cron = "30 56 14 * * *")
	public void startScandvPrayTimeJob() throws Exception {
		LOG.info(" ====> Job Started at :" + new Date());
		JobParameters param = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		JobExecution execution = jobLauncher.run(scandvPrayTimeJob(), param);
		LOG.info(" ====> Job finished with status : " + execution.getStatus());
	}
}
