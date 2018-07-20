package com.ahajri.heaven.calendar.service;

import java.util.Date;
import java.util.List;

import com.ahajri.heaven.calendar.collection.EventCollection;
import com.ahajri.heaven.calendar.exception.BusinessException;
import com.ahajri.heaven.calendar.queries.QueryParam;

/**
 * 
 * @author
 *         <p>
 *         ahajri
 *         </p>
 *
 */
public interface EventService {

	public List<EventCollection> findByDateBetween(Date fromDate, Date toDate) throws BusinessException;

	public List<EventCollection> save(EventCollection event) throws BusinessException;

	public EventCollection findById(String id) throws BusinessException;

	

	public List<EventCollection> findAll() throws BusinessException;
	
	public void delete(EventCollection event) throws BusinessException;
	
	public void deleteByCriteria(QueryParam... qp) throws BusinessException;
	
	public List<EventCollection> findByCriteria(QueryParam... qp) throws BusinessException;
}
