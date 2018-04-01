package com.mh.servicios.salidasTienda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.SalidaTienda;
import com.mh.model.esb.domain.msg.SalidaTiendaMessage;
import com.mh.model.esb.repo.esb.SalidaTiendaRepository;
import com.mh.model.esb.repo.msg.SalidaTiendaMessageRepository;
import com.mh.services.MensajesService;

@Service
public class SalidasTiendaMensajesService extends MensajesService<SalidaTienda, SalidaTiendaMessage> {
	@Autowired
	private SalidaTiendaMessageRepository messageRepository;

	@Autowired
	private SalidaTiendaRepository repository;

	@Override
	protected JpaRepository<SalidaTiendaMessage, Long> getMessageRepository() {
		return messageRepository;
	}

	@Override
	protected JpaRepository<SalidaTienda, String> getEntityRepository() {
		return repository;
	}

	@Override
	protected String getLogTableName() {
		return "msg.SalidasTiendas";
	}	
}
