package com.mh.amqp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mh.amqp.dto.RequestDTO;
import com.mh.amqp.handlers.ReportesLogHandler;
import com.mh.amqp.handlers.EntradasProductoHandler;
import com.mh.amqp.handlers.LocacionesHandler;
import com.mh.amqp.handlers.OrdenesProduccionHandler;
import com.mh.amqp.handlers.PedidosHandler;
import com.mh.amqp.handlers.ProductosHandler;
import com.mh.amqp.handlers.SalidasTiendasHandler;
import com.mh.amqp.handlers.SyncAllHandler;
import com.mh.core.patterns.AbstractHandler;

import lombok.extern.slf4j.Slf4j;

@Component("receiver")
@Slf4j
public class Receiver {

	public static final String RECEIVE_METHOD_NAME = "receiveMessage";

	private AbstractHandler<RequestDTO> rootChain = null;

	@Autowired
	private SyncAllHandler syncAllHandler;

	@Autowired
	private LocacionesHandler locacionesHandler;
	@Autowired
	private ProductosHandler productosHandler;
	@Autowired
	private PedidosHandler pedidosHandler;
	@Autowired
	private OrdenesProduccionHandler ordenesProduccionHandler;
	@Autowired
	private EntradasProductoHandler entradasProductosTerminadosHandler;
	@Autowired
	private SalidasTiendasHandler salidasTiendasHandler;

	@Autowired
	private ReportesLogHandler alertasLogHandler;

	
	public void receiveMessage(RequestDTO request) {
		log.info("Procesando solicitud {}", request);

		AbstractHandler<RequestDTO> root = getRootChain();
		root.receiveRequest(request);
	}

	private AbstractHandler<RequestDTO> getRootChain() {
		if (rootChain == null) {
			rootChain = syncAllHandler;

			rootChain.setNextHandler(locacionesHandler);
			rootChain.setNextHandler(productosHandler);
			rootChain.setNextHandler(pedidosHandler);
			rootChain.setNextHandler(salidasTiendasHandler);
			rootChain.setNextHandler(ordenesProduccionHandler);
			rootChain.setNextHandler(entradasProductosTerminadosHandler);
			rootChain.setNextHandler(alertasLogHandler);
		}

		return rootChain;
	}
}
