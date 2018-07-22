package com.ahajri.heaven.calendar.mongo.cloud;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ahajri.heaven.calendar.exception.BusinessException;
import com.google.gson.Gson;

@Service
public class CloudApiMongoService {

	@Value("${mlab.api.key}")
	protected String apiKey;

	@Value("${mlab.db.user}")
	protected String dbUser;

	@Value("${mlab.db.password}")
	protected String dbPwd;

	@Value("${mlab.db.name}")
	protected String dbName;

	@Value("${mlab.db.port}")
	protected String dbPort;

	@Value("${mlab.db.host}")
	protected String dbHost;

	/**
	 * insert One Document
	 * 
	 * @param collectionName
	 * 
	 * @param document
	 * 
	 * @throws BusinessException
	 */
	public HttpResponse insertOne(final String collectionName, final Map<String, Object> documentMap) throws BusinessException {
		try {
			final String postUrl = "https://api.mlab.com/api/1/databases/" + dbName + "/collections/" + collectionName
					+ "?apiKey=" + apiKey;
			
			final HttpClient httpclient = HttpClients.createDefault();
			final HttpPost httppost = new HttpPost(postUrl);

			
			httppost.setHeader("content-type", "application/json");
			BasicHttpEntity basicEntity = new BasicHttpEntity();
			basicEntity.setContent(new ByteArrayInputStream(new Gson().toJson(documentMap).getBytes()));
			httppost.setEntity(basicEntity);

			return httpclient.execute(httppost);

		} catch (Exception e) {
			throw new BusinessException(e, "Could not insert document");
		}
	}
}
