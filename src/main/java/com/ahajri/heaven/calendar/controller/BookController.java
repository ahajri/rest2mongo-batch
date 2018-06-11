package com.ahajri.heaven.calendar.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.codec.binary.StringUtils;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ahajri.heaven.calendar.collection.BookCollection;
import com.ahajri.heaven.calendar.queries.QueryParam;
import com.ahajri.heaven.calendar.security.exception.FunctionalException;
import com.ahajri.heaven.calendar.security.exception.RestException;
import com.ahajri.heaven.calendar.security.exception.TechnicalException;
import com.ahajri.heaven.calendar.service.BookService;
import com.google.gson.Gson;

@RestController
@RequestMapping("/api/v1/hcalendar/books")
public class BookController extends AController<BookCollection> {

	@Autowired
	private BookService bookService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@Override
	public ResponseEntity<BookCollection> create(@RequestBody BookCollection book) throws RestException {
		try {
			BookCollection persisted = bookService.create(book);
			return new ResponseEntity<BookCollection>(persisted, HttpStatus.CREATED);
		} catch (FunctionalException | TechnicalException e) {
			throw new RestException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR,
					StringUtils.newStringUtf8("".getBytes()));
		}

	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@Override
	public ResponseEntity<Void> update(@RequestBody BookCollection book) throws RestException {
		try {
			bookService.update(book);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (FunctionalException | TechnicalException e) {
			throw new RestException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR,
					StringUtils.newStringUtf8("".getBytes()));
		}
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@Override
	public ResponseEntity<Void> remove(@RequestBody BookCollection book) throws RestException {
		try {
			bookService.remove(book);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (FunctionalException | TechnicalException e) {
			throw new RestException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR,
					StringUtils.newStringUtf8("".getBytes()));
		}
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	@Override
	public ResponseEntity<List<BookCollection>> findAll() throws RestException {
		try {
			List<BookCollection> books = bookService.findAll();
			return new ResponseEntity<List<BookCollection>>(books, HttpStatus.FOUND);
		} catch (FunctionalException | TechnicalException e) {
			throw new RestException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR,
					StringUtils.newStringUtf8("".getBytes()));
		}
	}

	@RequestMapping(value = "/delete/all", method = RequestMethod.DELETE)
	@Override
	public ResponseEntity<Void> removeAll() throws RestException {
		try {
			bookService.removeAll(Arrays.asList(new BookCollection()));
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (FunctionalException | TechnicalException e) {
			throw new RestException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR,
					StringUtils.newStringUtf8("".getBytes()));
		}
	}

	@RequestMapping(value = "/delete/criteria", method = RequestMethod.GET)
	@Override
	public ResponseEntity<Long> deleteByCriteria(@NotNull @RequestParam(name = "query") String encryptedObjectQuery)
			throws RestException {
		try {
			String json = new String(Base64.getDecoder().decode(encryptedObjectQuery.getBytes()));
			Type listType = new com.google.gson.reflect.TypeToken<ArrayList<QueryParam>>() {
			}.getType();
			List<QueryParam> paramList = gson.fromJson(json, listType);
			long nbrDeleted = bookService.deleteByCriteria(paramList);
			return new ResponseEntity<Long>(nbrDeleted, HttpStatus.OK);
		} catch (FunctionalException | TechnicalException e) {
			throw new RestException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR,
					StringUtils.newStringUtf8("".getBytes()));
		}
	}

	@RequestMapping(value = "/find/criteria", method = RequestMethod.GET)
	@Override
	public ResponseEntity<List<BookCollection>> findByCriteria(
			@NotNull @RequestParam(name = "query") String encryptedObjectQuery) throws RestException {
		try {
			String json = new String(Base64.getDecoder().decode(encryptedObjectQuery.getBytes()));
			Type listType = new com.google.gson.reflect.TypeToken<ArrayList<QueryParam>>() {
			}.getType();
			List<QueryParam> paramList = gson.fromJson(json, listType);
			List<BookCollection> books = bookService.findByCriteria(paramList);
			return new ResponseEntity<List<BookCollection>>(books, HttpStatus.FOUND);
		} catch (TechnicalException e) {
			throw new RestException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR,
					StringUtils.newStringUtf8("".getBytes()));
		}
	}

	@RequestMapping(value = "/delete/list", method = RequestMethod.DELETE)
	@Override
	public ResponseEntity<Void> removeList(@RequestBody List<BookCollection> books) throws RestException {
		try {
			bookService.removeAll(books);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (FunctionalException | TechnicalException e) {
			throw new RestException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR,
					StringUtils.newStringUtf8("".getBytes()));
		}
	}

}
