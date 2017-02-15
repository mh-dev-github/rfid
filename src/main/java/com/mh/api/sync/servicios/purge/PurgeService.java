package com.mh.api.sync.servicios.purge;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.web.client.RestClientException;

import com.mh.api.sync.servicios.BaseSyncService;

public abstract class PurgeService extends BaseSyncService<String> {
	
	@Value("${apes.rest.uri.base}")
	private String API_URI_BASE;

	@Value("${apes.rest.authorization}")
	private String API_AUTHORIZATION;
	
	@Qualifier("erpJdbcTemplate")
	@Autowired
	protected NamedParameterJdbcTemplate erpJdbcTemplate;

	protected PurgeService() {
		super();
	}
	
	public void purge() {
		
		List<String> ids = getIdsToPurge();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(HTTP_HEADER_AUTHORIZATION, getApiTokenAuthorization());
	
		HttpEntity<Object> requestEntity = new HttpEntity<>(null, headers);

		for (String id : ids) {
			if ("".equals(id))
				continue;
			
			try {
				getRestTemplate().exchange(getApiURI() + "/" + id, HttpMethod.DELETE, requestEntity, Object.class);
				deleteRecord(id);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
		}
	}
	
	// -------------------------------------------------------------------------------------
	// API URI
	// -------------------------------------------------------------------------------------
	
	@Override
	protected String getApiURIBase() {
		return API_URI_BASE;
	}

	@Override
	protected String getApiTokenAuthorization() {
		return API_AUTHORIZATION;
	}

	@Override
	protected Class<String> getResponseEntityClass() {
		return String.class;
	}
	
	// -------------------------------------------------------------------------------------
	// SQL
	// -------------------------------------------------------------------------------------
	protected List<String> getIdsToPurge() {
		List<String> ids;
	
		String sql = getSQLIdsToPurge();
		ids = esbJdbcTemplate.queryForList(sql, (SqlParameterSource) null, String.class);
	
		return ids;
	}

	protected boolean deleteRecord(String id) {
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("id", id);
		
		{
			String sql = getSQLDeleteLineas();
			
			if(sql != null){
				esbJdbcTemplate.update(sql, paramSource);
			}
		}

		{
			String sql = getSQLDelete();
			
			esbJdbcTemplate.update(sql, paramSource);
		}
		
		return true;
	}

	abstract protected String getSQLIdsToPurge();

	abstract protected String getSQLDelete();
	
	abstract protected String getSQLDeleteLineas();

}
