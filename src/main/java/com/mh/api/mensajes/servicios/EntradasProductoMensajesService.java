package com.mh.api.mensajes.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.EntradaProducto;
import com.mh.model.esb.domain.msg.EntradaProductoMessage;
import com.mh.model.esb.repo.esb.EntradaProductoRepository;
import com.mh.model.esb.repo.msg.EntradaProductoMessageRepository;

@Service
public class EntradasProductoMensajesService extends BaseMensajesService<EntradaProducto, EntradaProductoMessage> {
	@Autowired
	private EntradaProductoMessageRepository messageRepository;

	@Autowired
	private EntradaProductoRepository entityRepository;

	@Override
	protected JpaRepository<EntradaProductoMessage, Long> getMessageRepository() {
		return messageRepository;
	}

	@Override
	protected JpaRepository<EntradaProducto, String> getEntityRepository() {
		return entityRepository;
	}

	@Override
	protected String getLogTableName() {
		return "msg.EntradasProductoTerminado";
	}
}