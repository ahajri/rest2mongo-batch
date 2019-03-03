package com.knoor.soft.batch.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import com.google.gson.Gson;
import com.knoor.soft.batch.beans.BCountry;
import com.knoor.soft.constants.enums.EventType;
import com.knoor.soft.constants.enums.OperatorEnum;
import com.knoor.soft.constants.enums.RecurringEnum;
import com.knoor.soft.exception.BusinessException;
import com.knoor.soft.mongo.cloud.CloudMongoService;
import com.knoor.soft.queries.QueryParam;
import com.knoor.soft.service.PrayTimeService;
import com.knoor.soft.utils.HCDateUtils;

public class BCountryPrayTimeEventItemProcessor implements ItemProcessor<List<BCountry>, List<Document>> {

	private static final String EVENTS_COLLECTION_NAME = "event";

	private static final Logger LOG = LoggerFactory.getLogger(BCountryPrayTimeEventItemProcessor.class);

	@Autowired
	private PrayTimeService prayTimeService;

	@Autowired
	private CloudMongoService cloudMongoService;
	
	
	protected static String emailId;
	protected static String emailPassword;
	
	@Value("${email.id}")
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	@Value("${email.password}")
	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}
	
	
	private static void readInbox() throws MessagingException, IOException {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", "imap.mail.yahoo.com");
        properties.put("mail.imaps.port", "993");
        Session session = Session.getDefaultInstance(properties, null);
        Store store = session.getStore("imaps");
        System.out.println("####Connection initiated......"+emailId);
        store.connect(emailId, emailPassword);
        System.out.println("###Connection is ready :)");
        Folder inbox = store.getFolder("inbox");
        inbox.open(Folder.READ_ONLY);

        int messageCount = inbox.getMessageCount();
        System.out.println("###Total Messages in INBOX:- " + messageCount);

        for (int i = 10; i > 0; i--) {
            System.out.println("Mail Subject:- " + inbox.getMessage(messageCount - i).getSubject());
            System.out.println("Mail From:- " + inbox.getMessage(messageCount - i).getFrom()[0]);
            System.out.println("Mail Content:- " + inbox.getMessage(messageCount - i).getContent().toString());
            System.out.println("------------------------------------------------------------");
        }
        store.close();
        
    }

	@Override
	public List<Document> process(List<BCountry> items) throws Exception {
		//readInbox();
		
		final List<Document> docs = new ArrayList<>();
		
	
		items.stream().forEach(item -> {

			final String countryName = item.getName();

			item.getCities().stream().forEach(c -> {
				final Document prayTimeCityEventDoc = new Document();
				// loop cities and extract pray time for today
				final String cityName = c.getName();
				final String cityTimeZone = c.getTimeZone();
				final double lat = c.getLat();
				final double lng = c.getLng();
				
				final LocalDateTime nowOfCity = LocalDateTime.now(ZoneId.of(cityTimeZone));

				
				final QueryParam[] queryParams = new QueryParam[5];

				queryParams[0] = new QueryParam("city_name", OperatorEnum.EQ.name(), cityName);
				queryParams[1] = new QueryParam("event_type", OperatorEnum.EQ.name(), EventType.PRAY_TIME.name());
				queryParams[2] = new QueryParam("month", OperatorEnum.EQ.name(), nowOfCity.getMonthValue());
				queryParams[3] = new QueryParam("day_of_month", OperatorEnum.EQ.name(), nowOfCity.getDayOfMonth());
				queryParams[4] = new QueryParam("country_name", OperatorEnum.EQ.name(), countryName);
				List<Document> foundEvents = null;
				try {
					foundEvents = cloudMongoService.search(EVENTS_COLLECTION_NAME, queryParams);
				} catch (BusinessException e1) {
					LOG.error("====>No pray time found for city " + cityName + " on " + nowOfCity.getDayOfMonth() + "/"
							+ nowOfCity.getMonthValue());
				}

				try {
					if (CollectionUtils.isEmpty(foundEvents)) {
						// Pray time not created yet
						prayTimeCityEventDoc.put("country_name", countryName);
						prayTimeCityEventDoc.put("city_name", cityName);
						prayTimeCityEventDoc.put("event_type", EventType.PRAY_TIME.name());
						prayTimeCityEventDoc.put("recurring", RecurringEnum.YEARLY.name());
						prayTimeCityEventDoc.put("month", nowOfCity.getMonthValue());
						prayTimeCityEventDoc.put("day_of_month", nowOfCity.getDayOfMonth());
						prayTimeCityEventDoc.put("lat", lat);
						prayTimeCityEventDoc.put("lng", lng);
						prayTimeCityEventDoc.put("creation_date", HCDateUtils.convertToDateViaSqlTimestamp(nowOfCity));

						final Map<String, Object> prayInfos = prayTimeService.getPrayTimeByLatLngDate(lat, lng,
								Date.from(nowOfCity.atZone(ZoneId.of(cityTimeZone)).toInstant()), cityTimeZone);

						prayTimeCityEventDoc.put("pray_infos", Document.parse(new Gson().toJson(prayInfos)));

						docs.add(prayTimeCityEventDoc);

					} else {
						LOG.info(String.format("====>Pray time already exists for city: %s, month: %d, day:  %d",
								cityName, nowOfCity.getMonthValue(), nowOfCity.getDayOfMonth()));
					}
				} catch (BusinessException e) {
					LOG.error("Problem while calculating pray time: ", e);
					throw new RuntimeException(e);
				}
			});
		});
		return docs;
	}

}
