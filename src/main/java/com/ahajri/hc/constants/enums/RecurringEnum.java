package com.ahajri.hc.constants.enums;

/**
 * 
 * @author ahajri
 *
 */
public enum RecurringEnum {

	ONETIME("ONETIME"), HOURLY("HOURLY"), DAILY("DAILY"), MONTHLY("MONTHLY"), WEEKLY("WEEKLY"), YEARLY("YEARLY");

	private String key;

	private RecurringEnum(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

}
