package com.mh.servicios.pedidos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.Pedido;
import com.mh.model.esb.domain.msg.PedidoMessage;
import com.mh.model.esb.repo.esb.PedidoRepository;
import com.mh.model.esb.repo.msg.PedidoMessageRepository;
import com.mh.services.CorreccionesService;

@Service
public class PedidosCorreccionesService extends CorreccionesService<Pedido, PedidoMessage> {
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
	protected PedidoMessage clonarMensaje(PedidoMessage a) {
		return new PedidoMessage(a);
	}

	@Override
	protected String getLogTableName() {
		return "msg.Despachos";
	}
}
