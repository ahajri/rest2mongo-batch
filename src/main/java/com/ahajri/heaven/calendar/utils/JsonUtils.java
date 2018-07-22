package com.ahajri.heaven.calendar.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 * @author ahajri
 *
 */
public final class JsonUtils {

	public static <T> T load(final InputStream inputStream, final Class<T> clazz) {
		try {
			if (inputStream != null) {
				final Gson gson = new Gson();
				final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				return gson.fromJson(reader, clazz);
			}
		} catch (final Exception e) {
		}
		return null;
	}
	
	public static String prettyPrint(Object obj) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(obj);
	}
}
