package com.mh.dto.servicios.sync.pull;

import com.mh.dto.servicios.sync.base.SalidaTiendaBaseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class SalidaTiendaPullDTO extends SalidaTiendaBaseDTO {
	private String sku;
	private int amount;
	private String bodegaOrigen;
	private String bodegaDestino;

    @Builder
	public SalidaTiendaPullDTO(String externalId, String id, String sourceId, String destinationId,
			String expectedShipmentDate, String sku, int amount, String bodegaOrigen, String bodegaDestino) {
		super(externalId, id, sourceId, destinationId, expectedShipmentDate);
		this.sku = sku;
		this.amount = amount;
		this.bodegaOrigen = bodegaOrigen;
		this.bodegaDestino = bodegaDestino;
	}
}