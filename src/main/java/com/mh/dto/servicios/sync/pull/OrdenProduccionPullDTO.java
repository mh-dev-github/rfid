package com.mh.dto.servicios.sync.pull;

import com.mh.dto.servicios.sync.base.OrdenProduccionBaseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class OrdenProduccionPullDTO extends OrdenProduccionBaseDTO {
	private String sku;
	private int amount;

    @Builder
	public OrdenProduccionPullDTO(String externalId, String id, String supplier, String arrivalDate, String sku,
			int amount) {
		super(externalId, id, supplier, arrivalDate);
		this.sku = sku;
		this.amount = amount;
	}
}