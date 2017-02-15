package com.mh.model.esb.domain.esb;

import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true)
@NoArgsConstructor
@Embeddable
public class LineaPedido extends Linea{
	private String expectedShipmentDate;
}