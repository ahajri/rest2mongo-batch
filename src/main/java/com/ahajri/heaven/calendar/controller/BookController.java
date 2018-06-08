package com.ahajri.heaven.calendar.controller;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahajri.heaven.calendar.collection.BookCollection;
import com.ahajri.heaven.calendar.queries.QueryParam;
import com.ahajri.heaven.calendar.security.exception.FunctionalException;
import com.ahajri.heaven.calendar.security.exception.RestException;
import com.ahajri.heaven.calendar.security.exception.TechnicalException;
import com.ahajri.heaven.calendar.service.BookService;

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
			return new ResponseEntity<Void>( HttpStatus.OK);
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

	@RequestMapping(value = "/delete/criteria", method = RequestMethod.DELETE)
	@Override
	public ResponseEntity<Void> deleteByCriteria(QueryParam... qp) throws RestException {
		try {
			bookService.deleteByCriteria(qp);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (FunctionalException | TechnicalException e) {
			throw new RestException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR,
					StringUtils.newStringUtf8("".getBytes()));
		}
	}

	@RequestMapping(value = "/find/criteria", method = RequestMethod.GET)
	@Override
	public ResponseEntity<List<BookCollection>> findByCriteria(QueryParam... qp) throws RestException {
		try {
			List<BookCollection> books = bookService.findByCriteria(qp);
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
