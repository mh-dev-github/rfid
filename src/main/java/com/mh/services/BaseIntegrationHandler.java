package com.mh.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.mh.amqp.RabbitMqConfig;
import com.mh.amqp.handlers.AbstractHandler;
import com.mh.dto.amqp.RequestDTO;
import com.mh.dto.amqp.RequestType;
import com.mh.model.esb.domain.msg.MessageType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseIntegrationHandler extends AbstractHandler<RequestDTO> {
	abstract protected RabbitTemplate getRabbitTemplate();

	abstract protected PullService<?> getServicePull();

	abstract protected PushService<?, ?, ?> getServicePush();

	abstract protected CheckService<?, ?, ?> getServiceCheck();

	abstract protected CorreccionesService<?, ?> getCorrecionesService();

	public BaseIntegrationHandler() {
		super();
	}

	@Override
	protected void handleRequest(RequestDTO request) {
		log.info("Procesando {}", request);

		switch (request.getRequestType()) {
		case PULL:
			pull();
			break;
		case PUSH:
			push(request);
			break;
		case CHECK:
			check(request);
			break;
		case CORRECCION_REINTENTO:
			reTry(request);
			break;
		case CORRECCION_CREATE:
			reCreate(request);
			break;
		case CORRECCION_UPDATE:
			reUpdate(request);
			break;
		default:
			break;
		}
	}

	private void reUpdate(RequestDTO request) {
		getCorrecionesService().update(request.getIntegracionType(), MessageType.U, request.getExternalId());
	}

	private void reCreate(RequestDTO request) {
		getCorrecionesService().create(request.getIntegracionType(), MessageType.C, request.getExternalId());
	}

	private void reTry(RequestDTO request) {
		getCorrecionesService().retry(request.getIntegracionType(), MessageType.R, request.getExternalId());
	}

	private void check(RequestDTO request) {
		if (getServiceCheck().check(request)) {
			getRabbitTemplate().convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY, request);
		}
	}

	private void push(RequestDTO request) {
		if (getServicePush().push(request)) {
			getRabbitTemplate().convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY, request);
			getRabbitTemplate().convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
					new RequestDTO(RequestType.CHECK, request.getIntegracionType()));
		}
	}

	private void pull() {
		getServicePull().pull();
	}
}