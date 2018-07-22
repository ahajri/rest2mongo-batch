package com.ahajri.heaven.calendar.realmongo;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.bson.Document;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.ahajri.heaven.calendar.exception.BusinessException;
import com.ahajri.heaven.calendar.mongo.cloud.CloudApiMongoService;

@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CloudMongoTest {

	@Autowired
	private CloudApiMongoService cloudApiMongoService;

	@Test
	@Ignore
	public void testACreate() {
		Document event = new Document();
		event.append("name", "Test Cloud Create Event");
		event.append("creation_date", new Date());
		try {
			cloudApiMongoService.insertOne("event", event);
		} catch (BusinessException e) {
			fail(e.getMessage());
		}
		assertTrue(true);
	}

	public void testBUpdate() {
		// TODO Auto-generated method stub

	}

	public void testCFindAll() {
		// TODO Auto-generated method stub

	}

	public void testDFindByCriteria() {
		// TODO Auto-generated method stub

	}

	public void testEDeleteAllByCriteria() {
		// TODO Auto-generated method stub

	}

}
