package com.mh.api.cajas.dto;

import lombok.Data;

/**
 * DTO Linea Caja. Representa una de las referencias que contiene una caja y su cantidad 
 * @author arosorio@gmail.com
 *
 */
@Data
public class LineaCajaDTO {
	private String codigo;
	private int cantidad;
}