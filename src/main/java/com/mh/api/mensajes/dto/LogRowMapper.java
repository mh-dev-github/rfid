package com.mh.api.mensajes.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.mh.model.esb.domain.msg.MessageStatusType;
import com.mh.model.esb.domain.msg.MessageType;

public class LogRowMapper implements RowMapper<LogDTO> {
	public LogDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		// @formatter:off
		return LogDTO
		.builder()
		.fecha(rs.getDate("fecha").toLocalDate())
		.externalId(rs.getString("externalId"))
		.id(rs.getString("id"))
		.mid(rs.getLong("mid"))

		.type(MessageType.valueOf(rs.getString("tipo_cambio")))
		.status(MessageStatusType.valueOf(rs.getString("estado_cambio")))
		.code(rs.getInt("sync_codigo"))
		.message(rs.getString("sync_mensaje"))
		.exception(rs.getString("sync_exception"))
		.intentos(rs.getInt("intentos"))
		
		.fechaUltimoPull(rs.getTimestamp("fecha_ultimo_pull").toLocalDateTime())
		.fechaUltimoPush((rs.getTimestamp("fecha_ultimo_push")!=null)?rs.getTimestamp("fecha_ultimo_push").toLocalDateTime():null)
		.build();
	};
	// @formatter:on
}