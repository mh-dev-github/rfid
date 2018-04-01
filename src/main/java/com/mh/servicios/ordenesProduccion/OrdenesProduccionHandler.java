package com.mh.servicios.ordenesProduccion;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mh.dto.amqp.RequestDTO;
import com.mh.model.esb.domain.esb.IntegracionType;
import com.mh.services.CorreccionesService;
import com.mh.services.BaseIntegrationHandler;
import com.mh.services.CheckService;
import com.mh.services.PullService;
import com.mh.services.PushService;

@Component
public class OrdenesProduccionHandler extends BaseIntegrationHandler {
	@Autowired
	protected RabbitTemplate rabbitTemplate;

	@Autowired
	private OrdenesProduccionPullService servicePull;

	@Autowired
	private OrdenesProduccionPushService servicePush;

	@Autowired
	private OrdenesProduccionCheckService serviceCheck;

	@Autowired
	private OrdenesProduccionCorreccionesService serviceCorrecciones;

	@Override
	protected RabbitTemplate getRabbitTemplate() {
		return this.rabbitTemplate;
	}

	@Override
	protected PullService<?> getServicePull() {
		return this.servicePull;
	}
	
	@Override
	protected CorreccionesService<?, ?> getCorrecionesService() {
		return serviceCorrecciones;
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
	protected boolean canHandleRequest(RequestDTO request) {
		return IntegracionType.ORDENES_DE_PRODUCCION.equals(request.getIntegracionType());
	}
}
