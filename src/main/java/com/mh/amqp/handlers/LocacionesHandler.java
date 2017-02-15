package com.mh.amqp.handlers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mh.amqp.dto.RequestDTO;
import com.mh.api.correcciones.servicios.BaseCorreccionesService;
import com.mh.api.correcciones.servicios.LocacionesCorreccionesService;
import com.mh.api.sync.servicios.check.CheckService;
import com.mh.api.sync.servicios.check.LocacionesCheckService;
import com.mh.api.sync.servicios.pull.LocacionesPullService;
import com.mh.api.sync.servicios.pull.PullService;
import com.mh.api.sync.servicios.push.LocacionesPushService;
import com.mh.api.sync.servicios.push.PushService;
import com.mh.model.esb.domain.esb.IntegracionType;

@Component
public class LocacionesHandler extends BaseIntegrationTypeHandler {
	@Autowired
	protected RabbitTemplate rabbitTemplate;

	@Autowired
	private LocacionesPullService servicePull;

	@Autowired
	private LocacionesPushService servicePush;

	@Autowired
	private LocacionesCheckService serviceCheck;

	@Autowired
	private LocacionesCorreccionesService serviceCorrecciones;

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
		return IntegracionType.LOCACIONES.equals(request.getIntegracionType());
	}
}
