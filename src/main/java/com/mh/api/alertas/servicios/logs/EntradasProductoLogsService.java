package com.mh.api.alertas.servicios;

import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.EntradaProducto;
import com.mh.model.esb.domain.msg.EntradaProductoMessage;

@Service
public class EntradasProductoAlertasService
		extends BaseAlertasService<EntradaProducto, EntradaProductoMessage> {

	@Override
	protected String getLogTableName() {
		return "msg.EntradasProductoTerminado";
	}
}
