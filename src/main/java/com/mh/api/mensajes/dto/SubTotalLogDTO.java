package com.mh.api.mensajes.dto;

import java.time.LocalDate;

import com.mh.model.esb.domain.msg.MessageStatusType;
import com.mh.model.esb.domain.msg.MessageType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SubTotalLogDTO {
	@NonNull
	private LocalDate fecha;
	@NonNull
	private MessageType type;
	@NonNull
	private MessageStatusType status;
	private int code;
	@NonNull
	private String message;
	@NonNull
	private String exception;
	private int total;
}