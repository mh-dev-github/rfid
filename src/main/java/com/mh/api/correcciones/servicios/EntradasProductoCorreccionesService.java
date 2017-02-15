package com.mh.api.correcciones.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.EntradaProducto;
import com.mh.model.esb.domain.esb.Linea;
import com.mh.model.esb.domain.msg.EntradaProductoMessage;
import com.mh.model.esb.domain.msg.LineaMessage;
import com.mh.model.esb.repo.esb.EntradaProductoRepository;
import com.mh.model.esb.repo.msg.EntradaProductoMessageRepository;

@Service
public class EntradasProductoCorreccionesService
		extends BaseCorreccionesService<EntradaProducto, EntradaProductoMessage> {
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
	protected EntradaProductoMessage crearMensaje(EntradaProducto entidad) {
		EntradaProductoMessage mensaje = new EntradaProductoMessage();

		poblarMessageEntity(mensaje, entidad);

		mensaje.setSupplier(entidad.getSupplier());
		mensaje.setArrivalDate(entidad.getArrivalDate());
		mensaje.setConcept(entidad.getConcept());

		List<Linea> list = entidad.getLineas();

		for (Linea e : list) {
			LineaMessage linea = new LineaMessage(e.getSku(), true, e.getAmount());
			mensaje.getLineas().add(linea);
		}

		return mensaje;
	}

	@Override
	protected EntradaProductoMessage clonarMensaje(EntradaProductoMessage a) {
		return new EntradaProductoMessage(a);
	}
}
