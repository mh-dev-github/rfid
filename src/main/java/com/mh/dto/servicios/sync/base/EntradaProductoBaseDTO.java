package com.mh.dto.servicios.sync.base;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class EntradaProductoBaseDTO extends PayloadBaseDTO {
    private String supplier;
    private String arrivalDate;
    private String concept;
	
    public EntradaProductoBaseDTO(String externalId, String id, String supplier, String arrivalDate,
			String concept) {
		super(externalId, id);
		this.supplier = supplier;
		this.arrivalDate = arrivalDate;
		this.concept = concept;
	}
}