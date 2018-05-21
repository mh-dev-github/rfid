package com.mh.amqp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mh.amqp.handlers.AbstractHandler;
import com.mh.dto.amqp.RequestDTO;
import com.mh.notificaciones.NotificacionesLogHandler;
import com.mh.servicios.entradasProducto.EntradasProductoHandler;
import com.mh.servicios.locaciones.LocacionesHandler;
import com.mh.servicios.ordenesProduccion.OrdenesProduccionHandler;
import com.mh.servicios.pedidos.PedidosHandler;
import com.mh.servicios.productos.ProductosHandler;
import com.mh.servicios.salidasTienda.SalidasTiendasHandler;
import com.mh.servicios.ventas.VentasHandler;
import com.mh.tasks.SyncAllHandler;

import lombok.extern.slf4j.Slf4j;


/**
 * Componente que consume los mensajes en la cola RabbitMQ. Responsable de direccionar cada mensaje al componente respectivo.
 * 
 * @author arosorio@gmail.com
 *
 */
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
	private VentasHandler ventasHandler;

	@Autowired
	private NotificacionesLogHandler alertasLogHandler;

	public void receiveMessage(RequestDTO request) {
		log.info("Procesando solicitud {}", request);

		AbstractHandler<RequestDTO> root = getRootChain();
		if (root != null) {
			root.receiveRequest(request);
		}
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
			rootChain.setNextHandler(ventasHandler);
			rootChain.setNextHandler(alertasLogHandler);
		}

		return rootChain;
	}
}