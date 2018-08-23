package com.ahajri.heaven.calendar.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

import com.ahajri.heaven.calendar.constants.KeyConstants;
import com.ahajri.heaven.calendar.constants.TimeConstants;
import com.ahajri.heaven.calendar.constants.UrlConstants;
import com.ahajri.heaven.calendar.exception.BusinessException;
import com.ahajri.heaven.calendar.service.PrayTimeService;
import com.ahajri.heaven.calendar.utils.HCDateUtils;
import com.ahajri.heaven.calendar.utils.HttpUtils;
/**
 * 
 * @author ahajri
 *
 */
@Service("prayTimeService")
public class PrayTimeServiceImpl implements PrayTimeService {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> getPrayTimeByLatLngDate(final double lat, final double lng, final Date date)
			throws BusinessException {

		final Map<String, Object> result = new HashMap<>();
		final Map<String, Object> params = new HashMap<>();
		params.put(KeyConstants.LATITUDE, lat);
		params.put(KeyConstants.LONGITUDE, lng);
		params.put(KeyConstants.DATE, new SimpleDateFormat("yyyy-MM-dd").format(date));
		final DateFormat datetimeFormat = new SimpleDateFormat(TimeConstants.AM_PM_TIME_PATTERN);
		HttpUriRequest request;
		try {
			request = new HttpGet(HttpUtils.buildParamUrl(UrlConstants.SUNRISE_SUNSET_URL, params));
			HttpResponse response = HttpClientBuilder.create().build().execute(request);
			Map<String, Object> resource = HttpUtils.retrieveResourceFromResponse(response, Map.class);
			Date sunriseDate = datetimeFormat.parse((String) ((Map) resource.get("results")).get("sunrise"));
			Date sunsetDate = datetimeFormat.parse((String) ((Map) resource.get("results")).get("sunset"));
			Calendar c = Calendar.getInstance();
			c.clear();
			c.setTime(sunriseDate);
			LocalDateTime sunriseDateTime = LocalDateTime.of(LocalDate.now(),
					LocalTime.of(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND)));
			c.clear();
			c.setTime(sunsetDate);
			System.out.println("--->"+ZoneId.getAvailableZoneIds().toString());
			LocalDateTime aichaainDateTime = LocalDateTime
					.of(LocalDate.now(ZoneId.of("Europe/Paris")),
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
			int hours = Integer.parseInt(dayLength.split(":")[0]);
			int minutes = Integer.parseInt(dayLength.split(":")[1]);
			int seconds = Integer.parseInt(dayLength.split(":")[2]);
			int dayLengthSeconds = seconds + (minutes * 60) + (hours * 3600);
			int dayHourChar3iSeconds =dayLengthSeconds/12;
			int nightLengthSecond = (TimeConstants.SECONDS_IN_DAY - dayLengthSeconds);
			int nightHourChar3iSeconds = nightLengthSecond/12;
			int fajrHour = 0;
			if (nightHourChar3iSeconds>dayHourChar3iSeconds) {
				fajrHour = nightHourChar3iSeconds;
			} else {
				fajrHour = dayHourChar3iSeconds;
			}
			
			c.clear();
			c.setTime(sunriseDate);
			LocalDateTime fajrPrayDateTime = LocalDateTime.of(LocalDate.now(),
					LocalTime.of(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND))).minusSeconds(fajrHour);

			String nightLength = HCDateUtils.getDurationString(nightLengthSecond);
			c.clear();
			c.setTime(sunsetDate);
			LocalDateTime midnightChar3iDateTime = LocalDateTime
					.of(LocalDate.now(),
							LocalTime.of(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND)))
					.plusSeconds(Long.parseLong("" + (nightLengthSecond / 2)));
			LocalDateTime sunsetDateTime = LocalDateTime.of(LocalDate.now(),
					LocalTime.of(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND)));
			
			result.put(KeyConstants.DohrainPrayTime, dohrainDateTime);
			result.put(KeyConstants.SunriseDateTime, sunriseDateTime);
			result.put(KeyConstants.SunsetDateTime, sunsetDateTime);
			result.put(KeyConstants.AichaainDateTime, aichaainDateTime);
			result.put(KeyConstants.DayLength, dayLength);
			result.put(KeyConstants.MidnightCharaiTime, midnightChar3iDateTime);
			result.put(KeyConstants.NighhtLength, nightLength);
			result.put(KeyConstants.FajrPrayTime, fajrPrayDateTime);
		} catch (URISyntaxException | IOException | ParseException e) {
			throw new BusinessException(e);
		} 

		return result;
	}

}
