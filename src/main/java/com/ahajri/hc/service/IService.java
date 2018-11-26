package com.ahajri.hc.service;

import java.util.List;

import com.ahajri.hc.exception.BusinessException;
import com.ahajri.hc.queries.QueryParam;

public interface IService<T> {
	
	public T create(T toPersist) throws  BusinessException;

	public void update(T collection) throws  BusinessException;

	public T findById(String bookId) throws  BusinessException;

	public void remove(T collection) throws  BusinessException;
	
	public void removeAll(List<T> collections) throws  BusinessException;

	public List<T> findByCriteria(List<QueryParam> paramList) throws BusinessException;

	public List<T> findAll() throws  BusinessException;

	public long deleteByCriteria(List<QueryParam> qp) throws  BusinessException;

}
