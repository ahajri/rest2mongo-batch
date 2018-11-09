package com.ahajri.heaven.calendar.messaging;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;

public class KafkaLab {

	public static void main(String[] args) {
		Map<String, List<PartitionInfo>> topics;

		Properties props = new Properties();
		props.put("bootstrap.servers",
				"pprod1-kafka.si2m.tec:9092,pprod2-kafka.si2m.tec:9092,pprod3-kafka.si2m.tec:9092");
		props.put("group.id", "test-consumer-group");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		topics = consumer.listTopics();

		topics.entrySet().stream().forEach(e -> {
			System.out.println(e.getKey() + "==========>");
			e.getValue().stream().forEach(p -> System.out.println(" ::topic =   " + p.topic()));
		});

		consumer.close();

	}

}
