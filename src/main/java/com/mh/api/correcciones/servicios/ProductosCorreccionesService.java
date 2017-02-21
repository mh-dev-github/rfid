package com.mh.api.correcciones.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.Producto;
import com.mh.model.esb.domain.msg.ProductoMessage;
import com.mh.model.esb.repo.esb.ProductoRepository;
import com.mh.model.esb.repo.msg.ProductoMessageRepository;

@Service
public class ProductosCorreccionesService extends BaseCorreccionesService<Producto, ProductoMessage> {
	@Autowired
	private ProductoMessageRepository messageRepository;

	@Autowired
	private ProductoRepository entityRepository;

	@Override
	protected JpaRepository<ProductoMessage, Long> getMessageRepository() {
		return messageRepository;
	}

	@Override
	protected JpaRepository<Producto, String> getEntityRepository() {
		return entityRepository;
	}

	@Override
	protected ProductoMessage clonarMensaje(ProductoMessage a) {
		return new ProductoMessage(a);
	}

	@Override
	protected String getLogTableName() {
		return "msg.Productos";
	}
}