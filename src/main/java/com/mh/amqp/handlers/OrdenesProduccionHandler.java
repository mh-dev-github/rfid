package com.mh.amqp.handlers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mh.amqp.dto.RequestDTO;
import com.mh.api.correcciones.servicios.BaseCorreccionesService;
import com.mh.api.correcciones.servicios.OrdenesProduccionCorreccionesService;
import com.mh.api.sync.servicios.check.CheckService;
import com.mh.api.sync.servicios.check.OrdenesProduccionCheckService;
import com.mh.api.sync.servicios.pull.OrdenesProduccionPullService;
import com.mh.api.sync.servicios.pull.PullService;
import com.mh.api.sync.servicios.push.OrdenesProduccionPushService;
import com.mh.api.sync.servicios.push.PushService;
import com.mh.model.esb.domain.esb.IntegracionType;

@Component
public class OrdenesProduccionHandler extends BaseIntegrationTypeHandler {
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
	protected BaseCorreccionesService<?, ?> getCorrecionesService() {
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
