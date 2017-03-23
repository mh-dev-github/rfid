package com.mh.api.alertas.servicios;

import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.Producto;
import com.mh.model.esb.domain.msg.ProductoMessage;

@Service
public class ProductosAlertasService extends BaseAlertasService<Producto, ProductoMessage> {
	protected String getLogTableName() {
		return "msg.Productos";
	}
}