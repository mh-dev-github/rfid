package com.mh.tasks;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mh.amqp.handlers.AbstractHandler;
import com.mh.dto.amqp.RequestDTO;
import static com.mh.dto.amqp.RequestType.*;
import static com.mh.model.esb.domain.esb.IntegracionType.*;

import lombok.extern.slf4j.Slf4j;

import static com.mh.amqp.RabbitMqConfig.EXCHANGE_NAME;
import static com.mh.amqp.RabbitMqConfig.ROUTING_KEY;

/**
 * Componente encargado de colocar en cola la petición de ejecutar por demanda todos los flujos
 * 
 * @author arosorio@gmail.com
 *
 */
@Component
@Slf4j
public class SyncAllHandler extends AbstractHandler<RequestDTO> {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Override
	protected boolean canHandleRequest(RequestDTO request) {
		return SYNC_ALL.equals(request.getRequestType());
	}

	@Override
	protected void handleRequest(RequestDTO request) {
		log.info("Procesando {}", request);

		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, new RequestDTO(PULL, LOCACIONES));
		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, new RequestDTO(PUSH, LOCACIONES));
		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, new RequestDTO(CHECK, LOCACIONES));

		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, new RequestDTO(PULL, PRODUCTOS));
		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, new RequestDTO(PUSH, PRODUCTOS));
		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, new RequestDTO(CHECK, PRODUCTOS));

		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, new RequestDTO(PULL, PEDIDOS));
		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, new RequestDTO(PUSH, PEDIDOS));
		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, new RequestDTO(CHECK, PEDIDOS));

		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, new RequestDTO(PULL, SALIDAS_TIENDA));
		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, new RequestDTO(PUSH, SALIDAS_TIENDA));
		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, new RequestDTO(CHECK, SALIDAS_TIENDA));

		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, new RequestDTO(PULL, ORDENES_DE_PRODUCCION));
		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, new RequestDTO(PUSH, ORDENES_DE_PRODUCCION));
		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, new RequestDTO(CHECK, ORDENES_DE_PRODUCCION));

		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, new RequestDTO(PULL, ENTRADAS_PT));
		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, new RequestDTO(PUSH, ENTRADAS_PT));
		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, new RequestDTO(CHECK, ENTRADAS_PT));
	}
}
