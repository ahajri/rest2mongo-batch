package com.ahajri.heaven.calendar.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ahajri.heaven.calendar.collection.EventCollection;
import com.ahajri.heaven.calendar.queries.QueryParam;
import com.ahajri.heaven.calendar.security.exception.TechnicalException;
import com.ahajri.heaven.calendar.service.EventService;

@Service(value = "eventService")
public class EventServiceImpl implements EventService {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<EventCollection> findByDateBetween(@NotNull Date fromDate, @NotNull Date toDate)
			throws TechnicalException {
		if (toDate.before(fromDate)) {
			throw new TechnicalException(new IllegalArgumentException("Please check the dates"));
		}
		Query q = new Query()
				.addCriteria(Criteria.where("startDateTime").gte(fromDate).and("endDateTime").lte(fromDate));
		return mongoTemplate.find(q, EventCollection.class);
	}

	@Override
	public EventCollection save(EventCollection event) throws TechnicalException {
		mongoTemplate.save(event);
		return findById(event.getIdEvent());
	}

	@Override
	public EventCollection findById(String id) throws TechnicalException {
		return mongoTemplate.findById(id, EventCollection.class);
	}

	@Override
	public List<EventCollection> findByCriteria(QueryParam... qp) throws TechnicalException {
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
		return mongoTemplate.find(q, EventCollection.class);
	}

	@Override
	public List<EventCollection> findAll() throws TechnicalException {
		try {
			return mongoTemplate.findAll(EventCollection.class);
		} catch (Exception e) {
			throw new TechnicalException(e);
		}
	}

}
