package com.mh.servicios.entradasProducto;

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
public class EntradasProductoHandler extends BaseIntegrationHandler {
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
	protected CorreccionesService<?, ?> getCorrecionesService() {
		return serviceCorrecciones;
	}

	@Override
	protected boolean canHandleRequest(RequestDTO request) {
		return IntegracionType.ENTRADAS_PT.equals(request.getIntegracionType());
	}
}
