package com.mh.model.esb.domain.msg;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class LineaPedidoMessage extends LineaMessage {
	private String expectedShipmentDate;

	public LineaPedidoMessage(String sku, boolean matchSku, int amount, String expectedShipmentDate) {
		super(sku, matchSku, amount);
		this.expectedShipmentDate = expectedShipmentDate;
	}

}