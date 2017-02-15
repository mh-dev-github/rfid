package com.mh.api.seriales;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mh.api.seriales.dto.SerialDTO;
import com.mh.api.seriales.dto.SerialesDTO;
import com.mh.api.seriales.servicios.SerialesExportService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/seriales")
@Slf4j
public class SerialesController {
	
	@Autowired
	private SerialesExportService exportService;
	
	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?> addSerial(@RequestBody SerialesDTO input) {
		log.debug("Requesting for path addSerial");
		HttpHeaders httpHeaders = new HttpHeaders();

		List<SerialDTO> list = input.getItems();
		try {
			exportService.export(list);
			return new ResponseEntity<>(null, httpHeaders, HttpStatus.ACCEPTED);
		} catch (RuntimeException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), httpHeaders, HttpStatus.BAD_REQUEST);
		}
	}
	
}
