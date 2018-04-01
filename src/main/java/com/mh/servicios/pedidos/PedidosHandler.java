package com.mh.servicios.pedidos;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.mh.amqp.RabbitMqConfig;
import com.mh.dto.amqp.RequestDTO;
import com.mh.dto.amqp.RequestType;
import com.mh.model.esb.domain.esb.IntegracionType;
import com.mh.services.CorreccionesService;
import com.mh.services.BaseIntegrationHandler;
import com.mh.services.CheckService;
import com.mh.services.PullService;
import com.mh.services.PushService;

@Component
public class PedidosHandler extends BaseIntegrationHandler {
	@Autowired
	protected RabbitTemplate rabbitTemplate;

	@Autowired
	private PedidosPullService servicePull;

	@Autowired
	@Qualifier("create")
	private PedidosPushService servicePush;

	@Autowired
	@Qualifier("update")
	private PedidosUpdatePushService serviceUpdatePush;
	
	@Autowired
	private PedidosCheckService serviceCheck;

	@Autowired
	private PedidosCorreccionesService serviceCorrecciones;

	@Override
	protected RabbitTemplate getRabbitTemplate() {
		return this.rabbitTemplate;
	}

	@Override
	protected PullService<?> getServicePull() {
		return this.servicePull;
	}

	@Override
	protected PushService<?, ?, ?> getServicePush() {
		return this.servicePush;
	}

	@Override
	protected CheckService<?, ?, ?> getServiceCheck() {
		return serviceCheck;
	}
	
	@Override
	protected CorreccionesService<?, ?> getCorrecionesService() {
		return serviceCorrecciones;
	}


	@Override
	protected boolean canHandleRequest(RequestDTO request) {
		return IntegracionType.PEDIDOS.equals(request.getIntegracionType());
	}

	@Override
	protected void handleRequest(RequestDTO request) {
		switch (request.getRequestType()) {
		case PUSH_UPDATE:
			if (serviceUpdatePush.push(request)) {
				rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY, request);
				rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY, new RequestDTO(RequestType.CHECK));
			}
			break;
		default:
			super.handleRequest(request);
			break;
		}
	}
}

