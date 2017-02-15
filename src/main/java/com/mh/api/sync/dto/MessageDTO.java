package com.mh.api.sync.dto;

import java.time.LocalDateTime;

import com.mh.api.sync.dto.base.PayloadDTO;
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
public class MessageDTO<S extends PayloadDTO> {
	private int mid;
	@NonNull
	private MessageType type;
	@NonNull
	private MessageStatusType status;
	private int intents;
	private LocalDateTime lastPull;
	private LocalDateTime lastPush;
	@NonNull
	private ResultDTO result;
	@NonNull
	private S payload;
}
