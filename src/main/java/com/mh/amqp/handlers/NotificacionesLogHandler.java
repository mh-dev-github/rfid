package com.mh.amqp.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mh.amqp.dto.RequestDTO;
import com.mh.api.alertas.servicios.AlertasLogService;
import com.mh.core.patterns.AbstractHandler;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NotificacionesLogHandler extends AbstractHandler<RequestDTO> {
	@Autowired
	private AlertasLogService service;

	@Override
	protected boolean canHandleRequest(RequestDTO request) {
		switch (request.getRequestType()) {
		case ALERTAS_LOGS:
		case CONSOLIDADOS_LOGS:
			return true;
		default:
			return false;
		}
	}

	@Override
	protected void handleRequest(RequestDTO request) {
		log.info("Procesando {}", request);
		switch (request.getRequestType()) {
		case ALERTAS_LOGS:
			service.generarReporteAlertas();
			break;
		case CONSOLIDADOS_LOGS:
			service.generarReporteConsolidados();
			break;
		default:
			break;
		}
	}
}
