package com.mh.api.sync.servicios.pull.dto;

import com.mh.api.sync.dto.base.ProductoBaseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class ProductoPullDTO extends ProductoBaseDTO {
	private String color;
	private String codigoColor;
	private String talla;
	private String tipoProducto;
	private String coleccion;
	private String grupoProducto;
	private String subGrupoProducto;
	private String fabricante;
	private String temporada;
	private String referencia;
	private String modelo;
	private String genero;

	@Builder
	public ProductoPullDTO(String externalId, String id, String companyPrefix, String name, String reference,
			String ean, String color, String codigoColor, String talla, String tipoProducto, String coleccion,
			String grupoProducto, String subGrupoProducto, String fabricante, String temporada, String referencia,
			String modelo, String genero) {
		super(externalId, id, companyPrefix, name, reference, ean);
		this.color = color;
		this.codigoColor = codigoColor;
		this.talla = talla;
		this.tipoProducto = tipoProducto;
		this.coleccion = coleccion;
		this.grupoProducto = grupoProducto;
		this.subGrupoProducto = subGrupoProducto;
		this.fabricante = fabricante;
		this.temporada = temporada;
		this.referencia = referencia;
		this.modelo = modelo;
		this.genero = genero;
	}
}