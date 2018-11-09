package com.ahajri.heaven.calendar.batch;

import org.springframework.batch.item.file.LineMapper;

import com.ahajri.heaven.calendar.model.BCountry;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BCountryJsonLineMapper implements LineMapper<BCountry> {
	
	 private ObjectMapper mapper = new ObjectMapper();


	@Override
	public BCountry mapLine(String line, int lineNumber) throws Exception {
		return mapper.readValue(line, BCountry.class);
	}

}
