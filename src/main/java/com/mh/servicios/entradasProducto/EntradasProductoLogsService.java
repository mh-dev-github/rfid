package com.mh.servicios.entradasProducto;

import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.EntradaProducto;
import com.mh.model.esb.domain.msg.EntradaProductoMessage;
import com.mh.services.LogsService;

@Service
public class EntradasProductoLogsService
		extends LogsService<EntradaProducto, EntradaProductoMessage> {

	@Override
	protected String getLogTableName() {
		return "msg.EntradasProductoTerminado";
	}
}
