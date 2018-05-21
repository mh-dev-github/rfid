package com.mh.model.esb.domain.esb;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * Entidad sincronizable Entradas Producto Terminado
 * 
 * @author arosorio@gmail.com
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper=true)
@NoArgsConstructor
@Entity
@Table(catalog = "esb", name = "EntradasProductoTerminado")
public class EntradaProducto extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Column(length = 50, nullable = false)
	private String supplier;
	@Column(length = 10, nullable = false)
	private String arrivalDate;
	@Column(length = 50, nullable = false)
	private String concept;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(catalog = "esb", name = "EntradasProductoTerminado_Lineas", joinColumns = @JoinColumn(name = "externalId", nullable = false))
	private List<Linea> lineas;
}
