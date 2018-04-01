package com.mh.dto.servicios.sync.base;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class LocacionBaseDTO extends PayloadBaseDTO{
    private String name;
    private String address;
	private String type;

	public LocacionBaseDTO(String externalId, String id, String name, String address, String type) {
		super(externalId, id);
		this.name = name;
		this.address = address;
		this.type = type;
	}
}
