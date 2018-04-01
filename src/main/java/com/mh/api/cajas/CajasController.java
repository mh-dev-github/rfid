package com.mh.api.cajas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mh.api.cajas.dto.CajaDTO;
import com.mh.api.cajas.dto.CajasDTO;
import com.mh.servicios.cajas.CajasExportService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/cajas")
@Slf4j
public class CajasController {

	@Autowired
	private CajasExportService exportService;

	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?> addCaja(@RequestBody CajasDTO input) {
		log.debug("Requesting for path addCaja:{}", input.toString());
		HttpHeaders httpHeaders = new HttpHeaders();

		CajaDTO caja = input.getCaja();
		try {
			exportService.export(caja);
			return new ResponseEntity<>(null, httpHeaders, HttpStatus.ACCEPTED);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(e.getMessage(), httpHeaders, HttpStatus.BAD_REQUEST);
		}
	}
}
