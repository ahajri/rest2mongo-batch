package un.light.mafhh.service;

import java.util.Date;
import java.util.Map;

import un.light.mafhh.security.exception.TechnicalException;

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
	 * @return {@link Map} of pray time
	 * @throws TechnicalException
	 */
	Map<String, Object> getPrayTimeByLatLngDate(double d, double e, Date date) throws TechnicalException;

}
