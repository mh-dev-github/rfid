package com.mh.model.esb.domain.msg;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper=true)
@NoArgsConstructor
@Entity
@Table(catalog = "msg", name = "Productos")
public class ProductoMessage extends BaseMessageEntity {
	private static final long serialVersionUID = 1L;

	@Column(length = 7, nullable = false)
	private String companyPrefix;
	@Column(length = 50, nullable = false)
	private String name;
	@Column(length = 6, nullable = false)
	private String reference;
	@Column(length = 25, nullable = false)
	private String ean;
	@Column(length = 50, nullable = false)
	private String color;
	@Column(length = 50, nullable = false)
	private String codigoColor;
	@Column(length = 50, nullable = false)
	private String talla;
	@Column(length = 50, nullable = false)
	private String tipoProducto;
	@Column(length = 50, nullable = false)
	private String coleccion;
	@Column(length = 50, nullable = false)
	private String grupoProducto;
	@Column(length = 50, nullable = false)
	private String subGrupoProducto;
	@Column(length = 50, nullable = false)
	private String fabricante;
	@Column(length = 50, nullable = false)
	private String temporada;
	@Column(length = 50, nullable = false)
	private String referencia;
	@Column(length = 50, nullable = false)
	private String modelo;
	@Column(length = 50, nullable = false)
	private String genero;
	
	public ProductoMessage(ProductoMessage a) {
		super(a);
		this.companyPrefix = a.getCompanyPrefix();
		this.name = a.getName();
		this.reference = a.getReference();
		this.ean = a.getEan();
		this.color = a.getColor();
		this.codigoColor = a.getCodigoColor();
		this.talla = a.getTalla();
		this.tipoProducto = a.getTipoProducto();
		this.coleccion = a.getColeccion();
		this.grupoProducto = a.getGrupoProducto();
		this.subGrupoProducto = a.getSubGrupoProducto();
		this.fabricante = a.getFabricante();
		this.temporada = a.getTemporada();
		this.referencia = a.getReferencia();
		this.modelo = a.getModelo();
		this.genero = a.getGenero();
	}
}
