package com.mh.dto.servicios.sync.pull;

import com.mh.dto.servicios.sync.base.EntradaProductoBaseDTO;

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