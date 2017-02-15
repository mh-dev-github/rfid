package com.mh.api.sync.servicios.check.dto;

import com.mh.api.sync.dto.base.EntradaProductoBaseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class EntradaProductoCheckDTO extends EntradaProductoBaseDTO {
	
	private int skuCount;
	private int itemCount;

	public EntradaProductoCheckDTO(String externalId, String id, String supplier, String arrivalDate, String concept) {
		super(externalId, id, supplier, arrivalDate, concept);
	}
}