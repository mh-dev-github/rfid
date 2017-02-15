package com.mh.api.mensajes;

import static com.mh.api.Constantes.FORMATO_DATE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mh.api.mensajes.dto.LogDTO;
import com.mh.api.mensajes.dto.SubTotalLogDTO;
import com.mh.api.mensajes.servicios.BaseMensajesService;
import com.mh.api.mensajes.servicios.EntradasProductoMensajesService;
import com.mh.api.mensajes.servicios.LocacionesMensajesService;
import com.mh.api.mensajes.servicios.OrdenesProduccionMensajesService;
import com.mh.api.mensajes.servicios.PedidosMensajesService;
import com.mh.api.mensajes.servicios.ProductosMensajesService;
import com.mh.api.mensajes.servicios.SalidasTiendaMensajesService;
import com.mh.model.esb.domain.esb.BaseEntity;
import com.mh.model.esb.domain.esb.IntegracionType;
import com.mh.model.esb.domain.msg.BaseMessageEntity;
import com.mh.model.esb.domain.msg.MessageStatusType;
import com.mh.model.esb.domain.msg.MessageType;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/mensajes")
@CrossOrigin
@Slf4j
public class MensajesController {

	@Autowired
	PedidosMensajesService pedidosService;

	@Autowired
	SalidasTiendaMensajesService salidasTiendasService;

	@Autowired
	OrdenesProduccionMensajesService ordenesProduccionService;

	@Autowired
	EntradasProductoMensajesService entradasProductosService;

	@Autowired
	ProductosMensajesService productosService;

	@Autowired
	LocacionesMensajesService locacionesService;

	private BaseMensajesService<? extends BaseEntity, ? extends BaseMessageEntity> getService(IntegracionType tipoIntegracion) {
		BaseMensajesService<? extends BaseEntity, ? extends BaseMessageEntity> result;

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

	@RequestMapping(path = "{tipoIntegracion}/search/{dummy}", method = RequestMethod.GET, params = {
			"tipoConsulta=subtotales-log" })
	ResponseEntity<List<SubTotalLogDTO>> getSubTotalesLog(
			// @formatter:off
			@PathVariable IntegracionType tipoIntegracion,
			@MatrixVariable(required=false) List<MessageType> tipoMensaje, 
			@MatrixVariable(required=false) List<MessageStatusType> estado,
			@MatrixVariable @DateTimeFormat(pattern = FORMATO_DATE) LocalDate fechaDesde,
			@MatrixVariable @DateTimeFormat(pattern = FORMATO_DATE) LocalDate fechaHasta,
			HttpServletRequest request
			// @formatter:on
	) {
		log.debug("Requesting {} {} {}", request.getMethod(), request.getRequestURL(), request.getQueryString());

		LocalDateTime desde = LocalDateTime.of(fechaDesde, LocalTime.MIDNIGHT);
		LocalDateTime hasta = LocalDateTime.of(fechaHasta, LocalTime.MIDNIGHT).plusDays(1);
		List<SubTotalLogDTO> result = getService(tipoIntegracion).getSubTotalesLog(desde, hasta, tipoMensaje, estado);
		return new ResponseEntity<>(result, crearHeaders(), HttpStatus.OK);
	}

	@RequestMapping(path = "{tipoIntegracion}/search/{dummy}", method = RequestMethod.GET, params = { "tipoConsulta=logs-mas-recientes" })
	ResponseEntity<List<LogDTO>> getLogsMasRecientes(
			// @formatter:off
			@PathVariable IntegracionType tipoIntegracion,
			@MatrixVariable(required=false) List<MessageType> tipoMensaje, 
			@MatrixVariable(required=false) List<MessageStatusType> estado,
			@MatrixVariable @DateTimeFormat(pattern = FORMATO_DATE) LocalDate fechaDesde,
			@MatrixVariable @DateTimeFormat(pattern = FORMATO_DATE) LocalDate fechaHasta,
			@MatrixVariable(required=false) List<String> externalId, 
			HttpServletRequest request
			// @formatter:on
	) {
		log.debug("Requesting {} {} {}", request.getMethod(), request.getRequestURL(), request.getQueryString());

		LocalDateTime desde = LocalDateTime.of(fechaDesde, LocalTime.MIDNIGHT);
		LocalDateTime hasta = LocalDateTime.of(fechaHasta, LocalTime.MIDNIGHT).plusDays(1);
		List<LogDTO> result = getService(tipoIntegracion).getLogsMasRecientes(desde, hasta, tipoMensaje, estado, externalId);
		return new ResponseEntity<>(result, crearHeaders(), HttpStatus.OK);
	}

	@RequestMapping(path = "{tipoIntegracion}/logs/{mid}", method = RequestMethod.GET)
	ResponseEntity<? extends BaseMessageEntity > getLog(
			// @formatter:off
			@PathVariable IntegracionType tipoIntegracion,
			@PathVariable Long mid,
			HttpServletRequest request
			// @formatter:on
	) {
		log.debug("Requesting {} {} {}", request.getMethod(), request.getRequestURL(), request.getQueryString());

		BaseMessageEntity result = getService(tipoIntegracion).getLog(mid);
		return new ResponseEntity<>(result, crearHeaders(), HttpStatus.OK);
	}

	protected HttpHeaders crearHeaders() {
		return new HttpHeaders();
	}
}
