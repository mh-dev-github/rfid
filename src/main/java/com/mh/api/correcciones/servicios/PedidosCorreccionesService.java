package com.mh.api.correcciones.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.LineaPedido;
import com.mh.model.esb.domain.esb.Pedido;
import com.mh.model.esb.domain.msg.LineaPedidoMessage;
import com.mh.model.esb.domain.msg.PedidoMessage;
import com.mh.model.esb.repo.esb.PedidoRepository;
import com.mh.model.esb.repo.msg.PedidoMessageRepository;

@Service
public class PedidosCorreccionesService extends BaseCorreccionesService<Pedido, PedidoMessage> {
	@Autowired
	private PedidoMessageRepository messageRepository;

	@Autowired
	private PedidoRepository entityRepository;

	@Override
	protected JpaRepository<PedidoMessage, Long> getMessageRepository() {
		return messageRepository;
	}

	@Override
	protected JpaRepository<Pedido, String> getEntityRepository() {
		return entityRepository;
	}

	@Override
	protected PedidoMessage crearMensaje(Pedido entidad) {
		PedidoMessage mensaje = new PedidoMessage();

		poblarMessageEntity(mensaje, entidad);

		mensaje.setSourceId(entidad.getSourceId());
		mensaje.setDestinationId(entidad.getDestinationId());
		mensaje.setCliente(entidad.getCliente());
		mensaje.setAgencia(entidad.getAgencia());
		mensaje.setBodegaOrigen(entidad.getBodegaOrigen());
		mensaje.setBodegaDestino(entidad.getBodegaDestino());
		
		List<LineaPedido> list = entidad.getLineas();

		for (LineaPedido e : list) {
			LineaPedidoMessage linea = new LineaPedidoMessage(e.getSku(), true, e.getAmount(), e.getExpectedShipmentDate());
			mensaje.getLineas().add(linea);
		}

		return mensaje;
	}

	@Override
	protected PedidoMessage clonarMensaje(PedidoMessage a) {
		return new PedidoMessage(a);
	}
}
