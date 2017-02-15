package com.mh.amqp.handlers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mh.amqp.dto.RequestDTO;
import com.mh.api.correcciones.servicios.BaseCorreccionesService;
import com.mh.api.correcciones.servicios.EntradasProductoCorreccionesService;
import com.mh.api.sync.servicios.check.CheckService;
import com.mh.api.sync.servicios.check.EntradasProductoCheckService;
import com.mh.api.sync.servicios.pull.EntradasProductoPullService;
import com.mh.api.sync.servicios.pull.PullService;
import com.mh.api.sync.servicios.push.EntradasProductoPushService;
import com.mh.api.sync.servicios.push.PushService;
import com.mh.model.esb.domain.esb.IntegracionType;

@Component
public class EntradasProductoHandler extends BaseIntegrationTypeHandler {
	@Autowired
	protected RabbitTemplate rabbitTemplate;

	@Autowired
	private EntradasProductoPullService servicePull;

	@Autowired
	private EntradasProductoPushService servicePush;

	@Autowired
	private EntradasProductoCheckService serviceCheck;

	@Autowired
	private EntradasProductoCorreccionesService serviceCorrecciones;

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
		return IntegracionType.ENTRADAS_PT.equals(request.getIntegracionType());
	}
}
