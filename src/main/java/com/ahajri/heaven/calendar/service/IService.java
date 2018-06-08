package com.ahajri.heaven.calendar.service;

import java.util.List;

import com.ahajri.heaven.calendar.queries.QueryParam;
import com.ahajri.heaven.calendar.security.exception.FunctionalException;
import com.ahajri.heaven.calendar.security.exception.TechnicalException;

public interface IService<T> {
	
	public T create(T toPersist) throws TechnicalException, FunctionalException;

	public void update(T collection) throws TechnicalException, FunctionalException;

	public T findById(String bookId) throws TechnicalException, FunctionalException;

	public void remove(T collection) throws TechnicalException, FunctionalException;
	
	public void removeAll(List<T> collections) throws TechnicalException, FunctionalException;

	public List<T> findByCriteria(QueryParam... qp) throws TechnicalException;

	public List<T> findAll() throws TechnicalException, FunctionalException;

	public void deleteByCriteria(QueryParam... qp) throws TechnicalException, FunctionalException;

}
