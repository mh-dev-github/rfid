package com.mh.servicios.locaciones;

import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.Locacion;
import com.mh.model.esb.domain.msg.LocacionMessage;
import com.mh.services.LogsService;

@Service
public class LocacionesLogsService extends LogsService<Locacion, LocacionMessage> {
	@Override
	protected String getLogTableName() {
		return "msg.Locaciones";
	}
}
