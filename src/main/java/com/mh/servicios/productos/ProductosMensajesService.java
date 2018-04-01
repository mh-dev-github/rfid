package com.mh.servicios.productos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.Producto;
import com.mh.model.esb.domain.msg.ProductoMessage;
import com.mh.model.esb.repo.esb.ProductoRepository;
import com.mh.model.esb.repo.msg.ProductoMessageRepository;
import com.mh.services.MensajesService;

@Service
public class ProductosMensajesService extends MensajesService<Producto, ProductoMessage> {
	@Autowired
	private ProductoMessageRepository messageRepository;

	@Autowired
	private ProductoRepository repository;

	@Override
	protected JpaRepository<ProductoMessage, Long> getMessageRepository() {
		return messageRepository;
	}

	@Override
	protected JpaRepository<Producto, String> getEntityRepository() {
		return repository;
	}

	@Override
	protected String getLogTableName() {
		return "msg.Productos";
	}	
}
