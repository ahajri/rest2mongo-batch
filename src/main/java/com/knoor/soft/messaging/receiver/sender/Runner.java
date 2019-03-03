package com.knoor.soft.messaging.receiver.sender;
//package com.ahajri.heaven.calendar.messaging.receiver.sender;
//
//import java.util.concurrent.TimeUnit;
//
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import com.ahajri.heaven.calendar.config.MessagingConfig;
//import com.ahajri.heaven.calendar.messaging.receiver.MessageReceiver;
//
//@Component
//public class Runner implements CommandLineRunner {
//
//	private final RabbitTemplate rabbitTemplate;
//	private final MessageReceiver receiver;
//
//	public Runner(MessageReceiver receiver, RabbitTemplate rabbitTemplate) {
//		this.receiver = receiver;
//		this.rabbitTemplate = rabbitTemplate;
//	}
//
//	@Override
//	public void run(String... args) throws Exception {
//		System.out.println("Sending message...");
//		rabbitTemplate.convertAndSend(MessagingConfig.topicExchangeName, "com.ahajri.heaven.calendar", "Hello from RabbitMQ!");
//		receiver.getLatch().await(5000, TimeUnit.MILLISECONDS);
//	}
//
//}
