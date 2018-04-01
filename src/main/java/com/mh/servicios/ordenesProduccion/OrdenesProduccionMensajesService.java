package com.mh.servicios.ordenesProduccion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.OrdenProduccion;
import com.mh.model.esb.domain.msg.OrdenProduccionMessage;
import com.mh.model.esb.repo.esb.OrdenProduccionRepository;
import com.mh.model.esb.repo.msg.OrdenProduccionMessageRepository;
import com.mh.services.MensajesService;

@Service
public class OrdenesProduccionMensajesService extends MensajesService<OrdenProduccion, OrdenProduccionMessage> {
	@Autowired
	private OrdenProduccionMessageRepository messageRepository;

	@Autowired
	private OrdenProduccionRepository repository;

	@Override
	protected JpaRepository<OrdenProduccionMessage, Long> getMessageRepository() {
		return messageRepository;
	}

	@Override
	protected JpaRepository<OrdenProduccion, String> getEntityRepository() {
		return repository;
	}

	@Override
	protected String getLogTableName() {
		return "msg.OrdenesProduccion";
	}
}
