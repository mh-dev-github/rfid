package com.mh.dto.servicios.sync.base;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class SalidaTiendaBaseDTO extends PayloadBaseDTO{
    private String sourceId;
    private String destinationId;
    private String expectedShipmentDate;
	
    public SalidaTiendaBaseDTO(String externalId, String id, String sourceId, String destinationId,
			String expectedShipmentDate) {
		super(externalId, id);
		this.sourceId = sourceId;
		this.destinationId = destinationId;
		this.expectedShipmentDate = expectedShipmentDate;
	}
}