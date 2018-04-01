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
import org.springframework.stereotype.Service;

import com.mh.api.mensajes.dto.LogDTO;
import com.mh.api.mensajes.dto.LogRowMapper;
import com.mh.api.mensajes.dto.SubTotalLogDTO;
import com.mh.api.mensajes.dto.SubTotalLogRowMapper;
import com.mh.model.esb.domain.esb.BaseEntity;
import com.mh.model.esb.domain.msg.BaseMessageEntity;
import com.mh.model.esb.domain.msg.MessageStatusType;
import com.mh.model.esb.domain.msg.MessageType;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public abstract class MensajesService<TEntity extends BaseEntity, TMessage extends BaseMessageEntity> {

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
	protected List<String> messageTypeToString(List<MessageType> list) {
		if (list == null) {
			list = new ArrayList<>();
		}
		if (list.isEmpty()) {
			for (MessageType e : MessageType.values()) {
				list.add(e);
			}
		}
		return list.stream().map(a -> a.toString()).collect(Collectors.toList());
	}

	protected List<String> messageStatusToString(List<MessageStatusType> list) {
		if (list == null) {
			list = new ArrayList<>();
		}
		if (list.isEmpty()) {
			for (MessageStatusType e : MessageStatusType.values()) {
				list.add(e);
			}
		}
		return list.stream().map(a -> a.toString()).collect(Collectors.toList());
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public List<SubTotalLogDTO> getSubTotalesLog(
			// @formatter:off
			LocalDateTime fechaDesde, 
			LocalDateTime fechaHasta, 
			List<MessageType> tipoCambio,
			List<MessageStatusType> estado
			// @formatter:on
	) {
		String sql = getSQLSelectSubTotalesLog().replace("{table_name}", getLogTableName());
		// @formatter:off
		val parametros = new MapSqlParameterSource()
				.addValue("fechaDesde", fechaDesde)
				.addValue("fechaHasta", fechaHasta)
				.addValue("tipoCambio", messageTypeToString(tipoCambio))
				.addValue("estadoCambio", messageStatusToString(estado));
		// @formatter:on

		log.info("sql {} ", sql);
		log.info("fechaDesde", fechaDesde);
		log.info("fechaHasta", fechaHasta);
		log.info("tipoCambio", messageTypeToString(tipoCambio));
		log.info("estadoCambio", messageStatusToString(estado));

		List<SubTotalLogDTO> result = esbJdbcTemplate.query(sql, parametros, new SubTotalLogRowMapper());
		return result;
	}

	public List<LogDTO> getLogsMasRecientes(
			// @// @formatter:off
			LocalDateTime fechaDesde, 
			LocalDateTime fechaHasta, 
			List<MessageType> tipoCambio,
			List<MessageStatusType> estado, 
			List<String> externalId
			// @formatter:on
	) {
		String sql = getSQLSelectLogs().replace("{table_name}", getLogTableName());

		// @formatter:off
		val parametros = new MapSqlParameterSource()
				.addValue("fechaDesde", fechaDesde)
				.addValue("fechaHasta", fechaHasta)
				.addValue("tipoCambio", messageTypeToString(tipoCambio))
				.addValue("estadoCambio", messageStatusToString(estado))
				.addValue("externalId", externalId)
				.addValue("cualquierExternalId",(externalId == null)?1:0);
		// @formatter:on

		log.info("sql {} ", sql);
		log.info("fechaDesde", fechaDesde);
		log.info("fechaHasta", fechaHasta);
		log.info("tipoCambio", messageTypeToString(tipoCambio));
		log.info("estadoCambio", messageStatusToString(estado));
		log.info("externalId", externalId);
		log.info("cualquierExternalId", (externalId == null) ? 1 : 0);

		List<LogDTO> result = esbJdbcTemplate.query(sql, parametros, new LogRowMapper());
		return result;
	}

	public TMessage getLog(Long mid) {
		return getMessageRepository().findOne(mid);
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	protected String getSQLSelectSubTotalesLog() {
		// @formatter:off
		return  "\n" +
				" WITH \n"+
				" cte_00 AS \n"+
				" ( \n"+
				"     SELECT \n"+
				"          CAST(a.fecha_ultimo_pull AS DATE) AS fecha_ultimo_pull \n"+
				"         ,a.tipo_cambio \n"+
				"         ,a.estado_cambio \n"+
				"         ,a.sync_codigo \n"+
				"         ,LEFT(a.sync_mensaje,50) AS sync_mensaje \n"+
				"         ,a.sync_exception \n"+
				"         ,ROW_NUMBER() OVER(PARTITION BY a.externalId ORDER BY a.mid DESC) AS orden \n"+
				"     FROM {table_name} a \n"+
				"     WHERE  \n"+
				"         0 = 0 \n"+
				"     AND a.fecha_ultimo_pull >= :fechaDesde \n"+
				"     AND a.fecha_ultimo_pull <  :fechaHasta \n"+
				" ) \n"+
				" SELECT \n"+
				"      a.fecha_ultimo_pull \n"+
				"     ,a.tipo_cambio \n"+
				"     ,a.estado_cambio \n"+
				"     ,a.sync_codigo \n"+
				"     ,a.sync_mensaje \n"+
				"     ,a.sync_exception \n"+
				"     ,COUNT(1) AS total \n"+
				" FROM cte_00 a \n"+
				" WHERE \n"+
				"     a.orden = 1 \n"+
				" AND a.tipo_cambio IN (:tipoCambio) \n"+
				" AND a.estado_cambio IN (:estadoCambio) \n"+
				" GROUP BY \n"+
				"      a.tipo_cambio \n"+
				"     ,a.fecha_ultimo_pull \n"+
				"     ,a.estado_cambio \n"+
				"     ,a.sync_codigo \n"+
				"     ,a.sync_mensaje \n"+
				"     ,a.sync_exception \n"+
				" ORDER BY \n"+
				"      a.tipo_cambio \n"+
				"     ,a.fecha_ultimo_pull DESC \n"+
				"     ,a.estado_cambio \n"+
				"     ,a.sync_codigo \n"+
				" ";
		// @formatter:on
	}

	protected String getSQLSelectLogs() {
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
				"     AND a.fecha_ultimo_pull >= :fechaDesde \n"+
				"     AND a.fecha_ultimo_pull <  :fechaHasta \n"+
				" ) \n"+
				" SELECT \n"+
				"     a.* \n"+
				" FROM cte_00 a \n"+
				" WHERE \n"+
				"     a.orden = 1 \n"+
				" AND a.tipo_cambio IN (:tipoCambio) \n"+
				" AND a.estado_cambio IN (:estadoCambio) \n"+
				" AND (a.externalId IN (:externalId) OR :cualquierExternalId = 1 )\n"+
				" ORDER BY \n"+
				"      a.fecha DESC \n"+
				"     ,a.externalId \n"+
				"     ,a.fecha_ultimo_pull \n"+
				" ";
		// @formatter:on
	}
}
