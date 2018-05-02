package com.ahajri.heaven.calendar.security.exception.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ahajri.heaven.calendar.security.exception.RestError;
import com.ahajri.heaven.calendar.security.exception.RestException;
import com.mongodb.DuplicateKeyException;

@EnableWebMvc
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { DuplicateKeyException.class, org.springframework.dao.DuplicateKeyException.class })
	protected ResponseEntity<Object> handleMongoUnicityConstraintException(RuntimeException ex, WebRequest request) {
		String bodyOfResponse = "some unique value already tocken";
		HttpHeaders headers = new HttpHeaders();
		request.getHeaderNames().forEachRemaining(s -> headers.add(s, request.getHeader(s)));
		return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.CONFLICT, request);
	}

	@ExceptionHandler(RestException.class)
	@ResponseBody
	public ResponseEntity<RestError> handleControllerException(HttpServletRequest req, RestException ex,
			HttpServletResponse resp) {
		Optional<Throwable> optionalCause = Optional.of(ex.getCause());
		return response(ex.getHttpStatus(), ex.getCode(), ex.getMessage(), optionalCause);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		Map<String, String> responseBody = new HashMap<>();
		responseBody.put("path", request.getContextPath());
		responseBody.put("message", "Service non disponible momentanément (404).");
		return new ResponseEntity<Object>(responseBody, HttpStatus.NOT_FOUND);
	}

	private ResponseEntity<RestError> response(HttpStatus status, String code, String msg,
			Optional<Throwable> optionalCause) {
		return new ResponseEntity<RestError>(new RestError(status.value(), code, msg, optionalCause), status);
	}
}
