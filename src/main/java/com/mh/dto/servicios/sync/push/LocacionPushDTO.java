package com.mh.dto.servicios.sync.push;

import com.mh.dto.servicios.sync.base.LocacionBaseDTO;

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