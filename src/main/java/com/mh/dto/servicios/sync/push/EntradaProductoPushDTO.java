package com.mh.dto.servicios.sync.push;

import java.util.List;

import com.mh.dto.servicios.sync.base.EntradaProductoBaseDTO;
import com.mh.dto.servicios.sync.base.SkuAmountDTO;

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