package com.ahajri.hc.utils;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.ahajri.hc.constants.KeyConstants;

public final class HCDateUtils {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss S");

	private final static Calendar c = Calendar.getInstance();

	/**
	 * Method to calculate real pray time
	 * 
	 * @param sunrise:
	 *            Sunrise Time
	 * @param sunset:
	 *            Sunset Time
	 * @return: {@link Map} of PrayTimes
	 */
	public static final HashMap<String, Date> getPrayTimes(@NotNull Date sunrise, @NotNull Date sunset) {
		LocalDateTime sunriseLdt = LocalDateTime.ofInstant(sunrise.toInstant(), ZoneId.systemDefault());
		LocalDateTime sunsetLdt = LocalDateTime.ofInstant(sunset.toInstant(), ZoneId.systemDefault());
		Duration dayDuration = Duration.between(sunriseLdt, sunsetLdt);
		long dayMinutes = dayDuration.toMinutes();
		long realDayMinutesPerHour = dayMinutes / 12;
		sunriseLdt.plusMinutes((realDayMinutesPerHour * 6));
		HashMap<String, Date> prayTimes = new HashMap<>();
		prayTimes.put(KeyConstants.DohrainPrayTime, Date.from(sunriseLdt.atZone(ZoneId.systemDefault()).toInstant()));
		return prayTimes;
	}

	public static String getDurationString(int seconds) {

		int hours = seconds / 3600;
		int minutes = (seconds % 3600) / 60;
		seconds = seconds % 60;

		return twoDigitString(hours) + ":" + twoDigitString(minutes) + ":" + twoDigitString(seconds);
	}

	private static String twoDigitString(int number) {

		if (number == 0) {
			return "00";
		}

		if (number / 10 == 0) {
			return "0" + number;
		}

		return String.valueOf(number);
	}

	/**
	 * 
	 * @return
	 */
	public static Date getMaxDate() {
		c.clear();
		c.set(Calendar.YEAR, 9999);
		c.set(Calendar.MONTH, 11);
		c.set(Calendar.DAY_OF_MONTH, 31);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTime();
	}

	public static LocalDate asLocalDate(java.util.Date date, ZoneId zone) {
		if (date == null)
			return null;

		if (date instanceof java.sql.Date)
			return ((java.sql.Date) date).toLocalDate();
		else
			return Instant.ofEpochMilli(date.getTime()).atZone(zone).toLocalDate();
	}

	public static java.util.Date asUtilDate(Object date, ZoneId zone) {
		if (date == null)
			return null;

		if (date instanceof java.sql.Date || date instanceof java.sql.Timestamp)
			return new java.util.Date(((java.util.Date) date).getTime());
		if (date instanceof java.util.Date)
			return (java.util.Date) date;
		if (date instanceof LocalDate)
			return java.util.Date.from(((LocalDate) date).atStartOfDay(zone).toInstant());
		if (date instanceof LocalDateTime)
			return java.util.Date.from(((LocalDateTime) date).atZone(zone).toInstant());
		if (date instanceof ZonedDateTime)
			return java.util.Date.from(((ZonedDateTime) date).toInstant());
		if (date instanceof Instant)
			return java.util.Date.from((Instant) date);

		throw new UnsupportedOperationException(
				"Don't know hot to convert " + date.getClass().getName() + " to java.util.Date");
	}

	/**
	 * 
	 * @param input
	 * @param days
	 * @return incremented days
	 */
	public static Date incrementDateByDays(Date input, int days) {
		c.clear();
		c.setTime(input);
		c.add(Calendar.DATE, days);
		return c.getTime();
	}

	public static Date incrementDateByHours(Date input, int hours) {
		c.clear();
		c.setTime(input);
		c.add(Calendar.HOUR_OF_DAY, hours);
		return c.getTime();
	}

	public static Date incrementDateByWeeks(Date input, int weeks) {
		LocalDate nextWeek = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plus(weeks,
				ChronoUnit.WEEKS);
		return asUtilDate(nextWeek, ZoneId.systemDefault());
	}

	public static Date incrementDateByMonths(Date input, int months) {
		LocalDate nextMonths = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plus(months,
				ChronoUnit.MONTHS);
		return asUtilDate(nextMonths, ZoneId.systemDefault());
	}
	
	public static Date incrementDateByYears(Date input, int years) {
		LocalDate nextYears = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plus(years,
				ChronoUnit.YEARS);
		return asUtilDate(nextYears, ZoneId.systemDefault());
	}

	public static Date convertToDateViaSqlTimestamp(LocalDateTime dateToConvert) {
		return Timestamp.valueOf(dateToConvert);
	}

}
