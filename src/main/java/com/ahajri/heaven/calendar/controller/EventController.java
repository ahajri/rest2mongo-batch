//package com.ahajri.heaven.calendar.controller;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Date;
//import java.util.List;
//
//import javax.validation.constraints.NotNull;
//
//import org.apache.commons.codec.binary.StringUtils;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.bson.Document;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.ahajri.heaven.calendar.collection.EventCollection;
//import com.ahajri.heaven.calendar.exception.BusinessException;
//import com.ahajri.heaven.calendar.exception.RestException;
//import com.ahajri.heaven.calendar.mongo.cloud.CloudApiMongoService;
//import com.ahajri.heaven.calendar.service.EventService;
//import com.ahajri.heaven.calendar.utils.JsonUtils;
//
//@RestController
//@RequestMapping("/api/v1/hcalendar/events")
//public class EventController {
//
//	private static final String REQUEST_PARAM_DATE_FORMAT = "yyyyMMddHHmmss";
//
//	@Autowired
//	private EventService eventService;
//
//	@Autowired
//	private CloudApiMongoService cloudApiMongoService ;
//
//	/**
//	 * 
//	 * @param fromDate
//	 * @param toDate
//	 * @return
//	 * @throws RestException
//	 */
//	@RequestMapping(value = "/byDates", method = RequestMethod.GET)
//	public ResponseEntity<List<EventCollection>> findByDateBetween(
//			@NotNull @RequestParam(value = "fromDate") @DateTimeFormat(pattern = REQUEST_PARAM_DATE_FORMAT) Date fromDate,
//			@RequestParam(value = "toDate") @DateTimeFormat(pattern = REQUEST_PARAM_DATE_FORMAT) Date toDate)
//			throws RestException {
//
//		try {
//			List<EventCollection> found = eventService.findByDateBetween(fromDate, toDate);
//			if (found == null) {
//				throw new RestException("No Event Found", new BusinessException("User Not Found"), HttpStatus.NOT_FOUND,
//						StringUtils.newStringUtf8("".getBytes()));
//			}
//			return new ResponseEntity<List<EventCollection>>(found, HttpStatus.FOUND);
//		} catch (BusinessException e) {
//			throw new RestException(e.getMessage(), e, HttpStatus.NOT_FOUND, StringUtils.newStringUtf8("".getBytes()));
//		}
//
//	}
//
//	@RequestMapping(value = "/create", method = RequestMethod.POST)
//	public ResponseEntity<List<EventCollection>> create(@RequestBody EventCollection event) throws RestException {
//		try {
//			List<EventCollection> persisteds = eventService.save(event);
//			return new ResponseEntity<List<EventCollection>>(persisteds, HttpStatus.CREATED);
//		} catch (BusinessException e) {
//			throw new RestException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR,
//					StringUtils.newStringUtf8("".getBytes()));
//		}
//	}
//
//	@RequestMapping(value = "/cloud/create", method = RequestMethod.POST)
//	public ResponseEntity<Document> cloudCreate(@RequestBody Document event) throws RestException {
//		try {
//			HttpResponse response = cloudApiMongoService.insertOne("event", event);
//			HttpEntity entity = response.getEntity();
//
//			if (entity != null) {
//				InputStream instream = entity.getContent();
//				Document persisted =null;
//				try {
//					 persisted = JsonUtils.load(instream, Document.class);
//				} finally {
//					instream.close();
//				}
//				return new ResponseEntity<Document>(persisted, HttpStatus.CREATED);
//			}
//			return new ResponseEntity<Document>( HttpStatus.INTERNAL_SERVER_ERROR);
//		} catch (BusinessException | UnsupportedOperationException | IOException e) {
//			throw new RestException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR,
//					StringUtils.newStringUtf8("".getBytes()));
//		}
//	}
//
//	@RequestMapping(value = "/all", method = RequestMethod.GET)
//	public ResponseEntity<List<EventCollection>> findAll() throws RestException {
//		try {
//			return new ResponseEntity<List<EventCollection>>(eventService.findAll(), HttpStatus.FOUND);
//		} catch (BusinessException e) {
//			throw new RestException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR,
//					StringUtils.newStringUtf8("".getBytes()));
//		}
//	}
//}
