package com.mh.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.client.RestTemplate;


/**
 * Clase base de todos los servicios de sincronización
 * 
 * @author arosorio@gmail.com
 *
 * @param <T>
 */
public abstract class BaseSyncService<T> {

	public static final String HTTP_HEADER_AUTHORIZATION = "Authorization";

	@Qualifier("esbJdbcTemplate")
	@Autowired
	protected NamedParameterJdbcTemplate esbJdbcTemplate;
	@Autowired
	private RestTemplate restTemplate;

	public BaseSyncService() {
		super();
	}

	protected abstract String getApiURIBase();

	protected abstract String getApiURIPath();

	protected String getApiURI() {
		return getApiURIBase() + getApiURIPath();
	}

	protected abstract String getApiTokenAuthorization();

	protected abstract Class<T> getResponseEntityClass();

	protected RestTemplate getRestTemplate() {
		return restTemplate;
	}
}
