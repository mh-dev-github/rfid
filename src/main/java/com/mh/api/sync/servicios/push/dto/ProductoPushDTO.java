package com.mh.api.sync.servicios.push.dto;

import java.util.List;

import com.mh.api.sync.dto.AttributeDTO;
import com.mh.api.sync.dto.base.ProductoBaseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class ProductoPushDTO extends ProductoBaseDTO {

	private List<AttributeDTO> attributes;

	@Builder
	public ProductoPushDTO(String externalId, String id, String companyPrefix, String name, String reference,
			String ean, List<AttributeDTO> attributes) {
		super(externalId, id, companyPrefix, name, reference, ean);
		this.attributes = attributes;
	}
}