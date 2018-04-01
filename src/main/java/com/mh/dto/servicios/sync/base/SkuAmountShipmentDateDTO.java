package com.mh.dto.servicios.sync.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class SkuAmountShipmentDateDTO {
	private String sku;
	private int amount;
	private String expectedShipmentDate;
}
