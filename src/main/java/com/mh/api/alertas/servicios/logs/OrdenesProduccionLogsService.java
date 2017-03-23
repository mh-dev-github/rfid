package com.mh.api.alertas.servicios.logs;

import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.OrdenProduccion;
import com.mh.model.esb.domain.msg.OrdenProduccionMessage;

@Service
public class OrdenesProduccionLogsService
		extends BaseLogsService<OrdenProduccion, OrdenProduccionMessage> {
	@Override
	protected String getLogTableName() {
		return "msg.OrdenesProduccion";
	}
}