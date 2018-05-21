package com.mh.api.mensajes.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.mh.model.esb.domain.msg.MessageStatusType;
import com.mh.model.esb.domain.msg.MessageType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;


/**
 * Representa un mensaje enviado al sistema destino
 * 
 * @author arosorio@gmail.com
 *
 */
@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LogDTO {
	@NonNull
	private LocalDate fecha;
	@NonNull
	private String externalId;
	@NonNull
	private String id;
	private long mid;
	@NonNull
	private MessageType type;
	@NonNull
	private MessageStatusType status;
	private int code;
	@NonNull
	private String message;
	@NonNull
	private String exception;
	private int intentos;
	@NonNull
	private LocalDateTime fechaUltimoPull;
	private LocalDateTime fechaUltimoPush;
}