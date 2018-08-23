package com.ahajri.heaven.calendar.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 * @author ahajri
 *
 */
public final class JsonUtils {

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	public static boolean isJSONValid(String jsonInString) {
		try {
			gson.fromJson(jsonInString, Object.class);
			return true;
		} catch (com.google.gson.JsonSyntaxException ex) {
			return false;
		}
	}

	/**
	 * 
	 * @param json
	 * @param tClass
	 * @return
	 * @throws IOException
	 */
	public static  <T> List<T> jsonArrayToObjectList(String json, Class<T> tClass) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		List<T> ts = mapper.readValue(json, new TypeReference<List<T>>() {
		});
		return ts;
	}
	
	public static  <T> Set<T> jsonArrayToObjectSet(String json, Class<T> tClass) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		Set<T> ts = mapper.readValue(json, new TypeReference<Set<T>>() {
		});
		return ts;
	}
	/**
	 * 
	 * @param inputStream
	 * @param clazz
	 * @return
	 */
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
		return gson.toJson(obj);
	}
}
