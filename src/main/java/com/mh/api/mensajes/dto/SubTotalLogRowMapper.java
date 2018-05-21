package com.mh.api.mensajes.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.mh.model.esb.domain.msg.MessageStatusType;
import com.mh.model.esb.domain.msg.MessageType;


/**
 * 
 * RowMapper que convierte los resultados de una consulta que haga el conteo de los mensajes en una tabla de mensaje a un DTO.
 * 
 * @author arosorio@gmail.com
 *
 */
public class SubTotalLogRowMapper implements RowMapper<SubTotalLogDTO> {
	public SubTotalLogDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		// @formatter:off
		return SubTotalLogDTO
		.builder()
		.fecha(rs.getDate("fecha_ultimo_pull").toLocalDate())
		.type(MessageType.valueOf(rs.getString("tipo_cambio")))
		.status(MessageStatusType.valueOf(rs.getString("estado_cambio")))
		.code(rs.getInt("sync_codigo"))
		.message(rs.getString("sync_mensaje"))
		.exception(rs.getString("sync_exception"))
		.total(rs.getInt("total"))
		.build();
	};
	// @formatter:on
}