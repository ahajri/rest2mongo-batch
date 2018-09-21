package com.ahajri.heaven.calendar.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ahajri.heaven.calendar.constants.enums.OperatorEnum;
import com.ahajri.heaven.calendar.enums.ErrorMessageEnum;
import com.ahajri.heaven.calendar.exception.BusinessException;
import com.ahajri.heaven.calendar.exception.RestException;
import com.ahajri.heaven.calendar.model.HUser;
import com.ahajri.heaven.calendar.mongo.cloud.CloudMongoService;
import com.ahajri.heaven.calendar.queries.QueryParam;
import com.ahajri.heaven.calendar.security.LoginRequest;
import com.google.gson.Gson;

@RestController
public class LoginController {

	@Value("${application.key}")
	protected String applicationKey;

	@Value("${access.token}")
	protected String accessToken;

	@Autowired
	private CloudMongoService cloudMongoService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private static final Gson gson = new Gson();

	private static Logger LOG = LoggerFactory.getLogger(LoginController.class);

	private static final String USER_COLLECTION_NAME = "users";

	@PostMapping(path = "/login")
	public ResponseEntity<HUser> login(@Valid @RequestBody LoginRequest loginRequest) throws RestException {

		HUser result = new HUser();

		QueryParam[] qps = new QueryParam[2];

		qps[0] = new QueryParam("email", OperatorEnum.EQ.name(), loginRequest.getEmail());
		List<HUser> foundUsers = null;
		try {
			foundUsers = cloudMongoService.search(USER_COLLECTION_NAME, qps).stream()
					.map(d -> gson.fromJson(gson.toJson(d), HUser.class)).collect(Collectors.toList());
		} catch (BusinessException e) {
			LOG.error(e.getMessage(), e);
			throw new RestException(ErrorMessageEnum.USER_NOT_FOUND_FOR_EMAIL.getMessage(loginRequest.getEmail()), e,
					HttpStatus.NOT_FOUND);
		}
LOG.info("####################"+foundUsers.toString());
		if (foundUsers.size() > 1) {
			throw new RestException(ErrorMessageEnum.MORE_THAN_ONE_USER_FOR_EMAIL.getMessage(loginRequest.getEmail()),
					new Exception(ErrorMessageEnum.MORE_THAN_ONE_USER_FOR_EMAIL.getMessage(loginRequest.getEmail())),
					HttpStatus.NOT_FOUND);
		}
		
		result=foundUsers.get(0);
		if (!passwordEncoder.matches(loginRequest.getPassword(), result.getPassword())) {
			throw new RestException(ErrorMessageEnum.WRONG_PASSWORD.getMessage(),
					new Exception(ErrorMessageEnum.WRONG_PASSWORD.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
		result.setPassword(null);

		return ResponseEntity.ok(result);
	}
	
	@GetMapping(path="/status")
	public ResponseEntity<String> getStatus(){
		return ResponseEntity.ok("Status OK");
	}

}
