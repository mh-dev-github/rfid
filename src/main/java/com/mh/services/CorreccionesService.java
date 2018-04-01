package com.mh.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.mh.api.mensajes.dto.LogDTO;
import com.mh.api.mensajes.dto.LogRowMapper;
import com.mh.model.esb.domain.esb.BaseEntity;
import com.mh.model.esb.domain.esb.IntegracionType;
import com.mh.model.esb.domain.msg.BaseMessageEntity;
import com.mh.model.esb.domain.msg.MessageStatusType;
import com.mh.model.esb.domain.msg.MessageType;

import lombok.val;

public abstract class CorreccionesService<TEntity extends BaseEntity, TMessage extends BaseMessageEntity> {

	@Qualifier("esbJdbcTemplate")

	@Autowired
	protected NamedParameterJdbcTemplate esbJdbcTemplate;

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	abstract protected JpaRepository<TMessage, Long> getMessageRepository();

	abstract protected JpaRepository<TEntity, String> getEntityRepository();

	abstract protected String getLogTableName();

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	@Transactional(value = "transactionManager")
	public void retry(IntegracionType tipoIntegracion, MessageType tipoMensaje, List<String> externalId) {

		List<CorreccionCheckResponseDTO> list = this.check(tipoIntegracion, tipoMensaje, externalId);

		// @formatter:off
		List<String> habilitados = list
				.stream()
				.filter(a -> a.isHabilitado())
				.map(a -> a.getExternalId())
				.collect(Collectors.toList());
		// @formatter:on

		LocalDateTime fechaUltimoPush = LocalDateTime.now();

		for (String e : habilitados) {
			//// @formatter:off
			List<LogDTO> logs = this.getLogs(tipoIntegracion ,e)
					.stream()
					.filter( a -> !MessageType.X.equals(a.getType()))
					.collect(Collectors.toList());
 			// @formatter:on
			if (!logs.isEmpty()) {
				TMessage mensaje = this.getMessageRepository().findOne(logs.get(0).getMid());
				mensaje.setFechaUltimoPush(fechaUltimoPush);

				TMessage clon = clonarMensaje(mensaje);
				clon.setEstadoCambio(MessageStatusType.REINTENTO);
				clon.setIntentos(0);
				clon.setFechaUltimoPull(fechaUltimoPush);
				clon.setFechaUltimoPush(null);
				clon.setSyncCodigo(0);
				clon.setSyncMensaje("");
				clon.setSyncException("");

				TEntity entidad = getEntityRepository().findOne(mensaje.getExternalId());
				entidad.setFechaUltimoPush(fechaUltimoPush);
				entidad.setSincronizado(false);

				getMessageRepository().save(mensaje);
				getMessageRepository().save(clon);
				getEntityRepository().save(entidad);
			}
		}
	}

	@Transactional(value = "transactionManager")
	public void create(IntegracionType tipoIntegracion, MessageType tipoMensaje, List<String> externalId) {

		List<CorreccionCheckResponseDTO> list = this.check(tipoIntegracion, tipoMensaje, externalId);

		// @formatter:off
		List<String> habilitados = list
				.stream()
				.filter(a -> a.isHabilitado())
				.map(a -> a.getExternalId())
				.collect(Collectors.toList());
		// @formatter:on

		LocalDateTime fechaUltimoPush = LocalDateTime.now();

		for (String e : habilitados) {
			//// @formatter:off
			List<LogDTO> logs = this.getLogs(tipoIntegracion ,e)
					.stream()
					.filter( a -> !MessageType.X.equals(a.getType()))
					.collect(Collectors.toList());
 			// @formatter:on
			if (!logs.isEmpty()) {
				TMessage mensaje = this.getMessageRepository().findOne(logs.get(0).getMid());
				mensaje.setFechaUltimoPush(fechaUltimoPush);

				TMessage clon = clonarMensaje(mensaje);
				clon.setTipoCambio(MessageType.C);
				clon.setEstadoCambio(MessageStatusType.PENDIENTE);
				clon.setIntentos(0);
				clon.setFechaUltimoPull(fechaUltimoPush);
				clon.setFechaUltimoPush(null);
				clon.setSyncCodigo(0);
				clon.setSyncMensaje("");
				clon.setSyncException("");
				clon.setId("");

				TEntity entidad = getEntityRepository().findOne(mensaje.getExternalId());
				entidad.setFechaUltimoPush(fechaUltimoPush);
				entidad.setSincronizado(false);
				entidad.setId("");

				getMessageRepository().save(mensaje);
				getMessageRepository().save(clon);
				getEntityRepository().save(entidad);
			}
		}
	}

	@Transactional(value = "transactionManager")
	public void update(IntegracionType tipoIntegracion, MessageType tipoMensaje, List<String> externalId) {

		List<CorreccionCheckResponseDTO> list = this.check(tipoIntegracion, tipoMensaje, externalId);

		// @formatter:off
		List<String> habilitados = list
				.stream()
				.filter(a -> a.isHabilitado())
				.map(a -> a.getExternalId())
				.collect(Collectors.toList());
		// @formatter:on

		LocalDateTime fechaUltimoPush = LocalDateTime.now();

		for (String e : habilitados) {
			//// @formatter:off
			List<LogDTO> logs = this.getLogs(tipoIntegracion ,e)
					.stream()
					.filter( a -> !MessageType.X.equals(a.getType()))
					.collect(Collectors.toList());
 			// @formatter:on
			if (!logs.isEmpty()) {
				TMessage mensaje = this.getMessageRepository().findOne(logs.get(0).getMid());
				mensaje.setFechaUltimoPush(fechaUltimoPush);

				TMessage clon = clonarMensaje(mensaje);
				clon.setTipoCambio(MessageType.U);
				clon.setEstadoCambio(MessageStatusType.PENDIENTE);
				clon.setIntentos(0);
				clon.setFechaUltimoPull(fechaUltimoPush);
				clon.setFechaUltimoPush(null);
				clon.setSyncCodigo(0);
				clon.setSyncMensaje("");
				clon.setSyncException("");

				TEntity entidad = getEntityRepository().findOne(mensaje.getExternalId());
				entidad.setFechaUltimoPush(fechaUltimoPush);
				entidad.setSincronizado(false);

				getMessageRepository().save(mensaje);
				getMessageRepository().save(clon);
				getEntityRepository().save(entidad);
			}
		}
	}

	// -------------------------------------------------------------------------------------
	// REINTENTAR
	// -------------------------------------------------------------------------------------
	public List<CorreccionCheckResponseDTO> check(IntegracionType tipoIntegracion, MessageType tipoMensaje, List<String> externalId) {

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

	// -------------------------------------------------------------------------------------
	// GET LOGS
	// -------------------------------------------------------------------------------------
	private List<LogDTO> getLogs(IntegracionType tipoIntegracion, String externalId) {

		String sql = getSQLSelectLogs().replace("{table_name}", getLogTableName());
		// @formatter:off
		val parametros = new MapSqlParameterSource()
				.addValue("externalId", externalId);
		// @formatter:on

		List<LogDTO> result = esbJdbcTemplate.query(sql, parametros, new LogRowMapper());
		return result;
	}

	private String getSQLSelectLogs() {
	// @formatter:off
	return  "\n" +
			" WITH \n"+
			" cte_00 AS \n"+
			" ( \n"+
			"     SELECT \n"+
			"          CAST(a.fecha_ultimo_pull AS DATE) AS fecha \n"+
			"         ,a.externalId \n"+
			"         ,a.id \n"+
			"         ,a.mid \n"+
			"         ,a.tipo_cambio \n"+
			"         ,a.estado_cambio \n"+
			"         ,a.intentos \n"+
			"         ,a.fecha_ultimo_pull \n"+
			"         ,a.fecha_ultimo_push \n"+
			"         ,a.sync_codigo \n"+
			"         ,a.sync_mensaje \n"+
			"         ,a.sync_exception \n"+
			"         ,ROW_NUMBER() OVER(PARTITION BY a.externalId ORDER BY a.mid DESC) AS orden \n"+
			"     FROM {table_name} a \n"+
			"     WHERE  \n"+
			"         0 = 0 \n"+
			"     AND a.externalId = :externalId \n"+
			" ) \n"+
			" SELECT \n"+
			"     a.* \n"+
			" FROM cte_00 a \n"+
			" ORDER BY \n"+
			"     a.orden \n"+
			" ";
	// @formatter:on
	}

	abstract protected TMessage clonarMensaje(TMessage a);
}
