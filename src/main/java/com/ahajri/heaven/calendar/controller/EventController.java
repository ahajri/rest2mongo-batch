package com.ahajri.heaven.calendar.controller;

import java.rmi.UnexpectedException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ahajri.heaven.calendar.collection.EventCollection;
import com.ahajri.heaven.calendar.collection.UserAuth;
import com.ahajri.heaven.calendar.security.exception.FunctionalException;
import com.ahajri.heaven.calendar.security.exception.RestException;
import com.ahajri.heaven.calendar.security.exception.TechnicalException;
import com.ahajri.heaven.calendar.service.EventService;

@RestController
@RequestMapping("/events")
public class EventController {

	private static final String REQUEST_PARAM_DATE_FORMAT = "yyyyMMddHHmmss";

	@Autowired
	private EventService eventService;

	@RequestMapping(value = "/byDates", method = RequestMethod.GET)
	public ResponseEntity<List<EventCollection>> findByDateBetween(
			@NotNull @RequestParam(value = "fromDate") @DateTimeFormat(pattern = REQUEST_PARAM_DATE_FORMAT) Date fromDate,
			@RequestParam(value = "toDate") @DateTimeFormat(pattern = REQUEST_PARAM_DATE_FORMAT) Date toDate)
			throws RestException {

		try {
			List<EventCollection> found = eventService.findByDateBetween(fromDate, toDate);
			if (found == null) {
				throw new RestException("No Event Found", new FunctionalException("User Not Found"),
						HttpStatus.NOT_FOUND, StringUtils.newStringUtf8("".getBytes()));
			}
			return new ResponseEntity<List<EventCollection>>(found, HttpStatus.FOUND);
		} catch (TechnicalException e) {
			throw new RestException(e.getMessage(), e, HttpStatus.CONFLICT, StringUtils.newStringUtf8("".getBytes()));
		}

	}

}
