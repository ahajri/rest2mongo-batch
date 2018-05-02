package com.ahajri.heaven.calendar.service;

import java.rmi.UnexpectedException;
import java.util.List;

import com.ahajri.heaven.calendar.collection.UserAuth;

/**
 * 
 * @author ahajri
 *
 */
public interface UserService {
	static final String UserCollectionName = "UserAuth";

	List findAll();

	UserAuth save(UserAuth user);

	void delete(String id);

	UserAuth findById(String id);

	UserAuth findByUsername(String username) throws UnexpectedException;

	void delete(UserAuth user);

}
