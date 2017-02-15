package com.mh.api.sync.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class SkuAmountDTO {
	private String sku;
	private int amount;
}
