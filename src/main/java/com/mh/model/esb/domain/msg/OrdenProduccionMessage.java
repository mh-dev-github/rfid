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
 * Mensaje Ordenes Produccion
 * 
 * @author arosorio@gmail.com
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Table(catalog = "msg", name = "OrdenesProduccion")
public class OrdenProduccionMessage extends BaseMessageEntity {
	private static final long serialVersionUID = 1L;

	@Column(length = 50, nullable = false)
	private String supplier;
	@Column(length = 10, nullable = false)
	private String arrivalDate;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(catalog = "msg", name = "OrdenesProduccion_Lineas", joinColumns = @JoinColumn(name = "mid", nullable = false))
	private List<LineaMessage> lineas = new ArrayList<>();

	public OrdenProduccionMessage(OrdenProduccionMessage a) {
		super(a);
		this.supplier = a.getSupplier();
		this.arrivalDate = a.getArrivalDate();
		// @formatter:off
		this.lineas = a.getLineas()
				.stream().map(b -> 
					new LineaMessage(b.getSku(),b.isMatchSku(), b.getAmount()))
				.collect(Collectors.toList());
		// @formatter:on
	}
}
