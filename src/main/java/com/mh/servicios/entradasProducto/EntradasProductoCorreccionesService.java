package com.mh.servicios.entradasProducto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.EntradaProducto;
import com.mh.model.esb.domain.msg.EntradaProductoMessage;
import com.mh.model.esb.repo.esb.EntradaProductoRepository;
import com.mh.model.esb.repo.msg.EntradaProductoMessageRepository;
import com.mh.services.CorreccionesService;

@Service
public class EntradasProductoCorreccionesService
		extends CorreccionesService<EntradaProducto, EntradaProductoMessage> {
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
	protected EntradaProductoMessage clonarMensaje(EntradaProductoMessage a) {
		return new EntradaProductoMessage(a);
	}

	@Override
	protected String getLogTableName() {
		return "msg.EntradasProductoTerminado";
	}
}
