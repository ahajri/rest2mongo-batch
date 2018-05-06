package com.ahajri.heaven.calendar.service;

import java.util.Date;
import java.util.List;

import com.ahajri.heaven.calendar.collection.EventCollection;
import com.ahajri.heaven.calendar.security.exception.TechnicalException;

public interface EventService {
	
	public List<EventCollection> findByDateBetween(Date fromDate,Date toDate) throws TechnicalException;
}
