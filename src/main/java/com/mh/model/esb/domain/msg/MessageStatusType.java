package com.mh.model.esb.domain.msg;


/**
 * Enumeración estados de mensajes
 * 
 * @author arosorio@gmail.com
 *
 */
public enum MessageStatusType {
	PENDIENTE, 
	ERROR, 
	REINTENTO, 
	ENVIADO, 
	
	DESCARTADO, 
	INTEGRADO, 
	INCONSISTENTE
}
