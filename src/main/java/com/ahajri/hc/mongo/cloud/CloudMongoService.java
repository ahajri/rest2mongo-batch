package com.ahajri.hc.mongo.cloud;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ahajri.hc.enums.ErrorMessageEnum;
import com.ahajri.hc.exception.BusinessException;
import com.ahajri.hc.queries.QueryParam;
import com.ahajri.hc.utils.JsonUtils;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndReplaceOptions;

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
	private void begin() {
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
	 * @throws BusinessException
	 */
	public void replaceOne(String collectionName, Document document) throws BusinessException {
		try {
			begin();
			MongoCollection<Document> collection = db.getCollection(collectionName);
			collection.findOneAndReplace(new Document("email", document.get("email")), document,
					new FindOneAndReplaceOptions().upsert(true));
			close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e, ErrorMessageEnum.USER_UPADATE_KO.getMessage());
		}
	}

	public void updateOne(String collectionName, Document updateQuery, Document updated) throws BusinessException {
		try {
			begin();
			MongoCollection<Document> collection = db.getCollection(collectionName);
			collection.updateOne(updateQuery, new Document("$set", updated));
			close();
		} catch (Exception e) {
			throw new BusinessException(e, ErrorMessageEnum.USER_UPADATE_KO.getMessage());
		}
	}

	/**
	 * 
	 * @param collectionName
	 * @param example
	 * @return
	 * @throws BusinessException
	 */
	public List<Document> findByExample(String collectionName, Document example) throws BusinessException {
		begin();
		MongoCollection<Document> collection = db.getCollection(collectionName);
		FindIterable<Document> documentsFI = collection.find(example);
		List<Document> documents = new ArrayList<>();
		documentsFI.iterator().forEachRemaining(c -> {
			documents.add(c);
		});
		close();
		return documents;
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
			throw new BusinessException(e,
					ErrorMessageEnum.DELETE_DOCUMENT_KO.getMessage(JsonUtils.prettyPrint(document)));
		}
	}

	/**
	 * 
	 * Search document
	 * 
	 * @param collectionName:
	 *            Collection name
	 * @param qp
	 *            : Query Parameters
	 * @return list of found documents
	 * @throws BusinessException
	 */
	public List<Document> search(String collectionName, QueryParam... qp) throws BusinessException {
		try {
			begin();
			MongoCollection<Document> collection = db.getCollection(collectionName);
			List<Document> result = new ArrayList<>();
			Document query = new Document();
			Arrays.asList(qp).stream().forEach(p -> {
				String operator = p.getOperator();
				String fieldName = p.getFieldName();
				Object value = p.getValue();
				switch (operator) {
				case "EQ":
					query.append(fieldName, value);
					break;
				case "NE":
					query.append(fieldName, new Document().append("$ne", value));
					break;
				case "GT":
					query.append(fieldName, new Document().append("$gt", value));
					break;
				case "GTE":
					query.append(fieldName, new Document().append("$gte", value));
					break;
				case "LT":
					query.append(fieldName, new Document().append("$lt", value));
					break;
				case "LTE":
					query.append(fieldName, new Document().append("$lte", value));
					break;
				case "IN":
					query.append(fieldName, new Document().append("$in", value));
					break;
				case "NIN":
					query.append(fieldName, new Document().append("$nin", value));
					break;
				default:
					break;
				}

			});

			FindIterable<Document> iterable = collection.find(query);
			if (iterable.first() == null) {
				throw new Exception(ErrorMessageEnum.FIND_DOCUMENT_KO.getMessage());
			}
			for (Document document : iterable) {
				result.add(document);
			}

			close();
			return result;

		} catch (Exception e) {
			throw new BusinessException(e, ErrorMessageEnum.FIND_DOCUMENT_KO.getMessage());
		}
	}

	/**
	 * 
	 */
	private void close() {
		if (client != null) {
			client.close();
		}
	}

}
