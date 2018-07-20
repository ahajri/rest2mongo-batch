//package com.ahajri.heaven.calendar.mongo.cloud;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.annotation.PostConstruct;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicNameValuePair;
//import org.bson.Document;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import com.ahajri.heaven.calendar.exception.BusinessException;
//import com.mongodb.MongoClient;
//import com.mongodb.MongoClientURI;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//
///**
// * 
// * @author ahajri
// *
// */
////@Service
//public class CloudMongoService {
//
//	private  String cloudMongUrl ;
//
//	private MongoDatabase db;
//
//	private MongoClient client;
//	
//	@Value("${mlab.api.key}")
//	protected String apiKey;
//
//	@Value("${mlab.db.user}")
//	protected String dbUser;
//	
//	@Value("${mlab.db.password}")
//	protected String dbPwd;
//	
//	@Value("${mlab.db.name}")
//	protected String dbName;
//
//	@Value("${mlab.db.port}")
//	protected String dbPort;
//	
//	@Value("${mlab.db.host}")
//	protected String dbHost;
//	
//	
//	public CloudMongoService() {
//	}
//	
//	@PostConstruct
//	public void init() {
//		cloudMongUrl = "mongodb://"+dbUser+":"+dbPwd+"@"+dbHost+":"+dbPort+"/"+dbName;
//		
//	}
//
//	public void begin() {
//		MongoClientURI uri = new MongoClientURI(cloudMongUrl);
//		client = new MongoClient(uri);
//		db = client.getDatabase(uri.getDatabase());
//	}
//
//	public void insertMany(String collectionName, List<Document> documents) {
//		MongoCollection<Document> collection = db.getCollection(collectionName);
//		collection.insertMany(documents);
//	}
//
//	/**
//	 * 
//	 * @param collectionName
//	 * @param document
//	 * @throws BusinessException
//	 */
//	public void insertOne(String collectionName, Document document) throws BusinessException {
//		try {
//			MongoCollection<Document> collection = db.getCollection(collectionName);
//			collection.insertOne(document);
//		} catch (Exception e) {
//			throw new BusinessException(e, "Could not insert document");
//		}
//	}
//	
//	
//	
//
//
//	public void deleteOne(String collectionName, Document document) {
//		MongoCollection<Document> collection = db.getCollection(collectionName);
//		collection.deleteOne(document);
//	}
//
//	public void close() {
//		if (client != null) {
//			client.close();
//		}
//	}
//
//}
