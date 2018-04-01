package com.mh.servicios.productos;

import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.Producto;
import com.mh.model.esb.domain.msg.ProductoMessage;
import com.mh.services.LogsService;

@Service
public class ProductosLogsService extends LogsService<Producto, ProductoMessage> {
	protected String getLogTableName() {
		return "msg.Productos";
	}
}