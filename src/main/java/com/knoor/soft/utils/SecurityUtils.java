package com.knoor.soft.utils;

import java.security.SecureRandom;

public final class SecurityUtils {

	private static final SecureRandom random = new SecureRandom();

	/**
	 * generate random token
	 * 
	 * @return random token
	 */
	public static String generateRandomToken() {
		long longToken = Math.abs(random.nextLong());
		return Long.toString(longToken, 16);

	}

}
