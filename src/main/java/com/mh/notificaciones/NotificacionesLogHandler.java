package com.mh.notificaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mh.amqp.handlers.AbstractHandler;
import com.mh.dto.amqp.RequestDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementa el patron Decorator. Para facilitar la inclusión de nuevas notificaciones esta clase extiende la funcionalidad de la clase AbstractHandler
 * Recibe un DTO RequestDTO el cual encapsula la informacion de la notificación.
 * 
 * Las clases especializadas determinaran si le compete o no procesar el objeto RequestDTO. 
 * Si debe procesar la petición, esta clase identifica el tipo de operación solicitada y de acuerdo aello invoca el servicio correspondiente.
 * Es responsabilidad de las clases especilizadas instanciar los servicios adecuados.  
 * 
 * @author arosorio@gmail.com
 *
 * @see <a href="https://sourcemaking.com/design_patterns/decorator">Decorator Design Pattern</a>
 */

@Component
@Slf4j
public class NotificacionesLogHandler extends AbstractHandler<RequestDTO> {
	@Autowired
	private NotificacionesLogService service;

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
