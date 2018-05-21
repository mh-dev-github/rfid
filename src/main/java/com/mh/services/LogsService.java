package com.mh.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.mh.api.mensajes.dto.LogDTO;
import com.mh.api.mensajes.dto.LogRowMapper;
import com.mh.model.esb.domain.esb.BaseEntity;
import com.mh.model.esb.domain.msg.BaseMessageEntity;

import lombok.val;


/**
 * Servicio base de consulta de mensajes
 * 
 * @author arosorio@gmail.com
 *
 * @param <E> Entidad
 * @param <M> Mensaje
 */
public abstract class LogsService<E extends BaseEntity, M extends BaseMessageEntity> {
	@Qualifier("esbJdbcTemplate")
	@Autowired
	protected NamedParameterJdbcTemplate esbJdbcTemplate;

	abstract protected String getLogTableName();

	// -------------------------------------------------------------------------------------
	// AUDITAR
	// -------------------------------------------------------------------------------------
	public List<LogDTO> getLogs(LocalDateTime fechaDesde, LocalDateTime fechaHasta) {

		String sql = getSQLSelectLogs().replace("{table_name}", getLogTableName());
		// @formatter:off
		val parametros = new MapSqlParameterSource()
				.addValue("fechaDesde", fechaDesde)
				.addValue("fechaHasta", fechaHasta);
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
			"     AND a.fecha_ultimo_pull > :fechaDesde \n"+
			"     AND a.fecha_ultimo_pull <= :fechaHasta \n"+
			" ) \n"+
			" SELECT \n"+
			"     a.* \n"+
			" FROM cte_00 a \n"+
			" WHERE 1 = 1 \n"+
			" AND a.orden = 1 \n"+
			" AND (0 = 1 \n"+
			" OR (a.estado_cambio IN ('DESCARTADO','INCONSISTENTE')) \n"+
			" OR (a.estado_cambio IN ('ERROR') AND a.tipo_cambio IN('X')) \n"+
			" ) \n"+
			" ORDER BY \n"+
			"     a.externalId \n"+
			" ";
	// @formatter:on
	}
}