package com.mh.dto.servicios.sync.pull;

import com.mh.dto.servicios.sync.base.LocacionBaseDTO;

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
