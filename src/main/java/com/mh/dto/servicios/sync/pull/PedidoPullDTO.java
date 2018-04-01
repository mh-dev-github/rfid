package com.mh.dto.servicios.sync.pull;

import java.time.LocalDateTime;

import com.mh.dto.servicios.sync.base.PedidoBaseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class PedidoPullDTO extends PedidoBaseDTO {
	private String cliente;
	private String agencia;
	private String bodegaOrigen;
	private String bodegaDestino;

	private String sku;
	private int amount;
	private String expectedShipmentDate;
	private LocalDateTime fechaUltimoPull;	

	@Builder
	public PedidoPullDTO(String externalId, String id, String sourceId, String destinationId, String cliente,
			String agencia, String bodegaOrigen, String bodegaDestino, String sku, int amount,
			String expectedShipmentDate,LocalDateTime fechaUltimoPull) {
		super(externalId, id, sourceId, destinationId);
		this.cliente = cliente;
		this.agencia = agencia;
		this.bodegaOrigen = bodegaOrigen;
		this.bodegaDestino = bodegaDestino;
		this.sku = sku;
		this.amount = amount;
		this.expectedShipmentDate = expectedShipmentDate;
		this.fechaUltimoPull = fechaUltimoPull;
	}
}