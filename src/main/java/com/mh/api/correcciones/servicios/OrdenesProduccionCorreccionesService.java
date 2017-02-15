package com.mh.api.correcciones.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.Linea;
import com.mh.model.esb.domain.esb.OrdenProduccion;
import com.mh.model.esb.domain.msg.LineaMessage;
import com.mh.model.esb.domain.msg.OrdenProduccionMessage;
import com.mh.model.esb.repo.esb.OrdenProduccionRepository;
import com.mh.model.esb.repo.msg.OrdenProduccionMessageRepository;

@Service
public class OrdenesProduccionCorreccionesService
		extends BaseCorreccionesService<OrdenProduccion, OrdenProduccionMessage> {
	@Autowired
	private OrdenProduccionMessageRepository messageRepository;

	@Autowired
	private OrdenProduccionRepository entityRepository;

	@Override
	protected JpaRepository<OrdenProduccionMessage, Long> getMessageRepository() {
		return messageRepository;
	}

	@Override
	protected JpaRepository<OrdenProduccion, String> getEntityRepository() {
		return entityRepository;
	}

	@Override
	protected OrdenProduccionMessage crearMensaje(OrdenProduccion entidad) {
		OrdenProduccionMessage mensaje = new OrdenProduccionMessage();

		poblarMessageEntity(mensaje, entidad);

		mensaje.setSupplier(entidad.getSupplier());
		mensaje.setArrivalDate(entidad.getArrivalDate());
		
		List<Linea> list = entidad.getLineas();

		for (Linea e : list) {
			LineaMessage linea = new LineaMessage(e.getSku(), true, e.getAmount());
			mensaje.getLineas().add(linea);
		}

		return mensaje;
	}

	@Override
	protected OrdenProduccionMessage clonarMensaje(OrdenProduccionMessage a) {
		return new OrdenProduccionMessage(a);
	}
}