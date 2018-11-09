package com.ahajri.heaven.calendar.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.ahajri.heaven.calendar.batch.BCountryItemProcessor;
import com.ahajri.heaven.calendar.batch.BCountryJsonLineMapper;
import com.ahajri.heaven.calendar.model.BCountry;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	@Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
	
	@Bean
	FlatFileItemReader<BCountry> flatFileScandinavianCountriesItemReader() {
	    FlatFileItemReader<BCountry> reader = new FlatFileItemReader<>();
	    reader.setResource(new FileSystemResource("scandinavian-countries.json"));
	    BCountryJsonLineMapper lineMapper = new BCountryJsonLineMapper();
	    reader.setLineMapper(lineMapper);
	    return reader;
	}
	
	@Bean
    public BCountryItemProcessor processor() {
        return new BCountryItemProcessor();
    }

}
