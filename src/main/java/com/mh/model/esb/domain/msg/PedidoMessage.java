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
 * Mesaje Pedido
 * 
 * @author arosorio@gmail.com
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper=true)
@NoArgsConstructor
@Entity
@Table(catalog = "msg", name = "Despachos")
public class PedidoMessage extends BaseMessageEntity {
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
	@CollectionTable(catalog = "msg", name = "Despachos_Lineas", joinColumns = @JoinColumn(name = "mid", nullable = false))
	private List<LineaPedidoMessage> lineas = new ArrayList<>();

	public PedidoMessage(PedidoMessage a) {
		super(a);
		this.sourceId = a.getSourceId();
		this.destinationId = a.getDestinationId();
		this.cliente = a.getCliente();
		this.agencia = a.getAgencia();
		this.bodegaOrigen = a.getBodegaOrigen();
		this.bodegaDestino = a.getBodegaDestino();
		// @formatter:off
		this.lineas = a.getLineas()
				.stream().map(b -> 
					new LineaPedidoMessage(b.getSku(),b.isMatchSku(), b.getAmount(), b.getExpectedShipmentDate()))
				.collect(Collectors.toList());
		// @formatter:on
	}
}
