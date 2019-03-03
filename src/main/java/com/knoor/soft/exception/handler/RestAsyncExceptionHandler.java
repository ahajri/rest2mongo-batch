package com.knoor.soft.exception.handler;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

public class RestAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(RestAsyncExceptionHandler.class);

	@Override
	public void handleUncaughtException(Throwable ex, Method method, Object... params) {
		LOG.error("Exception message - " + ex.getMessage());
		LOG.error("Method name - " + method.getName());
		for (Object param : params) {
			LOG.error("Parameter value - " + param);
		}

	}

}
