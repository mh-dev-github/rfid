package com.mh.api.sync.servicios.purge;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LocacionesPurgeService extends PurgeService {

	@Value("${apes.rest.uri.path.locaciones}")
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
		return "SELECT a.id FROM esb.Locaciones a WHERE a.externalId <> a.name ORDER BY a.id";
	}

	@Override
	protected String getSQLDelete() {
		return "DELETE FROM esb.Locaciones WHERE id = :id";
	}
	
	@Override
	protected String getSQLDeleteLineas() {
		return null;
	}	
}
