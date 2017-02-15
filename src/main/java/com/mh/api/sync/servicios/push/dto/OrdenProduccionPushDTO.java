package com.mh.api.sync.servicios.push.dto;

import java.util.List;

import com.mh.api.sync.dto.SkuAmountDTO;
import com.mh.api.sync.dto.base.OrdenProduccionBaseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class OrdenProduccionPushDTO extends OrdenProduccionBaseDTO{
    
    private List<SkuAmountDTO> skusAmount;

    @Builder
    public OrdenProduccionPushDTO(String externalId, String id, String supplier, String arrivalDate,
			List<SkuAmountDTO> skusAmount) {
		super(externalId, id, supplier, arrivalDate);
		this.skusAmount = skusAmount;
	}
}