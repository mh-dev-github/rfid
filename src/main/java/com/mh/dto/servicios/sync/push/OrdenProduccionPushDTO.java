package com.mh.dto.servicios.sync.push;

import java.util.List;

import com.mh.dto.servicios.sync.base.OrdenProduccionBaseDTO;
import com.mh.dto.servicios.sync.base.SkuAmountDTO;

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