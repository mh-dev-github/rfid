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

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper=true)
@NoArgsConstructor
@Entity
@Table(catalog = "esb", name = "Despachos")
public class Pedido extends BaseEntity {
	private static final long serialVersionUID = 1L;

	private String sourceId;
	@Column(length = 50, nullable = false)
	private String destinationId;
	@Column(length = 50, nullable = false)
	private String cliente;
	@Column(length = 50, nullable = false)
	private String agencia;
	@Column(length = 50, nullable = false, name = "BODEGA_ORIGEN")
	private String bodegaOrigen;
	@Column(length = 50, nullable = false, name = "BODEGA_DESTINO")
	private String bodegaDestino;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(catalog = "esb", name = "Despachos_Lineas", joinColumns = @JoinColumn(name = "externalId", nullable = false))
	private List<LineaPedido> lineas;
}
