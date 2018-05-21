package com.ahajri.heaven.calendar.mongo.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.ahajri.heaven.calendar.collection.EventCollection;

public interface EventRepository extends MongoRepository<EventCollection, String> {

}
