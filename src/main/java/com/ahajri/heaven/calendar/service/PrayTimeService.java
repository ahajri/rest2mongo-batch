package com.ahajri.heaven.calendar.service;

import java.util.Date;
import java.util.Map;

import com.ahajri.heaven.calendar.exception.BusinessException;

public interface PrayTimeService {

	/**
	 * Method to retrieve pray time using hadith AHl Ul Bait Peace from God Upon
	 * them
	 * 
	 * @param lat:
	 *            latitude
	 * @param lng:
	 *            longitude
	 * @param date:
	 *            date
	 * 
	 * @param timeZone:
	 *            Tiem Zone
	 * 
	 * @return {@link Map} of pray time
	 * 
	 * @throws TechnicalException
	 */
	Map<String, Object> getPrayTimeByLatLngDate(double d, double e, Date date, String timeZone)
			throws BusinessException;

}
