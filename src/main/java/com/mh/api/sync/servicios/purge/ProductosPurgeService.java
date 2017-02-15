package com.mh.api.sync.servicios.purge;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProductosPurgeService extends PurgeService {

	@Value("${apes.rest.uri.path.productos}")
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
		return "SELECT a.id FROM esb.Productos a WHERE a.id <> '' ORDER BY a.id";
	}

	@Override
	protected String getSQLDelete() {
		return "DELETE FROM esb.Productos WHERE id = :id";
	}

	@Override
	protected String getSQLDeleteLineas() {
		return null;
	}
}
