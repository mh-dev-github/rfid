package com.mh.services;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO que contiene el resultado de la validación que se hace a una entidad para verificar si se puede hacer una correción 
 * 
 * @author arosorio@gmail.com
 *
 */
@Data
@AllArgsConstructor
public class CorreccionCheckResponseDTO {
	private String externalId;
	private boolean habilitado;
	private String causa;
}