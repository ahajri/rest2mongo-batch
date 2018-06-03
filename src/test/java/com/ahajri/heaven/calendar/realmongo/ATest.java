package com.ahajri.heaven.calendar.realmongo;

import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.ahajri.heaven.calendar.mongo.config.SystemProfileValueSource2;

@ProfileValueSourceConfiguration(value = SystemProfileValueSource2.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class ATest {
	
	@Autowired
	protected TestRestTemplate restTemplate;

	public abstract void testACreateTest();

	public abstract void testBUpdateTest();
	
	public abstract void testCFindAllTest();
	
	public abstract void testDFindByCriteriaTest();

	public abstract void testEDeleteByCriteriaTest();


	

}
