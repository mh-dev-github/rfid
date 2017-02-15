package com.mh.api.sync.servicios.check.dto;

import com.mh.api.sync.dto.base.OrdenProduccionBaseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class OrdenProduccionCheckDTO extends OrdenProduccionBaseDTO {

	private int skuCount;
	private int itemCount;

	@Builder
	public OrdenProduccionCheckDTO(String externalId, String id, String supplier, String arrivalDate) {
		super(externalId, id, supplier, arrivalDate);
	}
}