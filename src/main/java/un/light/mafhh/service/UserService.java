package un.light.mafhh.service;

import java.rmi.UnexpectedException;
import java.util.List;

import un.light.mafhh.collection.User;

/**
 * 
 * @author ahajri
 *
 */
public interface UserService {
	static final String UserCollectionName = "User";

	List findAll();

	User save(User user);

	void delete(String id);

	User findById(String id);

	User findByUsername(String username) throws UnexpectedException;

	void delete(User user);

}
