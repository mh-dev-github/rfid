package com.mh.dto.servicios.sync.check;

import java.util.List;
import java.util.Map;

import com.mh.dto.servicios.sync.base.PedidoBaseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class PedidoCheckDTO extends PedidoBaseDTO {
	private int skuCount;
	private int itemCount;
	private List<Map<String,String>> properties;

	public PedidoCheckDTO(String externalId, String id, String sourceId, String destinationId, List<Map<String,String>> properties) {
		super(externalId, id, sourceId, destinationId);
		this.properties = properties;
	}
}