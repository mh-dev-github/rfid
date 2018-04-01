package com.mh.dto.servicios.sync.check;

import java.util.List;

import com.mh.dto.servicios.sync.base.AttributeDTO;
import com.mh.dto.servicios.sync.base.ProductoBaseDTO;

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