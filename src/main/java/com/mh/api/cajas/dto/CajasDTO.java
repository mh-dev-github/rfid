package com.mh.api.cajas.dto;

import lombok.Data;

/**
 * DTO Cajas. Contiene un unico DTO caja. Es solo un envoltorio que es requerido por RFID-APES.  
 * 
 * @author arosorio@gmail.com
 *
 */
@Data
public class CajasDTO {
	private CajaDTO caja;
}