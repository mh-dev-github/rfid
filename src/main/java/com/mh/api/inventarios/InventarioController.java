package com.mh.api.inventarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mh.servicios.inventarios.InventariosExportService;
import com.mh.servicios.inventarios.InventariosExportService.InventarioDTO;
import com.mh.servicios.inventarios.InventariosExportService.InventariosDTO;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/inventarios")
@Slf4j
public class InventarioController {

	@Autowired
	private InventariosExportService exportService;

	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?> addInventario(@RequestBody InventariosDTO input) {
		log.debug("Requesting for path addInventario:{}", input.toString());
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
