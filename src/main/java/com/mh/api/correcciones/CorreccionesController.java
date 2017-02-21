package com.mh.api.correcciones;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mh.amqp.RabbitMqConfig;
import com.mh.amqp.dto.RequestDTO;
import com.mh.amqp.dto.RequestType;
import com.mh.api.correcciones.dto.CorreccionCheckResponseDTO;
import com.mh.api.correcciones.servicios.BaseCorreccionesService;
import com.mh.api.correcciones.servicios.EntradasProductoCorreccionesService;
import com.mh.api.correcciones.servicios.LocacionesCorreccionesService;
import com.mh.api.correcciones.servicios.OrdenesProduccionCorreccionesService;
import com.mh.api.correcciones.servicios.PedidosCorreccionesService;
import com.mh.api.correcciones.servicios.ProductosCorreccionesService;
import com.mh.api.correcciones.servicios.SalidasTiendaCorreccionesService;
import com.mh.model.esb.domain.esb.BaseEntity;
import com.mh.model.esb.domain.esb.IntegracionType;
import com.mh.model.esb.domain.msg.BaseMessageEntity;
import com.mh.model.esb.domain.msg.MessageType;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/correcciones")
@CrossOrigin
@Slf4j
public class CorreccionesController {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	PedidosCorreccionesService pedidosService;

	@Autowired
	SalidasTiendaCorreccionesService salidasTiendasService;

	@Autowired
	OrdenesProduccionCorreccionesService ordenesProduccionService;

	@Autowired
	EntradasProductoCorreccionesService entradasProductosService;

	@Autowired
	ProductosCorreccionesService productosService;

	@Autowired
	LocacionesCorreccionesService locacionesService;
	
	private BaseCorreccionesService<? extends BaseEntity, ? extends BaseMessageEntity> getService(IntegracionType tipoIntegracion) {
		BaseCorreccionesService<? extends BaseEntity, ? extends BaseMessageEntity> result;

		switch (tipoIntegracion) {
		case PEDIDOS:
			result = this.pedidosService;
			break;
		case SALIDAS_TIENDA:
			result = this.salidasTiendasService;
			break;
		case ORDENES_DE_PRODUCCION:
			result = this.ordenesProduccionService;
			break;
		case ENTRADAS_PT:
			result = this.entradasProductosService;
			break;
		case PRODUCTOS:
			result = this.productosService;
			break;
		case LOCACIONES:
			result = this.locacionesService;
			break;
		default:
			throw new RuntimeException("Tipo de intetgración no concido");
		}

		return result;
	}
	
	@RequestMapping(path = "/check/{tipoIntegracion}/{tipoMensaje}", method = RequestMethod.POST)
	ResponseEntity<?> check(
			// @formatter:off
			@PathVariable IntegracionType tipoIntegracion,
			@PathVariable MessageType tipoMensaje,
			@RequestBody List<String> externalId
			// @formatter:on
	) {
		log.debug("Requesting add for path /check/{}/{} POST", tipoIntegracion, tipoMensaje);

		List<CorreccionCheckResponseDTO> result = getService(tipoIntegracion).check(tipoIntegracion, tipoMensaje, externalId);

		HttpHeaders httpHeaders = new HttpHeaders();
		return new ResponseEntity<>(result, httpHeaders, HttpStatus.ACCEPTED);
	}

	@RequestMapping(path = "/fix/{tipoIntegracion}/{tipoMensaje}", method = RequestMethod.POST)
	ResponseEntity<?> fix(
			// @formatter:off
			@PathVariable IntegracionType tipoIntegracion,
			@PathVariable MessageType tipoMensaje,
			@RequestBody List<String> externalId
			// @formatter:on
	) {
		log.debug("Requesting add for path /fix/{}/{} POST", tipoIntegracion, tipoMensaje);
		RequestType requestType = null;
		HttpStatus httpStatus = HttpStatus.ACCEPTED;
		String body = "OK";
		try {
			requestType = getRequestType(tipoMensaje);
			rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
					new RequestDTO(requestType, tipoIntegracion, externalId));
		} catch (Exception e) {
			httpStatus = HttpStatus.BAD_REQUEST;
			body = e.getMessage();
		}

		HttpHeaders httpHeaders = new HttpHeaders();
		return new ResponseEntity<>(body, httpHeaders, httpStatus);
	}

	private RequestType getRequestType(MessageType tipoMensaje) {
		RequestType result;

		switch (tipoMensaje) {
		case R:
			result = RequestType.CORRECCION_REINTENTO;
			break;
		case C:
			result = RequestType.CORRECCION_CREATE;
			break;
		case U:
			result = RequestType.CORRECCION_UPDATE;
			break;
		default:
			throw new RuntimeException("Tipo de operación no permitida");
		}

		return result;
	}
}