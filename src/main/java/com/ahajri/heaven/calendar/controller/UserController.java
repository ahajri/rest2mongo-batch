package com.ahajri.heaven.calendar.controller;

import java.rmi.UnexpectedException;
import java.security.Principal;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ahajri.heaven.calendar.collection.UserAuth;
import com.ahajri.heaven.calendar.security.exception.RestException;
import com.ahajri.heaven.calendar.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private RestTemplate restTemplate;

	private final static Logger log = Logger.getLogger(UserController.class);

	@PostConstruct
	public void init() {

	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public List<UserAuth> listUser() {
		return userService.findAll();
	}

	@RequestMapping(value = "/post", method = RequestMethod.POST)
	public UserAuth create(@RequestBody UserAuth user) {
		String pwd = user.getPassword();
		if (!user.isPasswordEncrypted()) {
			// FIXME: encrypt password
			user.setPasswordEncrypted(true);
		}
		return userService.save(user);
	}

	@RequestMapping(value = "/delete/{username}", method = RequestMethod.DELETE)
	public String delete(@PathVariable(value = "username") String username) throws RestException {
		try {
			userService.delete(userService.findByUsername(username));
		} catch (UnexpectedException e) {
			throw new RestException(e.getMessage(),e, HttpStatus.INTERNAL_SERVER_ERROR,
					StringUtils.newStringUtf8("".getBytes()));
		}
		return "deleted";
	}

	@RequestMapping(value = "/byUsername/{username}", method = RequestMethod.GET)
	public ResponseEntity<UserAuth> findByUsername(@PathVariable(value = "username") String username) throws RestException {
		try {
			UserAuth found = userService.findByUsername(username);
			if (found == null) {
				throw new RestException("User Not Found", new Exception("User Not Found"), HttpStatus.NOT_FOUND,
						StringUtils.newStringUtf8("".getBytes()));
			}
			return new ResponseEntity<UserAuth>(found, HttpStatus.FOUND);
		} catch (UnexpectedException e) {
			throw new RestException(e.getMessage(), e, HttpStatus.CONFLICT, StringUtils.newStringUtf8("".getBytes()));
		}
	}
	
	
	@RequestMapping("/user")
	public Principal user(Principal principal) {
		return principal;
	}
}
