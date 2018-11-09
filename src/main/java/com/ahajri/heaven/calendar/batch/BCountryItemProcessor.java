package com.ahajri.heaven.calendar.batch;

import org.springframework.batch.item.ItemProcessor;

import com.ahajri.heaven.calendar.model.BCountry;

public class BCountryItemProcessor implements ItemProcessor<BCountry, BCountry> {

	@Override
	public BCountry process(BCountry item) throws Exception {
		return item;
	}

}
