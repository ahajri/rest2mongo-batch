package com.ahajri.heaven.calendar.realmongo;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonFactoryBean;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder.ClientBuilder;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.ahajri.heaven.calendar.collection.EventCollection;
import com.ahajri.heaven.calendar.constants.enums.RecurringEnum;
import com.ahajri.heaven.calendar.mongo.config.SystemProfileValueSource2;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.util.JSON;

@ProfileValueSourceConfiguration(value = SystemProfileValueSource2.class)
// @IfProfileValue(name = ACTIVE_PROFILES_PROPERTY_NAME, value = "it")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RealMongoEventTest {

	@Autowired
	private RestTemplate restTemplate;

	private static EventCollection persisted;

	private static List<EventCollection> persisteds;

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Test
	public void test1CreateEvent() {
		EventCollection event = new EventCollection();
		event.setTitle("Test Event Monthly Recurring");
		event.setAllDay(false);
		event.setRecurring(RecurringEnum.MONTHLY.toString());
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		LocalDateTime startDateTime = LocalDateTime
				.of(LocalDate.now(),
						LocalTime.of(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND)))
				.plusMinutes(30);
		event.setStartDateTime(Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant()));
		c.clear();
		c.set(Calendar.YEAR, 2018);
		c.set(Calendar.MONTH, 11);
		c.set(Calendar.DAY_OF_MONTH, 31);
		event.setEndDateTime(c.getTime());
		
		
		
		persisteds = Arrays.asList(restTemplate.postForObject("http://localhost:5050/events/create", event, EventCollection[].class));
		assertNotEquals(0, persisteds.size());
	}

}
