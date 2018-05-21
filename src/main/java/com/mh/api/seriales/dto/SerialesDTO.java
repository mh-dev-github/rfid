package com.mh.api.seriales.dto;

import java.util.List;

import lombok.Data;

/**
 * DTO Cajas. Contiene una lista de seriales.
 * 
 * @author arosorio@gmail.com
 *
 */
@Data
public class SerialesDTO {
	private List<SerialDTO> items;
}
