package com.mh.api.alertas.servicios.logs;

import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.SalidaTienda;
import com.mh.model.esb.domain.msg.SalidaTiendaMessage;

@Service
public class SalidasTiendaLogsService extends BaseLogsService<SalidaTienda, SalidaTiendaMessage> {
	@Override
	protected String getLogTableName() {
		return "msg.SalidasTiendas";
	}
}
