package com.mh.api.cajas.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * DTO Caja. Representa una de las cajas con las cuales se despacha un pedido
 * 
 * @author arosorio@gmail.com
 *
 */
@Data
public class CajaDTO {
	private String externalId;
	private String codigoBodega;
	private String codigoCaja;
	private List<LineaCajaDTO> lineas = new ArrayList<>();
}