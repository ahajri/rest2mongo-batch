package com.ahajri.hc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ahajri.hc.enums.ErrorMessageEnum;
import com.ahajri.hc.enums.MessageEnum;
import com.ahajri.hc.exception.BusinessException;
import com.ahajri.hc.exception.RestException;
import com.ahajri.hc.model.HUser;
import com.ahajri.hc.mongo.cloud.CloudMongoService;
import com.ahajri.hc.queries.QueryParam;
import com.ahajri.hc.utils.SecurityUtils;
import com.google.gson.Gson;


@RestController
@RequestMapping("/user")
public class UserController  {

	private static final String USER_COLLECTION_NAME = "users";
	
	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
	
	@Value("${application.key}")
	protected String applicationKey;

	@Value("${access.token}")
	protected String accessToken;

	
	@Autowired
	private CloudMongoService cloudMongoService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private static  final Gson gson = new Gson();
	

	

	/**
	 * Create User with email and pasword and roles
	 * 
	 * @param user
	 * @return void
	 * @throws RestException
	 */
	@PostMapping(path = "/add")
	public ResponseEntity<HUser> createUser(@Valid @RequestBody HUser user) throws RestException {
		
		// verify if user already created
		Document query = new Document();
		query.append("email", user.getEmail());

		List<Document> docs;
		try {
			docs = cloudMongoService.findByExample(USER_COLLECTION_NAME, query);
			if (docs.size() >= 1) {
				throw new RestException(ErrorMessageEnum.EMAIL_TOKEN.getMessage(user.getEmail()),
						new BusinessException(new Exception(ErrorMessageEnum.EMAIL_TOKEN.getMessage(user.getEmail())),
								ErrorMessageEnum.EMAIL_TOKEN.getMessage(user.getEmail())),
						HttpStatus.CONFLICT, null);
			}

			Document userDocument = new Document();
			userDocument.append("email", user.getEmail());
			String encodedPassword = passwordEncoder.encode(user.getPassword());
			userDocument.append("password", encodedPassword);
			userDocument.append("actif", user.isActif());
			userDocument.append("token", SecurityUtils.generateRandomToken());
			List<Document> roles = new ArrayList<>();
			user.getRoles().stream().forEach(r -> {
				Document roleDocument = new Document("name", r.getName());
				roleDocument.append("description", r.getDescription());
				roles.add(roleDocument);
			});
			userDocument.append("roles", roles);
			cloudMongoService.insertOne(USER_COLLECTION_NAME, userDocument);

			HUser createdUser = gson.fromJson(
					cloudMongoService.findByExample(USER_COLLECTION_NAME, userDocument).get(0).toJson(), HUser.class);
			return ResponseEntity.ok(createdUser);
		} catch (BusinessException e) {
			LOG.error(e.getMessage(),e);
			throw new RestException(ErrorMessageEnum.USER_CREATION_KO.getMessage(e.getMessage()), e,
					HttpStatus.INTERNAL_SERVER_ERROR, null);
		}

	}

	/**
	 * 
	 * @param user
	 * @return Http reponse entity
	 * @throws RestException
	 */
	@PostMapping(path = "/update")
	public ResponseEntity<String> updateUser(@Valid @RequestBody HUser user) throws RestException {
		try {
			Document userDocument = new Document();
			userDocument.append("email", user.getEmail());

			List<Document> docs = cloudMongoService.findByExample(USER_COLLECTION_NAME, userDocument);
			if (CollectionUtils.isEmpty(docs)) {
				throw new RestException(ErrorMessageEnum.USER_NOT_FOUND_FOR_EMAIL.getMessage(user.getEmail()),
						new BusinessException(ErrorMessageEnum.USER_NOT_FOUND_FOR_EMAIL.getMessage(user.getEmail())),
						HttpStatus.NOT_FOUND, null);
			} else if (docs.size() > 1) {
				throw new RestException(ErrorMessageEnum.MORE_THAN_ONE_USER_FOR_EMAIL.getMessage(user.getEmail()),
						new BusinessException(
								ErrorMessageEnum.MORE_THAN_ONE_USER_FOR_EMAIL.getMessage(user.getEmail())),
						HttpStatus.CONFLICT, null);
			}
			Document found = docs.get(0);
			found.append("email", user.getEmail());
			found.append("password", user.getPassword());
			found.append("token", user.getToken());
			found.append("roles", user.getRoles().stream().map(r -> {
				Document role = new Document("name", r.getName());
				role.append("description", r.getDescription());
				return role;
			}).collect(Collectors.toSet()));
			found.append("actif", user.isActif());
			cloudMongoService.updateOne(USER_COLLECTION_NAME, new Document("email", user.getEmail()), found);

		} catch (BusinessException e) {
			LOG.error(e.getMessage(),e);
			throw new RestException(ErrorMessageEnum.USER_UPADATE_KO.getMessage(e.getMessage()), e,
					HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
		return ResponseEntity.ok(MessageEnum.USER_UPDATED_OK.getMessage(user.getEmail()));
	}

	@DeleteMapping(path = "/delete")
	public ResponseEntity<String> deleteUser(@Valid @RequestBody HUser user) throws RestException {
		try {
			Document userDocument = new Document();
			userDocument.append("email", user.getEmail());

			List<Document> docs = cloudMongoService.findByExample(USER_COLLECTION_NAME, userDocument);
			if (CollectionUtils.isEmpty(docs)) {
				throw new RestException(ErrorMessageEnum.USER_NOT_FOUND_FOR_EMAIL.getMessage(user.getEmail()),
						new BusinessException(ErrorMessageEnum.USER_NOT_FOUND_FOR_EMAIL.getMessage(user.getEmail())),
						HttpStatus.NOT_FOUND, null);
			} else if (docs.size() > 1) {
				throw new RestException(ErrorMessageEnum.MORE_THAN_ONE_USER_FOR_EMAIL.getMessage(user.getEmail()),
						new BusinessException(
								ErrorMessageEnum.MORE_THAN_ONE_USER_FOR_EMAIL.getMessage(user.getEmail())),
						HttpStatus.CONFLICT, null);
			}
			Document found = docs.get(0);

			cloudMongoService.deleteOne(USER_COLLECTION_NAME, found);

		} catch (BusinessException e) {
			LOG.error(e.getMessage(),e);
			throw new RestException(ErrorMessageEnum.DELETE_DOCUMENT_KO.getMessage(e.getMessage()), e,
					HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
		return ResponseEntity.ok(MessageEnum.USER_DELETED_OK.getMessage(user.getEmail()));
	}

	@PostMapping(path = "/search")
	@ResponseBody
	public ResponseEntity<List<HUser>> searchUser(final @RequestBody QueryParam... qp) throws RestException {
		List<HUser> result = new ArrayList<>();
		
		try {
			result = cloudMongoService.search(USER_COLLECTION_NAME, qp).stream()
					.map(d -> gson.fromJson(gson.toJson(d), HUser.class)).collect(Collectors.toList());
		} catch (BusinessException e) {
			LOG.error(e.getMessage(),e);
			throw new RestException(ErrorMessageEnum.SEARCH_USER_KO.getMessage(e.getMessage()), e,
					HttpStatus.NOT_FOUND, null);
		}
		return ResponseEntity.ok(result);
	}
}
