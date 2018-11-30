package com.ahajri.hc.batch.config;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.ahajri.hc.batch.beans.BCountry;
import com.ahajri.hc.constants.enums.EventType;
import com.ahajri.hc.constants.enums.OperatorEnum;
import com.ahajri.hc.constants.enums.RecurringEnum;
import com.ahajri.hc.exception.BusinessException;
import com.ahajri.hc.mongo.cloud.CloudMongoService;
import com.ahajri.hc.queries.QueryParam;
import com.ahajri.hc.service.PrayTimeService;
import com.ahajri.hc.utils.HCDateUtils;
import com.google.gson.Gson;

public class BCountryPrayTimeEventItemProcessor implements ItemProcessor<List<BCountry>, List<Document>> {

	private static final String EVENTS_COLLECTION_NAME = "event";

	private static final Logger LOG = LoggerFactory.getLogger(BCountryPrayTimeEventItemProcessor.class);

	@Autowired
	private PrayTimeService prayTimeService;

	@Autowired
	private CloudMongoService cloudMongoService;

	@Override
	public List<Document> process(List<BCountry> items) throws Exception {
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
						LOG.debug(String.format("====>Pray time already exists for city: %s, month: %d, day:  %d",
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
