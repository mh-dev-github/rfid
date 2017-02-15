package com.mh.api.correcciones.servicios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.mh.api.correcciones.dto.CorreccionCheckResponseDTO;
import com.mh.api.mensajes.dto.LogDTO;
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

public abstract class BaseCorreccionesService<TEntity extends BaseEntity, TMessage extends BaseMessageEntity> {

	@Autowired
	PedidosMensajesService pedidoService;

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

	// -------------------------------------------------------------------------------------
	// REINTENTAR
	// -------------------------------------------------------------------------------------
	abstract protected JpaRepository<TMessage, Long> getMessageRepository();

	abstract protected JpaRepository<TEntity, String> getEntityRepository();

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	@Transactional(readOnly = true)
	public void retry(IntegracionType tipoIntegracion, MessageType tipoMensaje, List<String> externalId) {

		List<CorreccionCheckResponseDTO> list = this.check(tipoIntegracion, tipoMensaje, externalId);

		//// @formatter:off
		List<String> habilitados = list
				.stream()
				.filter(a -> a.isHabilitado())
				.map(a -> a.getExternalId())
				.collect(Collectors.toList());
		// @formatter:on

		LocalDateTime fechaUltimoPush = LocalDateTime.now();
		
		for (String e : habilitados) {
			TMessage mensaje = getLastMessage(e);

			mensaje.setFechaUltimoPush(fechaUltimoPush);
			TMessage clon = clonarMensaje(mensaje);
			clon.setEstadoCambio(MessageStatusType.REINTENTO);
			clon.setIntentos(0);
			clon.setFechaUltimoPull(fechaUltimoPush);

			TEntity entidad = getEntityRepository().findOne(mensaje.getExternalId());
			entidad.setFechaUltimoPush(fechaUltimoPush);
			entidad.setSincronizado(false);

			getMessageRepository().save(mensaje);
			getMessageRepository().save(clon);
			getEntityRepository().save(entidad);
		}
	}

	@Transactional(readOnly = true)
	public void create(IntegracionType tipoIntegracion, MessageType tipoMensaje, List<String> externalId) {

		List<CorreccionCheckResponseDTO> list = this.check(tipoIntegracion, tipoMensaje, externalId);

		//// @formatter:off
		List<String> habilitados = list
				.stream()
				.filter(a -> a.isHabilitado())
				.map(a -> a.getExternalId())
				.collect(Collectors.toList());
		// @formatter:on

		LocalDateTime fechaUltimoPush = LocalDateTime.now();
		
		for (String e : habilitados) {
			TMessage mensaje = getLastMessage(e);

			mensaje.setFechaUltimoPush(fechaUltimoPush);
			TMessage clon = clonarMensaje(mensaje);
			clon.setEstadoCambio(MessageStatusType.REINTENTO);
			clon.setIntentos(0);
			clon.setFechaUltimoPull(fechaUltimoPush);

			TEntity entidad = getEntityRepository().findOne(mensaje.getExternalId());
			entidad.setFechaUltimoPush(fechaUltimoPush);
			entidad.setSincronizado(false);

			getMessageRepository().save(mensaje);
			getMessageRepository().save(clon);
			getEntityRepository().save(entidad);
		}
	}
	
	@Transactional(readOnly = true)
	public void update(IntegracionType tipoIntegracion, MessageType tipoMensaje, List<String> externalId) {

		List<CorreccionCheckResponseDTO> list = this.check(tipoIntegracion, tipoMensaje, externalId);

		//// @formatter:off
		List<String> habilitados = list
				.stream()
				.filter(a -> a.isHabilitado())
				.map(a -> a.getExternalId())
				.collect(Collectors.toList());
		// @formatter:on

		LocalDateTime fechaUltimoPush = LocalDateTime.now();
		
		for (String e : habilitados) {
			TMessage mensaje = getLastMessage(e);

			mensaje.setFechaUltimoPush(fechaUltimoPush);
			TMessage clon = clonarMensaje(mensaje);
			clon.setEstadoCambio(MessageStatusType.REINTENTO);
			clon.setIntentos(0);
			clon.setFechaUltimoPull(fechaUltimoPush);

			TEntity entidad = getEntityRepository().findOne(mensaje.getExternalId());
			entidad.setFechaUltimoPush(fechaUltimoPush);
			entidad.setSincronizado(false);

			getMessageRepository().save(mensaje);
			getMessageRepository().save(clon);
			getEntityRepository().save(entidad);
		}
	}
	
	
	
	private TMessage getLastMessage(String externalId) {
//		String sql = getSQLSelectLastMessage(externalId, mid).replace("{table_name}", getLogTableName());
//		;
//
//		// @formatter:off
//		val parametros = new MapSqlParameterSource()
//				.addValue("externalId", externalId)
//				.addValue("mid", mid);
//		// @formatter:on
//
//		List<Long> mids = esbJdbcTemplate.queryForList(sql, parametros, Long.class);
//		List<TMessage> rows = getMessageRepository().findAll(mids);

		return null;
	}

	abstract protected TMessage crearMensaje(TEntity entidad);
	
	abstract protected TMessage clonarMensaje(TMessage a);


	// -------------------------------------------------------------------------------------
	// REINTENTAR
	// -------------------------------------------------------------------------------------
	public void generarUpdate(String externalId) {
		LocalDateTime fechaUltimoPush = LocalDateTime.now();
		TEntity entidad = getEntityRepository().findOne(externalId);

		checkIfUpdateAllowed(entidad);

		TMessage mensaje = crearMensaje(entidad);
		entidad.setFechaUltimoPush(fechaUltimoPush);
		entidad.setSincronizado(false);

		getMessageRepository().save(mensaje);
		getEntityRepository().save(entidad);
	}

	private void checkIfUpdateAllowed(BaseEntity entidad) {
//		if (entidad == null) {
//			throw new RuntimeException("El registro al cual le pretende generar un update, no existe");
//		}
//
//		if (!entidad.isSincronizado()) {
//			throw new RuntimeException(
//					"El registro al cual le pretende generar un update, tiene pendiente una sincronización");
//		}
//
//		List<LogDTO> logs = getLogs(entidad.getExternalId());
//
//		if (!logs.isEmpty()) {
//			LogDTO log = logs.get(0);
//
//			if (log.getStatus() != MessageStatusType.INTEGRADO) {
//				throw new RuntimeException(
//						"El ultimo mensaje del registro al cual pretende generarle un update, se encuentra en estado "
//								+ log.getStatus());
//			}
//		}
	}

	protected void poblarMessageEntity(BaseMessageEntity mensaje, BaseEntity entidad) {
		LocalDateTime fechaUltimoPull = LocalDateTime.now();

		mensaje.setTipoCambio(MessageType.U);
		mensaje.setEstadoCambio(MessageStatusType.PENDIENTE);
		mensaje.setIntentos(0);
		mensaje.setFechaUltimoPull(fechaUltimoPull);
		mensaje.setFechaUltimoPush(null);
		mensaje.setSyncCodigo(0);
		mensaje.setSyncMensaje("");
		mensaje.setSyncException("");
		mensaje.setExternalId(entidad.getExternalId());
		mensaje.setId(entidad.getId());
	}

	public List<CorreccionCheckResponseDTO> check(

			IntegracionType tipoIntegracion, MessageType tipoMensaje, List<String> externalId) {
		List<CorreccionCheckResponseDTO> result = new ArrayList<>();
		for (String e : externalId) {
			//// @formatter:off
			List<LogDTO> logs = this.getLogs(tipoIntegracion ,e)
					.stream()
					.filter( a -> !MessageType.X.equals(a.getType()))
					.collect(Collectors.toList());
 			// @formatter:on

			CorreccionCheckResponseDTO dto = null;
			dto = checkExists(e, logs);
			if (dto == null) {
				dto = checkStatus(e, logs, tipoMensaje);
			}
			if (dto == null) {
				dto = new CorreccionCheckResponseDTO(e, true, "");
			}
			result.add(dto);
		}

		return result;
	}

	private CorreccionCheckResponseDTO checkExists(String e, List<LogDTO> logs) {
		CorreccionCheckResponseDTO dto = null;
		if (logs.isEmpty()) {
			dto = new CorreccionCheckResponseDTO(e, false, "No tiene registros en el Log");
		}
		return dto;
	}

	private CorreccionCheckResponseDTO checkStatus(String e, List<LogDTO> logs, MessageType tipoMensaje) {
		CorreccionCheckResponseDTO dto = null;
		LogDTO log = logs.get(0);
		switch (tipoMensaje) {
		case R:
			switch (log.getStatus()) {
			case DESCARTADO:
				break;
			default:
				dto = new CorreccionCheckResponseDTO(e, false,
						"Su último estado en el log es " + log.getStatus()
								+ ". Para poder REINTENTAR la operación, el último estado debe ser "
								+ MessageStatusType.DESCARTADO + ".");
				break;
			}
			break;
		case C:
		case U:
			switch (log.getStatus()) {
			case INTEGRADO:
			case INCONSISTENTE:
				break;
			default:
				dto = new CorreccionCheckResponseDTO(e, false,
						"Su último estado en el log es " + log.getStatus()
								+ ". Para poder REINTENTAR la operación de creación o actualización, el último estado debe ser "
								+ MessageStatusType.INTEGRADO + " o " + MessageStatusType.INCONSISTENTE + ".");
				break;
			}
			break;
		default:
			break;
		}

		return dto;
	}

	private List<LogDTO> getLogs(IntegracionType tipoIntegracion, String externalId) {

		BaseMensajesService<?, ?> service = this.getService(tipoIntegracion);

		List<LogDTO> result = null;//service.getLogs(externalId);
		return result;
	}

	private BaseMensajesService<?, ?> getService(IntegracionType tipoIntegracion) {
		BaseMensajesService<?, ?> result;

		switch (tipoIntegracion) {
		case PEDIDOS:
			result = this.pedidoService;
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
}
