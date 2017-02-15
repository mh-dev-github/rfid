package com.mh.api.sync.servicios.pull.dto;

import com.mh.api.sync.dto.base.LocacionBaseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class LocacionPullDTO extends LocacionBaseDTO {

	@Builder
	public LocacionPullDTO(String externalId, String id, String name, String address, String type) {
		super(externalId, id, name, address, type);
	}
}
