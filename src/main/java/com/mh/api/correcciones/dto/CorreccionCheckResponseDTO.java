package com.mh.api.correcciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CorreccionCheckResponseDTO {
	private String externalId;
	private boolean habilitado;
	private String causa;
}
