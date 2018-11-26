package com.ahajri.hc.service;

import java.io.IOException;
import java.net.MalformedURLException;
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
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

import com.ahajri.hc.constants.KeyConstants;
import com.ahajri.hc.constants.TimeConstants;
import com.ahajri.hc.constants.UrlConstants;
import com.ahajri.hc.exception.BusinessException;
import com.ahajri.hc.utils.HCDateUtils;
import com.ahajri.hc.utils.HttpUtils;

/**
 * 
 * @author ahajri
 *
 */
@Service("prayTimeService")
public class PrayTimeService {

	private static final String DATE_FORMAT = "yyyy-MM-dd";



	@SuppressWarnings({ "rawtypes" })
	public Map<String, Object> getPrayTimeByLatLngDate(final double lat, final double lng, final Date date, final String timeZone)
			throws BusinessException {

		final Map<String, Object> result = new HashMap<>();
		final Map<String, Object> params = new HashMap<>();
		params.put(KeyConstants.LATITUDE, lat);
		params.put(KeyConstants.LONGITUDE, lng);
		params.put(KeyConstants.DATE, new SimpleDateFormat(DATE_FORMAT).format(date));
		final DateFormat datetimeFormat = new SimpleDateFormat(TimeConstants.AM_PM_TIME_PATTERN);
		try {
			Map<String, Object> resource = callSunriseSunsetApi(params);
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
					.of(LocalDate.now(ZoneId.of(timeZone)),
							LocalTime.of(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND)))
					.plusMinutes(5);

			String dayLength = (String) ((Map) resource.get("results")).get("day_length");

			
			Date dohrainTime = datetimeFormat.parse((String) ((Map) resource.get("results")).get("solar_noon"));
			c.clear();
			c.setTime(dohrainTime);
			// adding 2 minutes i7tiyat
			LocalDateTime dohrainDateTime = LocalDateTime
					.of(LocalDate.now(),
							LocalTime.of(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND)))
					.plusMinutes(2);
			int hours = Integer.parseInt(dayLength.split(":")[0]);
			int minutes = Integer.parseInt(dayLength.split(":")[1]);
			int seconds = Integer.parseInt(dayLength.split(":")[2]);
			int dayLengthSeconds = seconds + (minutes * 60) + (hours * 3600);
			int dayHourChar3iSeconds = dayLengthSeconds / 12;
			int nightLengthSecond = (TimeConstants.SECONDS_IN_DAY - dayLengthSeconds);
			int nightHourChar3iSeconds = nightLengthSecond / 12;
			int fajrHour = 0;
			if (nightHourChar3iSeconds > dayHourChar3iSeconds) {
				fajrHour = nightHourChar3iSeconds;
			} else {
				fajrHour = dayHourChar3iSeconds;
			}

			c.clear();
			c.setTime(sunriseDate);
			LocalDateTime fajrPrayDateTime = LocalDateTime
					.of(LocalDate.now(),
							LocalTime.of(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND)))
					.minusSeconds(fajrHour);

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
			result.put(KeyConstants.MidnightChariiTime, midnightChar3iDateTime);
			result.put(KeyConstants.NightLength, nightLength);
			result.put(KeyConstants.FajrPrayTime, fajrPrayDateTime);
		} catch (URISyntaxException | IOException | ParseException e) {
			throw new BusinessException(e);
		}

		return result;
	}



	@SuppressWarnings("unchecked")
	private Map<String, Object> callSunriseSunsetApi(final Map<String, Object> params)
			throws URISyntaxException, MalformedURLException, IOException, ClientProtocolException {
		HttpUriRequest request = new HttpGet(HttpUtils.buildParamUrl(UrlConstants.SUNRISE_SUNSET_URL, params));
		HttpResponse response = HttpClientBuilder.create().build().execute(request);
		Map<String, Object> resource = HttpUtils.retrieveResourceFromResponse(response, Map.class);
		return resource;
	}

	
	
	public static void main(String[] args) throws BusinessException {
		PrayTimeService s = new PrayTimeService();
		System.out.println(s.getPrayTimeByLatLngDate(55.676098, 12.568337, new Date(),"CET").toString());
	}

}
