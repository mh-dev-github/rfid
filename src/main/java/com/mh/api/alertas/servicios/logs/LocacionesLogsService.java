package com.mh.api.alertas.servicios;

import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.Locacion;
import com.mh.model.esb.domain.msg.LocacionMessage;

@Service
public class LocacionesAlertasService extends BaseAlertasService<Locacion, LocacionMessage> {
	@Override
	protected String getLogTableName() {
		return "msg.Locaciones";
	}
}
