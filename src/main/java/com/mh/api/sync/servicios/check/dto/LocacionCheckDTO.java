package com.mh.api.sync.servicios.check.dto;

import com.mh.api.sync.dto.base.LocacionBaseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class LocacionCheckDTO extends LocacionBaseDTO {

	@Builder
	public LocacionCheckDTO(String externalId, String id, String name, String address, String type) {
		super(externalId, id, name, address, type);
	}
}
