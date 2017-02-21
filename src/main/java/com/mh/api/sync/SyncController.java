package com.mh.api.sync;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mh.amqp.RabbitMqConfig;
import com.mh.amqp.dto.RequestDTO;
import com.mh.amqp.dto.RequestType;
import com.mh.model.esb.domain.esb.IntegracionType;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/sync")
@CrossOrigin
@Slf4j
public class SyncController {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@RequestMapping(value = "/all", method = RequestMethod.POST)
	ResponseEntity<?> syncAll() {
		log.debug("Requesting for path /all");
		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.SYNC_ALL));

		HttpHeaders httpHeaders = new HttpHeaders();	
		return new ResponseEntity<>("", httpHeaders, HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/locaciones", method = RequestMethod.POST)
	ResponseEntity<?> syncLocaciones() {
		log.debug("Requesting for path /locaciones");
		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.PUSH, IntegracionType.LOCACIONES));

		HttpHeaders httpHeaders = new HttpHeaders();
		return new ResponseEntity<>("", httpHeaders, HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/productos", method = RequestMethod.POST)
	ResponseEntity<?> syncProductos() {
		log.debug("Requesting add for path /productos");
		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.PUSH, IntegracionType.PRODUCTOS));

		HttpHeaders httpHeaders = new HttpHeaders();
		return new ResponseEntity<>("", httpHeaders, HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/pedidos", method = RequestMethod.POST)
	ResponseEntity<?> syncPedidosCreate() {
		log.debug("Requesting add for path /pedidos POST");
		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.PUSH, IntegracionType.PEDIDOS));

		HttpHeaders httpHeaders = new HttpHeaders();
		return new ResponseEntity<>("", httpHeaders, HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/pedidos", method = RequestMethod.PUT)
	ResponseEntity<?> syncPedidosUpdate(@RequestBody List<String> externalId) {
		log.debug("Requesting add for path /pedidos PUT");
		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.PUSH_UPDATE, IntegracionType.PEDIDOS, externalId));

		HttpHeaders httpHeaders = new HttpHeaders();
		return new ResponseEntity<>("", httpHeaders, HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/salidas", method = RequestMethod.POST)
	ResponseEntity<?> syncSalidas() {
		log.debug("Requesting add for path /salidas");
		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.PUSH, IntegracionType.SALIDAS_TIENDA));

		HttpHeaders httpHeaders = new HttpHeaders();
		return new ResponseEntity<>("", httpHeaders, HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/ordenes", method = RequestMethod.POST)
	ResponseEntity<?> syncOrdenesProduccion() {
		log.debug("Requesting add for path /ordenes");
		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.PUSH, IntegracionType.ORDENES_DE_PRODUCCION));

		HttpHeaders httpHeaders = new HttpHeaders();
		return new ResponseEntity<>("", httpHeaders, HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/entradas", method = RequestMethod.POST)
	ResponseEntity<?> syncEntradasProductosTerminados() {
		log.debug("Requesting add for path /entradas");
		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.PUSH, IntegracionType.ENTRADAS_PT));

		HttpHeaders httpHeaders = new HttpHeaders();
		return new ResponseEntity<>("", httpHeaders, HttpStatus.ACCEPTED);
	}
}
