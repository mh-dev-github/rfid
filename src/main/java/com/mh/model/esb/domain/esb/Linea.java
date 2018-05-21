package com.mh.model.esb.domain.esb;

import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Super clase de las lineas que contienen articulos y cantidades
 * 
 * @author arosorio@gmail.com
 *
 */
@Data
@NoArgsConstructor
@Embeddable
@MappedSuperclass
public class Linea {
	private String sku;
	private int amount;	
}