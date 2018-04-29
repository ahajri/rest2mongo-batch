package un.light.mafhh.embeddedmongo;

import static org.junit.Assert.*;
import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;

import com.mongodb.DBCollection;

import un.light.mafhh.collection.User;
import un.light.mafhh.mongo.config.SystemProfileValueSource2;
import un.light.mafhh.mongo.repository.UserRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

//@ProfileValueSourceConfiguration(value = SystemProfileValueSource2.class)
//@IfProfileValue(name = ACTIVE_PROFILES_PROPERTY_NAME, value = "it-embedded") // "spring.profiles.active"
//@RunWith(SpringRunner.class)
//@DataMongoTest
public class MongoSliceUserIT {
	String collectionName;
	User userInsert;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private UserRepository userRepository;

	@Before
	public void before() {
		collectionName = "TestUserCollection";
		userInsert = new User("TestUser", "TestPwd");
	}

	@After
	public void after() {
		mongoTemplate.dropCollection(collectionName);
	}

	@Test
	public void checkMongoTemplate() {
		assertNotNull(mongoTemplate);
		DBCollection createdCollection = mongoTemplate.createCollection(collectionName);
		assertTrue(mongoTemplate.collectionExists(collectionName));
	}

	@Test
	public void checkDocumentAndQuery() {
		mongoTemplate.save(userInsert, collectionName);
		Query query = new Query(new Criteria().andOperator(Criteria.where("username").regex(userInsert.getUsername()),
				Criteria.where("password").regex(userInsert.getPassword())));

		User retrievedUser = mongoTemplate.findOne(query, User.class, collectionName);
		assertNotNull(retrievedUser);
	}

	@Test
	public void checkLogRepository() {
		assertNotNull(userRepository);
		User savedUser = userRepository.save(userInsert);
		assertNotNull(userRepository.findOne(savedUser.getId()));
	}
}
