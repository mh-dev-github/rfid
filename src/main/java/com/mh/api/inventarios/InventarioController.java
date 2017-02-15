package com.mh.api.inventarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mh.api.inventarios.dto.InventarioDTO;
import com.mh.api.inventarios.dto.InventariosDTO;
import com.mh.api.inventarios.servicios.InventariosExportService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/inventarios")
@Slf4j
public class InventarioController {

	@Autowired
	private InventariosExportService exportService;

	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?> addInventario(@RequestBody InventariosDTO input) {
		log.debug("Requesting for path addInventario");
		HttpHeaders httpHeaders = new HttpHeaders();

		InventarioDTO inventario = input.getInventario();
		try {
			exportService.export(inventario);
			return new ResponseEntity<>(null, httpHeaders, HttpStatus.ACCEPTED);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(e.getMessage(), httpHeaders, HttpStatus.BAD_REQUEST);
		}
	}
}
