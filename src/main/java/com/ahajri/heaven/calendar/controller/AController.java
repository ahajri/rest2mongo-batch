package com.ahajri.heaven.calendar.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.ahajri.heaven.calendar.queries.QueryParam;
import com.ahajri.heaven.calendar.security.exception.RestException;

/**
 * Abstract Controllers class
 * 
 * @author ahajri
 *
 * @param <T>
 */
public abstract class AController<T> {

	protected static final String REQUEST_PARAM_DATE_FORMAT = "yyyyMMddHHmmss";

	public abstract ResponseEntity<T> create(@RequestBody T collection) throws RestException;

	public abstract ResponseEntity<Void> update(@RequestBody T collection) throws RestException;

	public abstract ResponseEntity<Void> remove(@RequestBody T collection) throws RestException;

	public abstract ResponseEntity<List<T>> findAll() throws RestException;

	public abstract ResponseEntity<Void> removeAll() throws RestException;

	public abstract ResponseEntity<Long> deleteByCriteria(String encryptedObjectQuery) throws RestException;

	public abstract ResponseEntity<Void> removeList(List<T> collections) throws RestException;

	public abstract ResponseEntity<List<T>> findByCriteria(String encryptedObjectQuery) throws RestException;

}
