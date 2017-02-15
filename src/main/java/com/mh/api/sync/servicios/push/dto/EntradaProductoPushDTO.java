package com.mh.api.sync.servicios.push.dto;

import java.util.List;

import com.mh.api.sync.dto.SkuAmountDTO;
import com.mh.api.sync.dto.base.EntradaProductoBaseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class EntradaProductoPushDTO extends EntradaProductoBaseDTO {

	private List<SkuAmountDTO> skusAmount;

	@Builder
	public EntradaProductoPushDTO(String externalId, String id, String supplier, String arrivalDate, String concept,
			List<SkuAmountDTO> skusAmount) {
		super(externalId, id, supplier, arrivalDate, concept);
		this.skusAmount = skusAmount;
	}
}