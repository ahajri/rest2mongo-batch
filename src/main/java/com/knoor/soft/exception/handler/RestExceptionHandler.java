package com.knoor.soft.exception.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
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

import com.knoor.soft.exception.BusinessException;
import com.knoor.soft.exception.RestError;
import com.knoor.soft.exception.RestException;
import com.knoor.soft.utils.HttpUtils;

@EnableWebMvc
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { DuplicateKeyException.class, org.springframework.dao.DuplicateKeyException.class })
	protected ResponseEntity<Object> handleMongoUnicityConstraintException(RuntimeException ex, WebRequest request) {
		String bodyOfResponse = "some unique value already token";
		HttpHeaders headers = new HttpHeaders();
		request.getHeaderNames().forEachRemaining(s -> headers.add(s, request.getHeader(s)));
		return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.CONFLICT, request);
	}

	@ExceptionHandler(RestException.class)
	@ResponseBody
	public ResponseEntity<RestError> handleControllerException(HttpServletRequest req, RestException ex,
			HttpServletResponse resp) {
		Optional<Throwable> optionalCause = Optional.of(ex.getCause());
		HttpStatus httpStatus = HttpUtils.statusCode2Status(ex.getHttpStatus().value());
		String technicalMessage = StringUtils.EMPTY;
		String functionalMessage = StringUtils.EMPTY;
		if (optionalCause.isPresent()) {
			Throwable cause = optionalCause.get();
			if (cause instanceof BusinessException) {
				Throwable rootCause = ((BusinessException) cause).getCause();
				functionalMessage = ((BusinessException) cause).getFunctionalMessage();
				technicalMessage = rootCause.getMessage() != null ? rootCause.getMessage():functionalMessage;
				
				
				if (rootCause instanceof EmptyResultDataAccessException) {
					httpStatus = HttpStatus.NOT_FOUND;
				} else {
					httpStatus = ex.getHttpStatus();
				}
			} else if (cause instanceof IllegalArgumentException) {
				IllegalArgumentException argException = (IllegalArgumentException) cause;
				functionalMessage = ex.getMessage();
				technicalMessage = argException.getMessage();
				httpStatus = HttpStatus.BAD_REQUEST;
			} else {
				functionalMessage = ex.getMessage();
				technicalMessage = cause.getMessage();
			}
		} else {
			technicalMessage = "Unknown cause";
		}
		return response(httpStatus, technicalMessage, functionalMessage);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		Map<String, String> responseBody = new HashMap<>();
		responseBody.put("path", request.getContextPath());
		responseBody.put("message", "Service non disponible momentanément (404).");
		return new ResponseEntity<Object>(responseBody, HttpStatus.NOT_FOUND);
	}

	private ResponseEntity<RestError> response(HttpStatus status, String technicalMessage, String functionalMessage) {
		return new ResponseEntity<RestError>(new RestError(status.value(), technicalMessage, functionalMessage),
				status);
	}
}
