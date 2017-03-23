package com.mh.api.alertas.servicios.logs;

import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.Locacion;
import com.mh.model.esb.domain.msg.LocacionMessage;

@Service
public class LocacionesLogsService extends BaseLogsService<Locacion, LocacionMessage> {
	@Override
	protected String getLogTableName() {
		return "msg.Locaciones";
	}
}
