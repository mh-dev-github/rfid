package com.mh.dto.servicios.sync.pull;

import java.time.LocalDateTime;

import com.mh.dto.servicios.sync.base.VentaBaseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class VentaPullDTO extends VentaBaseDTO {
	private String barcode;
	private int quantity;

	private String mergeAction;
	private LocalDateTime dataParaTransferencia;
	private LocalDateTime fechaActualizacion;
	
	@Builder
	public VentaPullDTO(String externalId, String id, String storeCode, String saleDate, String barcode, int quantity,
			String mergeAction, LocalDateTime dataParaTransferencia, LocalDateTime fechaActualizacion) {
		super(externalId, id, storeCode, saleDate);
		this.barcode = barcode;
		this.quantity = quantity;
		this.mergeAction = mergeAction;
		this.dataParaTransferencia = dataParaTransferencia;
		this.fechaActualizacion = fechaActualizacion;
	}
}