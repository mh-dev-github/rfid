package com.mh.api.sync.servicios.purge;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrdenesProduccionPurgeService extends PurgeService {

	@Value("${apes.rest.uri.path.ordenes-de-produccion}")
	private String API_URI_PATH;

	// -------------------------------------------------------------------------------------
	// API URI
	// -------------------------------------------------------------------------------------
	@Override
	protected String getApiURIPath() {
		return API_URI_PATH;
	}

	// -------------------------------------------------------------------------------------
	// SQL
	// -------------------------------------------------------------------------------------
	@Override
	protected String getSQLIdsToPurge() {
		return "SELECT a.id FROM esb.OrdenesProduccion a WHERE a.id <> '' AND a.fecha_ultimo_pull >= CAST('2016-11-01' AS DATE) ORDER BY a.id";
	}

	@Override
	protected String getSQLDelete() {
		return "DELETE FROM esb.OrdenesProduccion WHERE id = :id";
	}
	
	@Override
	protected String getSQLDeleteLineas() {
		return "DELETE b FROM esb.OrdenesProduccion a INNER JOIN  esb.OrdenesProduccion_Lineas b ON b.externalId = a.externalId WHERE a.id = :id";
	}
}
