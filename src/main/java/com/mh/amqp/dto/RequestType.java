package com.mh.amqp.dto;

public enum RequestType {
	SYNC_ALL,
	PULL,
	PUSH,
	PUSH_UPDATE,
	CHECK,
	
	CORRECCION_REINTENTO,
	CORRECCION_CREATE,
	CORRECCION_UPDATE
}
