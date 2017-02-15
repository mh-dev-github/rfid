package com.mh.api.correcciones.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.Linea;
import com.mh.model.esb.domain.esb.SalidaTienda;
import com.mh.model.esb.domain.msg.LineaMessage;
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
	protected SalidaTiendaMessage crearMensaje(SalidaTienda entidad) {
		SalidaTiendaMessage mensaje = new SalidaTiendaMessage();

		poblarMessageEntity(mensaje, entidad);

		mensaje.setSourceId(entidad.getSourceId());
		mensaje.setDestinationId(entidad.getDestinationId());
		mensaje.setExpectedShipmentDate(entidad.getExpectedShipmentDate());
		mensaje.setBodegaOrigen(entidad.getBodegaOrigen());
		mensaje.setBodegaDestino(entidad.getBodegaDestino());

		List<Linea> list = entidad.getLineas();

		for (Linea e : list) {
			LineaMessage linea = new LineaMessage(e.getSku(), true, e.getAmount());
			mensaje.getLineas().add(linea);
		}

		return mensaje;
	}

	@Override
	protected SalidaTiendaMessage clonarMensaje(SalidaTiendaMessage a) {
		return new SalidaTiendaMessage(a);
	}
}
