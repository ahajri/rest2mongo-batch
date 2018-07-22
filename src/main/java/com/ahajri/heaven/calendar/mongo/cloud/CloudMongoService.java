package com.ahajri.heaven.calendar.mongo.cloud;

import java.util.List;

import javax.annotation.PostConstruct;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ahajri.heaven.calendar.exception.BusinessException;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * @author
 *         <p>
 *         ahajri
 *         </p>
 */
@Service
public class CloudMongoService {

	private String cloudMongUrl;

	private MongoDatabase db;

	private MongoClient client;

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

	public CloudMongoService() {
	}

	@PostConstruct
	public void init() {
		cloudMongUrl = "mongodb://" + dbUser + ":" + dbPwd + "@" + dbHost + ":" + dbPort + "/" + dbName;
	}

	/**
	 * 
	 */
	public void begin() {
		MongoClientURI uri = new MongoClientURI(cloudMongUrl);
		client = new MongoClient(uri);
		db = client.getDatabase(uri.getDatabase());
	}

	/**
	 * 
	 * @param collectionName
	 * @param documents
	 */
	public void insertMany(String collectionName, List<Document> documents) throws BusinessException {
		try {
			begin();
			MongoCollection<Document> collection = db.getCollection(collectionName);
			collection.insertMany(documents);
			close();
		} catch (Exception e) {
			throw new BusinessException(e, "Could not insert documents");
		}
	}

	/**
	 * 
	 * @param collectionName
	 * @param document
	 * @throws BusinessException
	 */
	public void insertOne(String collectionName, Document document) throws BusinessException {
		try {
			begin();
			MongoCollection<Document> collection = db.getCollection(collectionName);
			collection.insertOne(document);
			close();
		} catch (Exception e) {
			throw new BusinessException(e, "Could not insert document");
		}
	}

	/**
	 * 
	 * @param collectionName
	 * @param document
	 */
	public void deleteOne(String collectionName, Document document) throws BusinessException {
		try {
			begin();
			MongoCollection<Document> collection = db.getCollection(collectionName);
			collection.deleteOne(document);
			close();
		} catch (Exception e) {
			throw new BusinessException(e, "Could not delete document");
		}
	}

	/**
	 * 
	 */
	public void close() {
		if (client != null) {
			client.close();
		}
	}

}
