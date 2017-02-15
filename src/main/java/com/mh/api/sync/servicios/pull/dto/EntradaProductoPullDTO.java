package com.mh.api.sync.servicios.pull.dto;

import com.mh.api.sync.dto.base.EntradaProductoBaseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class EntradaProductoPullDTO extends EntradaProductoBaseDTO {
	private String sku;
	private int amount;

	@Builder
	public EntradaProductoPullDTO(String externalId, String id, String supplier, String arrivalDate, String concept,
			String sku, int amount) {
		super(externalId, id, supplier, arrivalDate, concept);
		this.sku = sku;
		this.amount = amount;
	}
}