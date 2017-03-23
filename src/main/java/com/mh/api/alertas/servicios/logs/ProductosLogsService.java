package com.mh.api.alertas.servicios.logs;

import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.Producto;
import com.mh.model.esb.domain.msg.ProductoMessage;

@Service
public class ProductosLogsService extends BaseLogsService<Producto, ProductoMessage> {
	protected String getLogTableName() {
		return "msg.Productos";
	}
}