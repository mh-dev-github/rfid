package com.mh.model.esb.domain.msg;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
 * Mensaje Salida Tienda
 * 
 * @author arosorio@gmail.com
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Table(catalog = "msg", name = "SalidasTiendas")
public class SalidaTiendaMessage extends BaseMessageEntity {
	private static final long serialVersionUID = 1L;

	@Column(length = 50, nullable = false)
	private String sourceId;
	@Column(length = 50, nullable = false)
	private String destinationId;
	@Column(length = 10, nullable = false)
	private String expectedShipmentDate;
	@Column(length = 50, nullable = false, name = "BODEGA_ORIGEN")
	private String bodegaOrigen;
	@Column(length = 50, nullable = false, name = "BODEGA_DESTINO")
	private String bodegaDestino;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(catalog = "msg", name = "SalidasTiendas_Lineas", joinColumns = @JoinColumn(name = "mid", nullable = false))
	private List<LineaMessage> lineas = new ArrayList<>();

	public SalidaTiendaMessage(SalidaTiendaMessage a) {
		super(a);
		this.sourceId = a.getSourceId();
		this.destinationId = a.getDestinationId();
		this.expectedShipmentDate = a.getExpectedShipmentDate();
		this.bodegaOrigen = a.getBodegaOrigen();
		this.bodegaDestino = a.getBodegaDestino();
		// @formatter:off
		this.lineas = a.getLineas()
				.stream().map(b -> 
					new LineaMessage(b.getSku(),b.isMatchSku(), b.getAmount()))
				.collect(Collectors.toList());
		// @formatter:on
	}
}
