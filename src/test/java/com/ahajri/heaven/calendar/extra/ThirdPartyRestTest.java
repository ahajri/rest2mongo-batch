package com.ahajri.heaven.calendar.extra;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.junit4.SpringRunner;

import com.ahajri.heaven.calendar.constants.KeyConstants;
import com.ahajri.heaven.calendar.constants.TimeConstants;
import com.ahajri.heaven.calendar.constants.UrlConstants;
import com.ahajri.heaven.calendar.exception.BusinessException;
import com.ahajri.heaven.calendar.service.impl.PrayTimeServiceImpl;
import com.ahajri.heaven.calendar.utils.HCDateUtils;
import com.ahajri.heaven.calendar.utils.HttpUtils;

@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ThirdPartyRestTest {
	
	PrayTimeServiceImpl prayTimeService = new PrayTimeServiceImpl() ;

	@SuppressWarnings({ "rawtypes" })
	@Test
	@Ignore
	public void test1_givenUrlExists_whenSunriseAndSunsetRestApiRetrieved_thenPrayTimeCreated()
			throws ClientProtocolException, IOException, URISyntaxException, ParseException {
		
		

		// Given: Lille town
		Map<String, Object> params = new HashMap<>();
		params.put(KeyConstants.LATITUDE, 50.6333);
		params.put(KeyConstants.LONGITUDE, 3.0667);
		params.put(KeyConstants.DATE, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		
		Map<String, Object> resource = HttpUtils.doHttpGet(UrlConstants.SUNRISE_SUNSET_URL, params);
		
		DateFormat datetimeFormat = new SimpleDateFormat(TimeConstants.AM_PM_TIME_PATTERN);
		Date sunriseDate = datetimeFormat.parse((String) ((Map) resource.get("results")).get("sunrise"));
		Date sunsetDate = datetimeFormat.parse((String) ((Map) resource.get("results")).get("sunset"));
		Calendar c = Calendar.getInstance();
		c.clear();
		c.setTime(sunriseDate);
		LocalDateTime sunriseDateTime = LocalDateTime.of(LocalDate.now(),
				LocalTime.of(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND)));
		c.clear();
		c.setTime(sunsetDate);

		LocalDateTime aichaainDateTime = LocalDateTime
				.of(LocalDate.now(),
						LocalTime.of(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND)))
				.plusMinutes(15);

		String dayLength = (String) ((Map) resource.get("results")).get("day_length");

		// adding 2 minutes i7tiyat
		Date dohrainTime = datetimeFormat.parse((String) ((Map) resource.get("results")).get("solar_noon"));
		c.clear();
		c.setTime(dohrainTime);
		LocalDateTime dohrainDateTime = LocalDateTime
				.of(LocalDate.now(),
						LocalTime.of(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND)))
				.plusMinutes(2);
		System.out.println("###dayLength####" + dayLength);
		int hours = Integer.parseInt(dayLength.split(":")[0]);
		int minutes = Integer.parseInt(dayLength.split(":")[1]);
		int seconds = Integer.parseInt(dayLength.split(":")[2]);
		int dayLengthSeconds = seconds + (minutes * 60) + (hours * 3600);
		System.out.println("##DayLenth##" + dayLengthSeconds);
		int nightLengthSecond = (TimeConstants.SECONDS_IN_DAY - dayLengthSeconds);
		System.out.println("#NightLength#" + nightLengthSecond);

		String nightLength = HCDateUtils.getDurationString(nightLengthSecond);
		System.out.println("#nightLength#" + nightLength);
		c.clear();
		c.setTime(sunsetDate);
		LocalDateTime midnightChar3iDateTime = LocalDateTime
				.of(LocalDate.now(),
						LocalTime.of(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND)))
				.plusSeconds(Long.parseLong("" + (nightLengthSecond / 2)));
		LocalDateTime sunsetDateTime = LocalDateTime.of(LocalDate.now(),
				LocalTime.of(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND)));
		System.out.println("#midnightChar3iDateTime#" + midnightChar3iDateTime);
		// Duration day_length = Duration.between(sunriseDateTime,
		// sunsetDateTime);
		// long s = day_length.toMillis()/1000;
		// System.out.println(String.format("%d:%02d:%02d", s / 3600, (s % 3600)
		// / 60, (s % 60)));
		assertNotNull(sunriseDateTime);

		// midnightChar3i

		Map<String, Object> result = new HashMap<>();
		result.put(KeyConstants.DohrainPrayTime, dohrainDateTime);
		result.put(KeyConstants.SunriseDateTime, sunriseDateTime);
		result.put(KeyConstants.SunriseDateTime, sunsetDateTime);
		result.put(KeyConstants.AichaainDateTime, aichaainDateTime);
		result.put(KeyConstants.DayLength, dayLength);
		result.put(KeyConstants.MidnightCharaiTime, midnightChar3iDateTime);
		// TODO: Fajr Pray Time

		System.out.println(result.toString());

		// TODO: Quiem Pray Time
//		LocalDate start = LocalDate.parse("2018-01-01"), end = LocalDate.parse("2018-12-31");
//		Stream.iterate(start, date -> date.plusDays(1)).limit(ChronoUnit.DAYS.between(start, end) + 1)
//				.forEach(System.out::println);
	}

	
	@Test
	public void tes2_PrayTime(){
		try {
			Map<String, Object> prayData = prayTimeService.getPrayTimeByLatLngDate(48.8566, 2.3522, new Date(),"CET");
			System.out.println("#################"+prayData.toString());
			Assert.assertTrue(true);
		} catch (BusinessException e) {
			Assert.fail(e.getCause().getMessage());
		}
	}
}
