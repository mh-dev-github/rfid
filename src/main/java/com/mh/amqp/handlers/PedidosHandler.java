package com.mh.amqp.handlers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.mh.amqp.RabbitMqConfig;
import com.mh.amqp.dto.RequestDTO;
import com.mh.amqp.dto.RequestType;
import com.mh.api.correcciones.servicios.BaseCorreccionesService;
import com.mh.api.correcciones.servicios.PedidosCorreccionesService;
import com.mh.api.sync.servicios.check.CheckService;
import com.mh.api.sync.servicios.check.PedidosCheckService;
import com.mh.api.sync.servicios.pull.PedidosPullService;
import com.mh.api.sync.servicios.pull.PullService;
import com.mh.api.sync.servicios.push.PedidosPushService;
import com.mh.api.sync.servicios.push.PedidosUpdatePushService;
import com.mh.api.sync.servicios.push.PushService;
import com.mh.model.esb.domain.esb.IntegracionType;

@Component
public class PedidosHandler extends BaseIntegrationTypeHandler {
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
	protected BaseCorreccionesService<?, ?> getCorrecionesService() {
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

