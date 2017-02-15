package com.mh.api.sync.dto.base;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class OrdenProduccionBaseDTO extends PayloadBaseDTO{
    private String supplier;
    private String arrivalDate;

    public OrdenProduccionBaseDTO(String externalId, String id, String supplier, String arrivalDate) {
		super(externalId, id);
		this.supplier = supplier;
		this.arrivalDate = arrivalDate;
	}
}