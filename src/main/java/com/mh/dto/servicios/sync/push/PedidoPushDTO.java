package com.mh.dto.servicios.sync.push;

import java.util.List;
import java.util.Map;

import com.mh.dto.servicios.sync.base.PedidoBaseDTO;
import com.mh.dto.servicios.sync.base.SkuAmountShipmentDateDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class PedidoPushDTO extends PedidoBaseDTO{
    
	private List<SkuAmountShipmentDateDTO> skusAmount;
    private List<Map<String,String>> properties;

    @Builder
	public PedidoPushDTO(String externalId, String id, String sourceId, String destinationId, List<Map<String,String>> properties,List<SkuAmountShipmentDateDTO> skusAmount) {
		super(externalId, id, sourceId, destinationId);
		this.properties = properties;
		this.skusAmount = skusAmount;
	}
}