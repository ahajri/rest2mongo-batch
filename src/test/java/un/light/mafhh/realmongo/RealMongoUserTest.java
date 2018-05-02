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

import com.ahajri.heaven.calendar.collection.UserAuth;
import com.ahajri.heaven.calendar.mongo.config.SystemProfileValueSource2;
import com.ahajri.heaven.calendar.security.exception.RestException;

@ProfileValueSourceConfiguration(value = SystemProfileValueSource2.class)
//@IfProfileValue(name = ACTIVE_PROFILES_PROPERTY_NAME, value = "it")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RealMongoUserTest {

	@Autowired
	private TestRestTemplate restTemplate;

	private static UserAuth userRecordReturned;

	@Test
	public void test1PostUser() {
		UserAuth userRecord = new UserAuth("ahajri3", "RealTestAhajri3Pwd");
		ResponseEntity<UserAuth> responseEntity = restTemplate.postForEntity("/users/post", userRecord, UserAuth.class);
		userRecordReturned = responseEntity.getBody();
		assertNotNull("Should have an PK", userRecordReturned.getId());
	}

	@Test
	public void test2FinUserByUsername() {
		String url = String.format("/users/byUsername/%s", userRecordReturned.getUsername());
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");

		HttpEntity entity = new HttpEntity(headers);

		HttpEntity<UserAuth> response = restTemplate.exchange(url, HttpMethod.GET, entity, UserAuth.class);
		System.out.println("User found -> " + response.getBody());

		//
		assertNotNull(response.getBody().getId());
	}

	@Test(expected = AssertionError.class)
	public void test3DeleteUser() {
		String url = String.format("/users/delete/%s", userRecordReturned.getUsername());
		restTemplate.delete(url);
		assertNull(restTemplate.getForEntity("/users/byUsername/" + userRecordReturned.getUsername(), UserAuth.class)
				.getBody());
	}

}
