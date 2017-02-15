package com.mh.api.cajas.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CajaDTO {
	private String externalId;
	private String codigoBodega;
	private String codigoCaja;
	private List<LineaCajaDTO> lineas = new ArrayList<>();
}
