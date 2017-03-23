package com.mh.api.alertas.servicios;

import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.SalidaTienda;
import com.mh.model.esb.domain.msg.SalidaTiendaMessage;

@Service
public class SalidasTiendaAlertasService extends BaseAlertasService<SalidaTienda, SalidaTiendaMessage> {
	@Override
	protected String getLogTableName() {
		return "msg.SalidasTiendas";
	}
}
