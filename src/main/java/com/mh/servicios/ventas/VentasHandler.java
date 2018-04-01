package com.mh.servicios.ventas;

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
public class VentasHandler extends BaseIntegrationHandler {
	@Autowired
	protected RabbitTemplate rabbitTemplate;

	@Autowired
	private VentasPushService servicePush;

	@Autowired
	private VentasCheckService serviceCheck;

	@Autowired
	private VentasCorreccionesService serviceCorrecciones;

	@Override
	protected RabbitTemplate getRabbitTemplate() {
		return this.rabbitTemplate;
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
		return IntegracionType.VENTAS.equals(request.getIntegracionType());
	}

	@Override
	protected PullService<?> getServicePull() {
		// TODO Auto-generated method stub
		return null;
	}
}
