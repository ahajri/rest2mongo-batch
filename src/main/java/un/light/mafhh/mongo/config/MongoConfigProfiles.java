package un.light.mafhh.mongo.config;

import com.mongodb.MongoClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

@Configuration
//@Profile("it") // or else problems when using @EnableAutoConfiguration
							// in Integration tests
@EnableAutoConfiguration
public class MongoConfigProfiles {
	
	private final MongoProperties mongoProperties;

	public MongoConfigProfiles(MongoProperties mongoProperties) {
		this.mongoProperties = mongoProperties;
	}

	@Bean
	public MongoDbFactory mongoDbFactory() throws Exception {
		MongoClient mongoClient = new MongoClient(mongoProperties.getHost(), mongoProperties.getPort());
		return new SimpleMongoDbFactory(mongoClient, mongoProperties.getDatabase());
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
		return mongoTemplate;
	}
	
	
}
