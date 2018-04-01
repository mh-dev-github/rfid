package com.mh.servicios.salidasTienda;

import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.SalidaTienda;
import com.mh.model.esb.domain.msg.SalidaTiendaMessage;
import com.mh.services.LogsService;

@Service
public class SalidasTiendaLogsService extends LogsService<SalidaTienda, SalidaTiendaMessage> {
	@Override
	protected String getLogTableName() {
		return "msg.SalidasTiendas";
	}
}
