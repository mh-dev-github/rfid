package com.mh.servicios.ordenesProduccion;

import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.OrdenProduccion;
import com.mh.model.esb.domain.msg.OrdenProduccionMessage;
import com.mh.services.LogsService;

@Service
public class OrdenesProduccionLogsService
		extends LogsService<OrdenProduccion, OrdenProduccionMessage> {
	@Override
	protected String getLogTableName() {
		return "msg.OrdenesProduccion";
	}
}