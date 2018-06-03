package com.ahajri.heaven.calendar.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.ahajri.heaven.calendar.collection.BookCollection;
import com.ahajri.heaven.calendar.collection.EventCollection;
import com.ahajri.heaven.calendar.queries.QueryParam;
import com.ahajri.heaven.calendar.security.exception.FunctionalException;
import com.ahajri.heaven.calendar.security.exception.TechnicalException;
import com.ahajri.heaven.calendar.service.BookService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Service("bookService")
public class BookServiceImpl implements BookService {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public BookCollection create(BookCollection toPersist) throws TechnicalException, FunctionalException {
		try {
			mongoTemplate.save(toPersist, BookCollection.class.getSimpleName());
			DBObject userDBObject = (DBObject) mongoTemplate.getConverter().convertToMongoType(toPersist);
			BasicQuery query = new BasicQuery("{ 'name' : '" + toPersist.getName() + "' }");
			return mongoTemplate.find(query, BookCollection.class).get(0);
		} catch (Exception e) {
			throw new TechnicalException(e);
		}
	}

	@Override
	public BookCollection update(BookCollection book) throws TechnicalException, FunctionalException {
		DBObject userDBObject = (DBObject) mongoTemplate.getConverter().convertToMongoType(book);
		BasicQuery query = new BasicQuery("{ 'bookId' : '" + book.getBookId() + "' }");
		Update setUpdate = Update.fromDBObject(new BasicDBObject("$set", userDBObject));
		mongoTemplate.updateFirst(query, setUpdate, BookCollection.class);
		return book;
	}

	@Override
	public BookCollection findById(String bookId) throws TechnicalException, FunctionalException {
		return mongoTemplate.findById(bookId, BookCollection.class);
	}

	@Override
	public void remove(BookCollection book) throws TechnicalException, FunctionalException {
		mongoTemplate.remove(book, BookCollection.class.getSimpleName());

	}

	@Override
	public List<BookCollection> findByCriteria(QueryParam... qp) throws TechnicalException {
		final Query q = new Query();

		final List<Criteria> criterias = new ArrayList<>();

		Arrays.asList(qp).stream().forEach(p -> {
			String operator = p.getOperator().toString();
			String fieldName = p.getFieldName();
			Object value = p.getValue();
			switch (operator) {
			case "EQ":
				Criteria cEq = Criteria.where(fieldName).is(value);
				criterias.add(cEq);
				break;
			case "NE":
				Criteria cNe = Criteria.where(fieldName).ne(value);
				criterias.add(cNe);
				break;
			case "GT":
				Criteria cGt = Criteria.where(fieldName).gt(value);
				criterias.add(cGt);
				break;
			case "GTE":
				Criteria cGte = Criteria.where(fieldName).gte(value);
				criterias.add(cGte);
				break;
			case "LT":
				Criteria cLt = Criteria.where(fieldName).lt(value);
				criterias.add(cLt);
				break;
			case "LTE":
				Criteria cLte = Criteria.where(fieldName).lte(value);
				criterias.add(cLte);
				break;
			case "IN":
				Criteria cIn = Criteria.where(fieldName).in(value);
				criterias.add(cIn);
				break;
			case "NIN":
				Criteria cNin = Criteria.where(fieldName).nin(value);
				criterias.add(cNin);
				break;
			default:
				break;
			}

		});
		q.addCriteria(new Criteria().andOperator((Criteria[]) criterias.toArray()));
		return mongoTemplate.find(q, BookCollection.class);
	}

	@Override
	public List<BookCollection> findAll() throws TechnicalException, FunctionalException {
		return mongoTemplate.findAll(BookCollection.class);
	}

	@Override
	public void deleteByCriteria(QueryParam... qp) throws TechnicalException, FunctionalException {

		final Query q = new Query();
		final List<Criteria> criterias = new ArrayList<>();
		Arrays.asList(qp).stream().forEach(p -> {
			String operator = p.getOperator().toString();
			String fieldName = p.getFieldName();
			Object value = p.getValue();
			switch (operator) {
			case "EQ":
				Criteria cEq = Criteria.where(fieldName).is(value);
				criterias.add(cEq);
				break;
			case "NE":
				Criteria cNe = Criteria.where(fieldName).ne(value);
				criterias.add(cNe);
				break;
			case "GT":
				Criteria cGt = Criteria.where(fieldName).gt(value);
				criterias.add(cGt);
				break;
			case "GTE":
				Criteria cGte = Criteria.where(fieldName).gte(value);
				criterias.add(cGte);
				break;
			case "LT":
				Criteria cLt = Criteria.where(fieldName).lt(value);
				criterias.add(cLt);
				break;
			case "LTE":
				Criteria cLte = Criteria.where(fieldName).lte(value);
				criterias.add(cLte);
				break;
			case "IN":
				Criteria cIn = Criteria.where(fieldName).in(value);
				criterias.add(cIn);
				break;
			case "NIN":
				Criteria cNin = Criteria.where(fieldName).nin(value);
				criterias.add(cNin);
				break;
			default:
				break;
			}

		});
		q.addCriteria(new Criteria().andOperator((Criteria[]) criterias.toArray()));
		try {
			mongoTemplate.remove(q, BookCollection.class);
		} catch (Exception e) {
			throw new TechnicalException(e);
		}

	}

	@Override
	public void removeAll(List<BookCollection> collections) throws TechnicalException, FunctionalException {

		try {
			Query query = new Query();
			for (BookCollection book : collections) {
				query.addCriteria(Criteria.byExample(book));
			}
			mongoTemplate.remove(query, BookCollection.class);
		} catch (Exception e) {
			throw new TechnicalException(e);
		}

	}

}
