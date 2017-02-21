package com.mh.api.correcciones.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.SalidaTienda;
import com.mh.model.esb.domain.msg.SalidaTiendaMessage;
import com.mh.model.esb.repo.esb.SalidaTiendaRepository;
import com.mh.model.esb.repo.msg.SalidaTiendaMessageRepository;

@Service
public class SalidasTiendaCorreccionesService extends BaseCorreccionesService<SalidaTienda, SalidaTiendaMessage> {
	@Autowired
	private SalidaTiendaMessageRepository messageRepository;

	@Autowired
	private SalidaTiendaRepository entityRepository;

	@Override
	protected JpaRepository<SalidaTiendaMessage, Long> getMessageRepository() {
		return messageRepository;
	}

	@Override
	protected JpaRepository<SalidaTienda, String> getEntityRepository() {
		return entityRepository;
	}

	@Override
	protected SalidaTiendaMessage clonarMensaje(SalidaTiendaMessage a) {
		return new SalidaTiendaMessage(a);
	}

	@Override
	protected String getLogTableName() {
		return "msg.SalidasTiendas";
	}
}
