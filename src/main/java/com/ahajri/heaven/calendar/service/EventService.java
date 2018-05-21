package com.ahajri.heaven.calendar.service;

import java.util.Date;
import java.util.List;

import com.ahajri.heaven.calendar.collection.EventCollection;
import com.ahajri.heaven.calendar.queries.QueryParam;
import com.ahajri.heaven.calendar.security.exception.TechnicalException;

/**
 * 
 * @author
 *         <p>
 *         ahajri
 *         </p>
 *
 */
public interface EventService {

	public List<EventCollection> findByDateBetween(Date fromDate, Date toDate) throws TechnicalException;

	public EventCollection save(EventCollection event) throws TechnicalException;

	public EventCollection findById(String id) throws TechnicalException;

	public List<EventCollection> findByCriteria(QueryParam... qp) throws TechnicalException;

	public List<EventCollection> findAll() throws TechnicalException;
}
