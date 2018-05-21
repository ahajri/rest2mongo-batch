package com.ahajri.heaven.calendar.embeddedmongo;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.ahajri.heaven.calendar.collection.EventCollection;
import com.ahajri.heaven.calendar.constants.enums.RecurringEnum;
import com.ahajri.heaven.calendar.security.exception.TechnicalException;
import com.ahajri.heaven.calendar.service.impl.EventServiceImpl;
import com.mongodb.Mongo;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.RuntimeConfig;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.extract.UserTempNaming;

public class EventEmbeddedIntegrationTest {

	private static final String LOCALHOST = "127.0.0.1";
	private static final String DB_NAME = "HeavenCaledarDB";
	private static final int MONGO_TEST_PORT = 27028;

	private EventServiceImpl eventService;

	private static MongodProcess mongoProcess;
	private static Mongo mongo;

	private MongoTemplate template;

	@BeforeClass
	public static void initializeDB() throws IOException {

		RuntimeConfig config = new RuntimeConfig();
		config.setExecutableNaming(new UserTempNaming());

		MongodStarter starter = MongodStarter.getInstance(config);

		MongodExecutable mongoExecutable = starter.prepare(new MongodConfig(Version.V2_2_0, MONGO_TEST_PORT, false));
		mongoProcess = mongoExecutable.start();

		mongo = new Mongo(LOCALHOST, MONGO_TEST_PORT);
		mongo.getDB(DB_NAME);
	}

	@AfterClass
	public static void shutdownDB() throws InterruptedException {
		mongo.close();
		mongoProcess.stop();
	}

	@Before
	public void setUp() throws Exception {
		eventService = new EventServiceImpl();
		template = new MongoTemplate(mongo, DB_NAME);
		//repoImpl.setMongoOps(template);
	}

	@After
	public void tearDown() throws Exception {
		template.dropCollection(EventCollection.class);
	}
	
	
	@Test
	public void test1CreateEvent(){
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
		List<EventCollection> persisted = null;
		try {
			persisted = eventService.save(event);
		} catch (TechnicalException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertNotNull(persisted);
		Assert.assertNotEquals(0, persisted.size());
	}
}
