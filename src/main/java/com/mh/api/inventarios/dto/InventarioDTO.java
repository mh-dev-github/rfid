package com.mh.api.inventarios.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class InventarioDTO {
	private String codigoInventario;
	private String codigoBodega;
	private List<LineaInventarioDTO> lineas = new ArrayList<>();
}