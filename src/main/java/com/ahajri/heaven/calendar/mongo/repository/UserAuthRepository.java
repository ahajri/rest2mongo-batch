package com.ahajri.heaven.calendar.mongo.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.ahajri.heaven.calendar.collection.UserAuth;
/**
 * 
 * @author ahajri
 *
 */
public interface UserAuthRepository extends MongoRepository<UserAuth, String> {

	public UserAuth findByUsername(String username);

	public UserAuth findByUsernameAndPassword(String username, String password);

	public UserAuth save(UserAuth entity);

	public  void delete(UserAuth entity) ;
	
	public long count() ;
	
	
	@Override
	default <S extends UserAuth> boolean exists(Example<S> example) {
		return false;
	}

}
