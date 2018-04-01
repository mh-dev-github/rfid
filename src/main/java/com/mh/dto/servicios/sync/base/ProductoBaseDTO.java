package com.mh.dto.servicios.sync.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class ProductoBaseDTO extends PayloadBaseDTO{
    private String companyPrefix;
    private String name;
    private String reference;
    private String ean;

    public ProductoBaseDTO(String externalId, String id, String companyPrefix, String name, String reference,
			String ean) {
		super(externalId, id);
		this.companyPrefix = companyPrefix;
		this.name = name;
		this.reference = reference;
		this.ean = ean;
	}

	@Override
	@JsonProperty("sku")
	public String getExternalId() {
		return super.getExternalId();
	}

	@Override
	@JsonIgnore
	public String getId() {
		return super.getId();
	}
}