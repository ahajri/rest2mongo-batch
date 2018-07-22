package com.ahajri.heaven.calendar.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author ahajri
 *
 */
public final class HttpUtils {

	/**
	 * 
	 * @param root
	 * @param params
	 * @return
	 * @throws URISyntaxException
	 * @throws MalformedURLException
	 */
	public static final String buildParamUrl(@NotNull String root, Map<String, Object> params)
			throws URISyntaxException, MalformedURLException {
		URIBuilder b = new URIBuilder(root);
		params.forEach((k, v) -> b.addParameter(k, String.valueOf(v)));
		return b.build().toURL().toString();
	}

	/**
	 * 
	 * @param response
	 * @param clazz
	 * @return  <T>
	 * @throws IOException
	 */
	public static <T> T retrieveResourceFromResponse(HttpResponse response, Class<T> clazz) throws IOException {

		String jsonFromResponse = EntityUtils.toString(response.getEntity());
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper.readValue(jsonFromResponse, clazz);
	}
	
	/**
	 * 
	 * @param url
	 * @param params
	 * @return {@link Map} of retrieved JSON resource
	 * @throws URISyntaxException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	@SuppressWarnings("unchecked")
	public static  Map<String, Object> doHttpGet(String url,Map<String, Object> params)
			throws URISyntaxException, MalformedURLException, IOException, ClientProtocolException {
		HttpUriRequest request = new HttpGet(buildParamUrl(url, params));

		HttpResponse response = HttpClientBuilder.create().build().execute(request);

		Map<String, Object> resource = retrieveResourceFromResponse(response, Map.class);
		return resource;
	}

	public static  HttpStatus statusCode2Status(int httpCode) {
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		switch (httpCode) {
		case 400:
			httpStatus=HttpStatus.BAD_REQUEST;
			break;
		case 401:
			httpStatus=HttpStatus.UNAUTHORIZED;
			break;
		case 403:
			httpStatus=HttpStatus.FORBIDDEN;
			break;
		case 404:
			httpStatus=HttpStatus.NOT_FOUND;
			break;
		case 405:
			httpStatus=HttpStatus.METHOD_NOT_ALLOWED;
			break;
		case 409:
			httpStatus=HttpStatus.CONFLICT;
			break;
		case 414:
			httpStatus=HttpStatus.URI_TOO_LONG;
			break;
		case 415:
			httpStatus=HttpStatus.UNSUPPORTED_MEDIA_TYPE;
			break;
		case 429:
			httpStatus=HttpStatus.TOO_MANY_REQUESTS;
			break;
		case 431:
			httpStatus=HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE;
			break;
		case 501:
			httpStatus=HttpStatus.NOT_IMPLEMENTED;
			break;
		case 502:
			httpStatus=HttpStatus.BAD_GATEWAY;
			break;
		case 503:
			httpStatus=HttpStatus.SERVICE_UNAVAILABLE;
			break;
		case 511:
			httpStatus=HttpStatus.NETWORK_AUTHENTICATION_REQUIRED;
			break;
		case 200:
			httpStatus=HttpStatus.OK;
			break;
		case 201:
			httpStatus=HttpStatus.CREATED;
			break;
		case 202:
			httpStatus=HttpStatus.ACCEPTED;
			break;
		case 204:
			httpStatus=HttpStatus.NO_CONTENT;
			break;
		case 207:
			httpStatus=HttpStatus.MULTI_STATUS;
			break;

		default:
			break;
		}
		return httpStatus;
	}

}
