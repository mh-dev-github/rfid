package com.mh.amqp.handlers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mh.amqp.RabbitMqConfig;
import com.mh.amqp.dto.RequestDTO;
import com.mh.amqp.dto.RequestType;
import com.mh.core.patterns.AbstractHandler;
import com.mh.model.esb.domain.esb.IntegracionType;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SyncAllHandler extends AbstractHandler<RequestDTO> {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Override
	protected boolean canHandleRequest(RequestDTO request) {
		return RequestType.SYNC_ALL.equals(request.getRequestType());
	}

	@Override
	protected void handleRequest(RequestDTO request) {
		log.info("Procesando {}", request);

		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.PULL, IntegracionType.LOCACIONES));
		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.PUSH, IntegracionType.LOCACIONES));
		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.CHECK, IntegracionType.LOCACIONES));

		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.PULL, IntegracionType.PRODUCTOS));
		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.PUSH, IntegracionType.PRODUCTOS));
		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.CHECK, IntegracionType.PRODUCTOS));

		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.PULL, IntegracionType.PEDIDOS));
		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.PUSH, IntegracionType.PEDIDOS));
		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.CHECK, IntegracionType.PEDIDOS));

		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.PULL, IntegracionType.SALIDAS_TIENDA));
		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.PUSH, IntegracionType.SALIDAS_TIENDA));
		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.CHECK, IntegracionType.SALIDAS_TIENDA));

		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.PULL, IntegracionType.ORDENES_DE_PRODUCCION));
		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.PUSH, IntegracionType.ORDENES_DE_PRODUCCION));
		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.CHECK, IntegracionType.ORDENES_DE_PRODUCCION));

		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.PULL, IntegracionType.ENTRADAS_PT));
		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.PUSH, IntegracionType.ENTRADAS_PT));
		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.CHECK, IntegracionType.ENTRADAS_PT));
	}
}
