package com.mh.api.alertas.servicios.logs;

import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.Pedido;
import com.mh.model.esb.domain.msg.PedidoMessage;

@Service
public class PedidosLogsService extends BaseLogsService<Pedido, PedidoMessage> {
	@Override
	protected String getLogTableName() {
		return "msg.Despachos";
	}
}
