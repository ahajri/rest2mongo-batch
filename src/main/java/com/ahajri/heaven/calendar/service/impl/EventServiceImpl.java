//package com.ahajri.heaven.calendar.service.impl;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//
//import javax.validation.constraints.NotNull;
//
//import org.apache.commons.lang3.SerializationUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.stereotype.Service;
//
//import com.ahajri.heaven.calendar.collection.EventCollection;
//import com.ahajri.heaven.calendar.constants.enums.RecurringEnum;
//import com.ahajri.heaven.calendar.exception.BusinessException;
//import com.ahajri.heaven.calendar.queries.QueryParam;
//import com.ahajri.heaven.calendar.service.EventService;
//import com.ahajri.heaven.calendar.utils.HCDateUtils;
//
//@Service(value = "eventService")
//public class EventServiceImpl implements EventService {
//
//	@Autowired
//	private MongoTemplate mongoTemplate;
//
//	@Override
//	public List<EventCollection> findByDateBetween(@NotNull Date fromDate, @NotNull Date toDate)
//			throws BusinessException {
//		if (toDate.before(fromDate)) {
//			throw new BusinessException(new IllegalArgumentException("Please check the dates"));
//		}
//		Query q = new Query()
//				.addCriteria(Criteria.where("startDateTime").gte(fromDate).and("endDateTime").lte(fromDate));
//		return mongoTemplate.find(q, EventCollection.class);
//	}
//
//	@Override
//	public List<EventCollection> save(EventCollection event) throws BusinessException {
//		final String recurring = event.getRecurring();
//		Date endDate = event.getEndDateTime();
//		Date startDate = event.getStartDateTime();
//		Date stepDate = startDate;
//		List<EventCollection> events = new ArrayList<>();
//		
//		
//		if (RecurringEnum.ONETIME.getKey().equalsIgnoreCase(recurring) || StringUtils.isEmpty(recurring)) {
//			mongoTemplate.save(event);
//			return Arrays.asList(event);
//		} else {
//			switch (recurring) {
//			case "DAILY": {
//				while (stepDate.before(endDate)) {
//					EventCollection nextEvent = SerializationUtils.clone(event);
//					nextEvent.setStartDateTime(HCDateUtils.incrementDateByDays(startDate, 1));
//					stepDate = HCDateUtils.incrementDateByDays(stepDate, 1);
//					nextEvent.setStartDateTime(stepDate);
//					if (stepDate.after(endDate)) {
//						continue;
//					}
//					events.add(nextEvent);
//				}
//
//			}
//				break;
//			case "HOURLY":
//				while (stepDate.before(endDate)) {
//					EventCollection nextEvent = SerializationUtils.clone(event);
//					nextEvent.setStartDateTime(HCDateUtils.incrementDateByHours(startDate, 1));
//					stepDate = HCDateUtils.incrementDateByHours(stepDate, 1);
//					nextEvent.setStartDateTime(stepDate);
//					if (stepDate.after(endDate)) {
//						continue;
//					}
//					events.add(nextEvent);
//				}
//				break;
//			case "WEEKLY":
//				while (stepDate.before(endDate)) {
//					EventCollection nextEvent =  event;
//					nextEvent.setStartDateTime(HCDateUtils.incrementDateByWeeks(startDate, 1));
//					stepDate = HCDateUtils.incrementDateByWeeks(stepDate, 1);
//					nextEvent.setStartDateTime(stepDate);
//					if (stepDate.after(endDate)) {
//						continue;
//					}
//					events.add(nextEvent);
//				}
//				break;
//			case "MONTHLY":
//				while (stepDate.before(endDate)) {
//					EventCollection nextEvent = SerializationUtils.clone(event);
//					nextEvent.setStartDateTime(HCDateUtils.incrementDateByMonths(startDate, 1));
//					stepDate = HCDateUtils.incrementDateByMonths(stepDate, 1);
//					nextEvent.setStartDateTime(stepDate);
//					if (stepDate.after(endDate)) {
//						continue;
//					}
//					events.add(nextEvent);
//				}
//				break;
//			case "YEARLY":
//				while (stepDate.before(endDate)) {
//					EventCollection nextEvent = SerializationUtils.clone(event);
//					nextEvent.setStartDateTime(HCDateUtils.incrementDateByYears(startDate, 1));
//					stepDate = HCDateUtils.incrementDateByYears(stepDate, 1);
//					nextEvent.setStartDateTime(stepDate);
//					if (stepDate.before(endDate)) {
//						events.add(nextEvent);
//					}
//					
//				}
//				break;
//
//			default:
//				break;
//			}
//			mongoTemplate.insert(events,EventCollection.class);
//			return events;
//		}
//
//	}
//
//	@Override
//	public EventCollection findById(String id) throws BusinessException {
//		return mongoTemplate.findById(id, EventCollection.class);
//	}
//
//	@Override
//	public List<EventCollection> findByCriteria(QueryParam... qp) throws BusinessException {
//		final Query q = new Query();
//
//		final List<Criteria> criterias = new ArrayList<>();
//		
//		Arrays.asList(qp).stream().forEach(p -> {
//			String operator = p.getOperator().toString();
//			String fieldName = p.getFieldName();
//			Object value = p.getValue();
//			switch (operator) {
//			case "EQ":
//				Criteria cEq = Criteria.where(fieldName).is(value);
//				criterias.add(cEq);
//				break;
//			case "NE":
//				Criteria cNe = Criteria.where(fieldName).ne(value);
//				criterias.add(cNe);
//				break;
//			case "GT":
//				Criteria cGt = Criteria.where(fieldName).gt(value);
//				criterias.add(cGt);
//				break;
//			case "GTE":
//				Criteria cGte = Criteria.where(fieldName).gte(value);
//				criterias.add(cGte);
//				break;
//			case "LT":
//				Criteria cLt = Criteria.where(fieldName).lt(value);
//				criterias.add(cLt);
//				break;
//			case "LTE":
//				Criteria cLte = Criteria.where(fieldName).lte(value);
//				criterias.add(cLte);
//				break;
//			case "IN":
//				Criteria cIn = Criteria.where(fieldName).in(value);
//				criterias.add(cIn);
//				break;
//			case "NIN":
//				Criteria cNin = Criteria.where(fieldName).nin(value);
//				criterias.add(cNin);
//				break;
//			default:
//				break;
//			}
//
//		});
//		q.addCriteria(new Criteria().andOperator((Criteria[]) criterias.toArray()));
//		return mongoTemplate.find(q, EventCollection.class);
//	}
//
//	@Override
//	public List<EventCollection> findAll() throws BusinessException {
//		try {
//			return mongoTemplate.findAll(EventCollection.class);
//		} catch (Exception e) {
//			throw new BusinessException(e);
//		}
//	}
//
//	@Override
//	public void delete(EventCollection event) throws BusinessException {
//		try {
//			 mongoTemplate.remove(event);
//			
//		} catch (Exception e) {
//			throw new BusinessException(e);
//		}
//		
//	}
//
//	@Override
//	public void deleteByCriteria(QueryParam... qp) throws BusinessException {
//		final Query q = new Query();
//		final List<Criteria> criterias = new ArrayList<>();
//		Arrays.asList(qp).stream().forEach(p -> {
//			String operator = p.getOperator().toString();
//			String fieldName = p.getFieldName();
//			Object value = p.getValue();
//			switch (operator) {
//			case "EQ":
//				Criteria cEq = Criteria.where(fieldName).is(value);
//				criterias.add(cEq);
//				break;
//			case "NE":
//				Criteria cNe = Criteria.where(fieldName).ne(value);
//				criterias.add(cNe);
//				break;
//			case "GT":
//				Criteria cGt = Criteria.where(fieldName).gt(value);
//				criterias.add(cGt);
//				break;
//			case "GTE":
//				Criteria cGte = Criteria.where(fieldName).gte(value);
//				criterias.add(cGte);
//				break;
//			case "LT":
//				Criteria cLt = Criteria.where(fieldName).lt(value);
//				criterias.add(cLt);
//				break;
//			case "LTE":
//				Criteria cLte = Criteria.where(fieldName).lte(value);
//				criterias.add(cLte);
//				break;
//			case "IN":
//				Criteria cIn = Criteria.where(fieldName).in(value);
//				criterias.add(cIn);
//				break;
//			case "NIN":
//				Criteria cNin = Criteria.where(fieldName).nin(value);
//				criterias.add(cNin);
//				break;
//			default:
//				break;
//			}
//
//		});
//		q.addCriteria(new Criteria().andOperator((Criteria[]) criterias.toArray()));
//		try {
//			mongoTemplate.remove(q, EventCollection.class);
//		} catch (Exception e) {
//			throw new BusinessException(e);
//		}
//		
//	}
//
//}
