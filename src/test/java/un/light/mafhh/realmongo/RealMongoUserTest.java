package un.light.mafhh.realmongo;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import un.light.mafhh.collection.User;
import un.light.mafhh.mongo.config.SystemProfileValueSource2;
import un.light.mafhh.security.exception.RestException;

@ProfileValueSourceConfiguration(value = SystemProfileValueSource2.class)
//@IfProfileValue(name = ACTIVE_PROFILES_PROPERTY_NAME, value = "it")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RealMongoUserTest {

	@Autowired
	private TestRestTemplate restTemplate;

	private static User userRecordReturned;

	@Test
	public void test1PostUser() {
		User userRecord = new User("ahajri3", "RealTestAhajri3Pwd");
		ResponseEntity<User> responseEntity = restTemplate.postForEntity("/users/post", userRecord, User.class);
		userRecordReturned = responseEntity.getBody();
		assertNotNull("Should have an PK", userRecordReturned.getId());
	}

	@Test
	public void test2FinUserByUsername() {
		String url = String.format("/users/byUsername/%s", userRecordReturned.getUsername());
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");

		HttpEntity entity = new HttpEntity(headers);

		HttpEntity<User> response = restTemplate.exchange(url, HttpMethod.GET, entity, User.class);
		System.out.println("User found -> " + response.getBody());

		//
		assertNotNull(response.getBody().getId());
	}

	@Test(expected = AssertionError.class)
	public void test3DeleteUser() {
		String url = String.format("/users/delete/%s", userRecordReturned.getUsername());
		restTemplate.delete(url);
		assertNull(restTemplate.getForEntity("/users/byUsername/" + userRecordReturned.getUsername(), User.class)
				.getBody());
	}

}
