package un.light.mafhh.service.impl;

import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import un.light.mafhh.collection.User;
import un.light.mafhh.mongo.repository.UserRepository;
import un.light.mafhh.service.UserService;

@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private UserRepository userRepository;

	

	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(userId);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				getAuthority());
	}

	private List<SimpleGrantedAuthority> getAuthority() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}

	@Override
	public List<User> findAll() {
		List<User> list = new ArrayList<>();
		userRepository.findAll().iterator().forEachRemaining(list::add);
		return mongoTemplate.find(new Query(), User.class, UserCollectionName);
	}

	@Override
	public User save(User user) {
		mongoTemplate.save(user, UserCollectionName);
		return findById(user.getId());
	}

	@Override
	public void delete(String id) {
		userRepository.delete(id);
	}

	@Override
	public void delete(User user) {
		userRepository.delete(user);
	}
	
	@Override
	public User findById(String id) {
		return mongoTemplate.findById(id, User.class, UserCollectionName);
	}

	@Override
	public User findByUsername(String username) throws UnexpectedException {
		Query q = new Query();
		q.addCriteria(Criteria.where("username").is(username));
		List<User> found = mongoTemplate.find(q, User.class, UserCollectionName);
		if (found.size() > 1) {
			throw new UnexpectedException("Plus d'un utilisateur avec le même username: "+username+" trouvé");
		}
		found.stream().forEach(u -> System.out.println("--->" + u.getUsername()));
		return userRepository.findByUsername(username);
	}
}
