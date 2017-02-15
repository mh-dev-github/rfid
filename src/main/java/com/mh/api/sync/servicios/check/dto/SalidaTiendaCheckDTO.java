package com.mh.api.sync.servicios.check.dto;

import com.mh.api.sync.dto.base.SalidaTiendaBaseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class SalidaTiendaCheckDTO extends SalidaTiendaBaseDTO{
	
	private int skuCount;
	private int itemCount;
	
	@Builder
	public SalidaTiendaCheckDTO(String externalId, String id, String sourceId, String destinationId,
			String expectedShipmentDate) {
		super(externalId, id, sourceId, destinationId, expectedShipmentDate);
	}
}