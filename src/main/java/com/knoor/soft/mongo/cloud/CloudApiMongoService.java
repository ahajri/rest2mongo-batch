package com.knoor.soft.mongo.cloud;

import java.io.ByteArrayInputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.knoor.soft.exception.BusinessException;
import com.knoor.soft.utils.JsonUtils;

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
	public HttpResponse insertOne(final String collectionName, final Object documentMap)
			throws BusinessException {
		try {
			final String postUrl = "https://api.mlab.com/api/1/databases/" + dbName + "/collections/" + collectionName
					+ "?apiKey=" + apiKey;

			final HttpClient httpclient = HttpClients.createDefault();
			final HttpPost httppost = new HttpPost(postUrl);

			httppost.setHeader("content-type", "application/json");
			BasicHttpEntity basicEntity = new BasicHttpEntity();
			basicEntity.setContent(new ByteArrayInputStream(JsonUtils.prettyPrint(documentMap).getBytes()));
			httppost.setEntity(basicEntity);

			return httpclient.execute(httppost);

		} catch (Exception e) {
			throw new BusinessException(e, "Could not insert document");
		}
	}
}
