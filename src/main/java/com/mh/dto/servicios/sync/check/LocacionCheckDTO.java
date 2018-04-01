package com.mh.dto.servicios.sync.check;

import com.mh.dto.servicios.sync.base.LocacionBaseDTO;

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
