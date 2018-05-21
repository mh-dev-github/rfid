package com.mh.model.esb.domain.esb;

import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * Extensión a la clase lineas, cuando incluyan una fecha esperada de despacho
 * @author arosorio@gmail.com
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true)
@NoArgsConstructor
@Embeddable
public class LineaPedido extends Linea{
	private String expectedShipmentDate;
}