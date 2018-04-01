package com.mh.dto.servicios.sync.push;

import java.util.List;

import com.mh.dto.servicios.sync.base.VentaBaseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class VentaPushDTO extends VentaBaseDTO{
    
	private List<Line> lines;

	@Builder
	public VentaPushDTO(String externalId, String id, String storeCode, String saleDate,List<Line> lines) {
		super(externalId, id, storeCode, saleDate);
		this.lines = lines;
	}
	
	@Getter
	@ToString
	@AllArgsConstructor	
	public static class Line {
		private String barcode;
		private int quantity;
	}
}