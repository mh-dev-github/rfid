package com.mh.model.esb.domain.esb;

import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
@MappedSuperclass
public class Linea {
	private String sku;
	private int amount;	
}