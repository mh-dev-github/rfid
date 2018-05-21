package com.mh.api.seriales.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * DTO Cajas. Contiene una lista de seriales asociados a un código ean.
 * 
 * @author arosorio@gmail.com
 *
 */
@Data
public class SerialDTO {
	private String ean;
	private List<String> epcs = new ArrayList<>();
}
