package com.mh.api.sync.servicios.push.dto;

import com.mh.api.sync.dto.base.LocacionBaseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class LocacionPushDTO extends LocacionBaseDTO {

	@Builder
	public LocacionPushDTO(String externalId, String id, String name, String address, String type) {
		super(externalId, id, name, address, type);
	}
}