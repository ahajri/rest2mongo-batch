package un.light.mafhh.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import un.light.mafhh.constants.KeyConstants;

public final class HCDateUtils {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss S");

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
	
	
	public static  String getDurationString(int seconds) {

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
	 * @param args
	 */
	public static void main(String[] args) {
		Date sunrise = new Date(2018, 1, 7, 8, 13);
		Date sunset = new Date(2018, 1, 7, 17, 53);
		System.out.println(getPrayTimes(sunrise, sunset));
	}

}
