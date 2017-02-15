package com.mh.api.sync.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@AllArgsConstructor
public class ResultDTO {
	private int code;
	private String message;
	private String exception;
}
