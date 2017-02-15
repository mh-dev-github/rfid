package com.mh.api.seriales.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class SerialDTO {
	private String ean;
	private List<String> epcs = new ArrayList<>();
}
