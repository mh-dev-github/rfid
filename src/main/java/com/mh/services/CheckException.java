package com.mh.services;


/**
 * RuntimeException que debe ser lanzada cuando la verificacion de la integración de una entidad de un resultado inconsistente
 * 
 * @author arosorio@gmail.com
 *
 */
public class CheckException extends RuntimeException {

	private static final long serialVersionUID = -9074969399978481339L;

	public CheckException(String message) {
		super(message);
	}
}
