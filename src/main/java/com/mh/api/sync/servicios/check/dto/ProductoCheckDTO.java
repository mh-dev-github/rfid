package com.mh.api.sync.servicios.check.dto;

import java.util.List;

import com.mh.api.sync.dto.AttributeDTO;
import com.mh.api.sync.dto.base.ProductoBaseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class ProductoCheckDTO extends ProductoBaseDTO {

    private List<AttributeDTO> attributes;

	public ProductoCheckDTO(String externalId, String id, String companyPrefix, String name, String reference,
			String ean, List<AttributeDTO> attributes) {
		super(externalId, id, companyPrefix, name, reference, ean);
		this.attributes = attributes;
	}
}