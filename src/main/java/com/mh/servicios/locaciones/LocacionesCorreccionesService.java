package com.mh.servicios.locaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.Locacion;
import com.mh.model.esb.domain.msg.LocacionMessage;
import com.mh.model.esb.repo.esb.LocacionRepository;
import com.mh.model.esb.repo.msg.LocacionMessageRepository;
import com.mh.services.CorreccionesService;

@Service
public class LocacionesCorreccionesService extends CorreccionesService<Locacion, LocacionMessage> {
	@Autowired
	private LocacionMessageRepository messageRepository;

	@Autowired
	private LocacionRepository entityRepository;

	@Override
	protected JpaRepository<LocacionMessage, Long> getMessageRepository() {
		return messageRepository;
	}

	@Override
	protected JpaRepository<Locacion, String> getEntityRepository() {
		return entityRepository;
	}

	@Override
	protected LocacionMessage clonarMensaje(LocacionMessage a) {
		return new LocacionMessage(a);
	}

	@Override
	protected String getLogTableName() {
		return "msg.Locaciones";
	}
}
