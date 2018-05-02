package com.ahajri.heaven.calendar.service.impl;

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

import com.ahajri.heaven.calendar.collection.UserAuth;
import com.ahajri.heaven.calendar.mongo.repository.UserAuthRepository;
import com.ahajri.heaven.calendar.service.UserService;

@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private UserAuthRepository userRepository;

	

	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		UserAuth user = userRepository.findByUsername(userId);
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
	public List<UserAuth> findAll() {
		List<UserAuth> list = new ArrayList<>();
		userRepository.findAll().iterator().forEachRemaining(list::add);
		return mongoTemplate.find(new Query(), UserAuth.class, UserCollectionName);
	}

	@Override
	public UserAuth save(UserAuth user) {
		mongoTemplate.save(user, UserCollectionName);
		return findById(user.getId());
	}

	@Override
	public void delete(String id) {
		userRepository.delete(id);
	}

	@Override
	public void delete(UserAuth user) {
		userRepository.delete(user);
	}
	
	@Override
	public UserAuth findById(String id) {
		return mongoTemplate.findById(id, UserAuth.class, UserCollectionName);
	}

	@Override
	public UserAuth findByUsername(String username) throws UnexpectedException {
		Query q = new Query();
		q.addCriteria(Criteria.where("username").is(username));
		List<UserAuth> found = mongoTemplate.find(q, UserAuth.class, UserCollectionName);
		if (found.size() > 1) {
			throw new UnexpectedException("Plus d'un utilisateur avec le même username: "+username+" trouvé");
		}
		found.stream().forEach(u -> System.out.println("--->" + u.getUsername()));
		return userRepository.findByUsername(username);
	}
}
