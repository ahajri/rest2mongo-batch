package un.light.mafhh.mongo.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;

import un.light.mafhh.collection.User;
/**
 * 
 * @author ahajri
 *
 */
public interface UserRepository extends MongoRepository<User, String> {

	public User findByUsername(String username);

	public User findByUsernameAndPassword(String username, String password);

	public User save(User entity);

	public  void delete(User entity) ;
	
	public long count() ;
	
	
	@Override
	default <S extends User> boolean exists(Example<S> example) {
		return false;
	}

}
