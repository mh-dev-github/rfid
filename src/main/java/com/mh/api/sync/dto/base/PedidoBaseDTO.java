package com.mh.api.sync.dto.base;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class PedidoBaseDTO extends PayloadBaseDTO{
	private String sourceId;
	private String destinationId;
	
	public PedidoBaseDTO(String externalId, String id, String sourceId, String destinationId) {
		super(externalId, id);
		this.sourceId = sourceId;
		this.destinationId = destinationId;
	}
}