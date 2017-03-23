package com.mh.api.alertas.servicios;

import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.OrdenProduccion;
import com.mh.model.esb.domain.msg.OrdenProduccionMessage;

@Service
public class OrdenesProduccionAlertasService
		extends BaseAlertasService<OrdenProduccion, OrdenProduccionMessage> {
	@Override
	protected String getLogTableName() {
		return "msg.OrdenesProduccion";
	}
}