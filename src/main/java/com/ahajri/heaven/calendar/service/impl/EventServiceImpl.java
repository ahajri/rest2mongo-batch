package com.ahajri.heaven.calendar.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ahajri.heaven.calendar.collection.EventCollection;
import com.ahajri.heaven.calendar.security.exception.TechnicalException;
import com.ahajri.heaven.calendar.service.EventService;

@Component(value="eventService")
public class EventServiceImpl implements EventService {

	@Override
	public List<EventCollection> findByDateBetween(Date fromDate, Date toDate) throws TechnicalException {
		
		return null;
	}

}
