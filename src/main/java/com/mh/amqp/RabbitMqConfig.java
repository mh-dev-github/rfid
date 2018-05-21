package com.mh.amqp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Clase de configuración RabbitMQ
 * 
 * @author arosorio@gmail.com
 *
 */
@Configuration
public class RabbitMqConfig {
	public static final String QUEUE_NAME = "rfid-queue";
	public static final boolean IS_DURABLE_QUEUE = false;

	public static final String EXCHANGE_NAME = "rfid-exchange";
	public static final String ROUTING_KEY = "rfid-sync";

	@Bean
	Queue queue() {
		return new Queue(QUEUE_NAME, IS_DURABLE_QUEUE);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange(EXCHANGE_NAME);
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		// @formatter:off
 		return BindingBuilder
				.bind(queue)
				.to(exchange)
				.with(ROUTING_KEY);
		// @formatter:on
	}

	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, Receiver.RECEIVE_METHOD_NAME);
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(QUEUE_NAME);
		container.setMessageListener(listenerAdapter);
		return container;
	}
}
