package com.mh.amqp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mh.model.esb.domain.esb.IntegracionType;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RequestDTO implements Serializable {

	private static final long serialVersionUID = -4542440468114218239L;

	private RequestType requestType;
	private IntegracionType integracionType;
	private List<String> externalId;

	public RequestDTO(RequestType requestType) {
		this(requestType, null, new ArrayList<>());
	}

	public RequestDTO(RequestType requestType, IntegracionType integracionType) {
		this(requestType, integracionType, new ArrayList<>());
	}

	public RequestDTO(RequestType requestType, IntegracionType integracionType, List<String> externalId) {
		super();
		this.requestType = requestType;
		this.integracionType = integracionType;
		this.externalId = externalId;
	}
}
