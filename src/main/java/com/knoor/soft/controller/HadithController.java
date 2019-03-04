package com.knoor.soft.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.knoor.soft.enums.ErrorMessageEnum;
import com.knoor.soft.exception.BusinessException;
import com.knoor.soft.exception.RestException;
import com.knoor.soft.mongo.cloud.CloudMongoService;

@RestController
@RequestMapping("/knoor/api/v1")
public class HadithController {
	
	private final Logger LOG = LoggerFactory.getLogger(HadithController.class);
	
	private static String HADITH_COLLECTION="hadiths" ;
	
	@Autowired
	private CloudMongoService cloudMongoService;
	
	
	@PostMapping(path = "/hadiths/duplicate")
	@ResponseBody
	public ResponseEntity<List<Document>> searchEvents() throws RestException {
		List<Document> result = new ArrayList<>();
		final Gson gson = new Gson();
		try {
			result = cloudMongoService.findDuplicateDocs(HADITH_COLLECTION).stream()
					.map(d -> gson.fromJson(gson.toJson(d), Document.class)).collect(Collectors.toList());
		} catch (BusinessException e) {
			LOG.error(e.getMessage(),e);
			throw new RestException(ErrorMessageEnum.DUPLICATE_HADITH_KO.getMessage(e.getMessage()), e,
					HttpStatus.NOT_FOUND, null);
		}
		return ResponseEntity.ok(result);
	}

}
