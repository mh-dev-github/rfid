package com.mh.api.alertas.servicios.logs;

import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.EntradaProducto;
import com.mh.model.esb.domain.msg.EntradaProductoMessage;

@Service
public class EntradasProductoLogsService
		extends BaseLogsService<EntradaProducto, EntradaProductoMessage> {

	@Override
	protected String getLogTableName() {
		return "msg.EntradasProductoTerminado";
	}
}
