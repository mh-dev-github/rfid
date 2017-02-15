package com.mh.api.sync.servicios.push.dto;

import java.util.List;

import com.mh.api.sync.dto.SkuAmountDTO;
import com.mh.api.sync.dto.base.SalidaTiendaBaseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class SalidaTiendaPushDTO extends SalidaTiendaBaseDTO{
    
    private List<SkuAmountDTO> skusAmount;

    @Builder
	public SalidaTiendaPushDTO(String externalId, String id, String sourceId, String destinationId,
			String expectedShipmentDate, List<SkuAmountDTO> skusAmount) {
		super(externalId, id, sourceId, destinationId, expectedShipmentDate);
		this.skusAmount = skusAmount;
	}
}