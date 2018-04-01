package com.mh.dto.servicios.sync.base;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class VentaBaseDTO extends PayloadBaseDTO {
	private String storeCode;
	private String saleDate;

	public VentaBaseDTO(String externalId, String id, String storeCode, String saleDate) {
		super(externalId, id);
		this.storeCode = storeCode;
		this.saleDate = saleDate;
	}
}
