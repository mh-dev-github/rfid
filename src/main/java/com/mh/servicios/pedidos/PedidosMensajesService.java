package com.mh.servicios.pedidos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.Pedido;
import com.mh.model.esb.domain.msg.PedidoMessage;
import com.mh.model.esb.repo.esb.PedidoRepository;
import com.mh.model.esb.repo.msg.PedidoMessageRepository;
import com.mh.services.MensajesService;

@Service
public class PedidosMensajesService extends MensajesService<Pedido, PedidoMessage> {
	@Autowired
	private PedidoMessageRepository messageRepository;

	@Autowired
	private PedidoRepository repository;

	@Override
	protected JpaRepository<PedidoMessage, Long> getMessageRepository() {
		return messageRepository;
	}

	@Override
	protected JpaRepository<Pedido, String> getEntityRepository() {
		return repository;
	}

	@Override
	protected String getLogTableName() {
		return "msg.Despachos";
	}
}
