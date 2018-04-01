package com.mh.servicios.pedidos;

import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.Pedido;
import com.mh.model.esb.domain.msg.PedidoMessage;
import com.mh.services.LogsService;

@Service
public class PedidosLogsService extends LogsService<Pedido, PedidoMessage> {
	@Override
	protected String getLogTableName() {
		return "msg.Despachos";
	}
}
