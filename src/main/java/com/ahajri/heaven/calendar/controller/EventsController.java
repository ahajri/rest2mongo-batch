package com.ahajri.heaven.calendar.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahajri.heaven.calendar.exception.BusinessException;
import com.ahajri.heaven.calendar.exception.RestException;
import com.ahajri.heaven.calendar.mongo.cloud.CloudApiMongoService;
import com.ahajri.heaven.calendar.utils.HttpUtils;
import com.ahajri.heaven.calendar.utils.JsonUtils;

@RestController
@RequestMapping("/hcalendar/events")
public class EventsController {

	private static final String REQUEST_PARAM_DATE_FORMAT = "yyyyMMddHHmmss";

	@Autowired
	private CloudApiMongoService cloudApiMongoService;

	@RequestMapping(value = "/cloud/create", method = RequestMethod.POST)
	public ResponseEntity<Document> cloudCreateApi(@RequestBody Map<String, Object> eventMap) throws RestException {
		try {
			HttpResponse response = cloudApiMongoService.insertOne("event", eventMap);
			HttpEntity entity = response.getEntity();

			int httpCode = response.getStatusLine().getStatusCode();

			HttpStatus httpStatus = HttpUtils.statusCode2Status(httpCode);

			if (entity != null) {
				InputStream instream = entity.getContent();
				Document data = null;
				try {
					data = JsonUtils.load(instream, Document.class);
				} finally {
					instream.close();
				}
				return new ResponseEntity<Document>(data, httpStatus);
			}
			return new ResponseEntity<Document>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (BusinessException | UnsupportedOperationException | IOException e) {
			throw new RestException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR,
					StringUtils.newStringUtf8("".getBytes()));
		}
	}

}
