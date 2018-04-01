package com.mh.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mh.dto.amqp.RequestDTO;
import com.mh.dto.servicios.sync.base.PayloadDTO;
import com.mh.model.esb.domain.esb.BaseEntity;
import com.mh.model.esb.domain.msg.BaseMessageEntity;
import com.mh.model.esb.domain.msg.MessageStatusType;
import com.mh.model.esb.domain.msg.MessageType;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class PushService<T extends PayloadDTO, S extends BaseMessageEntity, V extends BaseEntity>
		extends BaseSyncService<T> {
	protected PushService() {
		super();
	}

	@Value("${sync.PendingChangesBatchSize}")
	private int PENDING_CHANGES_BATCH_SIZE;

	@Value("${apes.rest.uri.base}")
	private String API_URI_BASE;

	@Value("${apes.rest.authorization}")
	private String API_AUTHORIZATION;

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	abstract protected JpaRepository<S, Long> getMessageRepository();

	abstract protected JpaRepository<V, String> getRepository();

	abstract protected boolean isRoundRobin();

	@Override
	protected String getApiURIBase() {
		return API_URI_BASE;
	}

	@Override
	protected String getApiTokenAuthorization() {
		return API_AUTHORIZATION;
	}

	protected int getPendingChangesBatchSize() {
		return PENDING_CHANGES_BATCH_SIZE;
	}

	abstract protected int getNumeroMaximoReintentos();

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	abstract protected PayloadDTO payload(S mensaje);

	// -------------------------------------------------------------------------------------
	// PUSH
	// -------------------------------------------------------------------------------------
	public boolean push(RequestDTO request) {
		boolean result = false;
		log.info("Inicio Push");

		LocalDateTime fechaUltimoPull = LocalDateTime.now();
		if (isRoundRobin()) {
			if (pushBatch(request, fechaUltimoPull)) {
				result = true;
			}
		} else {
			while (pushBatch(request, fechaUltimoPull)) {
				result = true;
			}
		}
		log.info("Fin Push");
		return result;
	}

	public boolean pushBatch(RequestDTO request, LocalDateTime fechaUltimoPull) {
		List<S> mensajes = getPendingChanges(request, fechaUltimoPull);

		for (S mensaje : mensajes) {
			if (MessageType.C.equals(mensaje.getTipoCambio())) {
				post(mensaje);
			} else {
				put(mensaje);
			}
		}
		return (mensajes.size() == getPendingChangesBatchSize());
	}

	private List<S> getPendingChanges(RequestDTO request, LocalDateTime fechaUltimoPull) {

		String sql = getSQLSelectFromPendingChanges(request);

		// @formatter:off
		val parametros = new MapSqlParameterSource()
				.addValue("fetch", getPendingChangesBatchSize() - 1)
				.addValue("fechaUltimoPull", fechaUltimoPull);
		// @formatter:on

		List<Long> mids = esbJdbcTemplate.queryForList(sql, parametros, Long.class);
		List<S> rows = getMessageRepository().findAll(mids);

		return rows;
	}

	abstract protected String getSQLSelectFromPendingChanges(RequestDTO request);

	// -------------------------------------------------------------------------------------
	// POST
	// -------------------------------------------------------------------------------------
	@Transactional(value = "transactionManager")
	protected void post(S mensaje) {
		if (conciliarPost(mensaje)) {
			return;
		}
		String url = getApiURI();
		exchange(mensaje.getMid(), url, HttpMethod.POST, createRequestEntity(mensaje), getResponseEntityClass());
	}

	private boolean conciliarPost(S mensaje) {
		if (!MessageType.C.equals(mensaje.getTipoCambio())) {
			return false;
		}
		if (!MessageStatusType.REINTENTO.equals(mensaje.getEstadoCambio())) {
			return false;
		}

		String url = getApiURI() + "/" + mensaje.getExternalId();
		exchange(mensaje.getMid(), url, HttpMethod.GET, createRequestEntity(mensaje), getResponseEntityClass());
		return true;
	}

	// -------------------------------------------------------------------------------------
	// PUT
	// -------------------------------------------------------------------------------------
	@Transactional(value = "transactionManager")
	protected void put(S mensaje) {
		String url = getApiURI() + "/" + mensaje.getId();
		exchange(mensaje.getMid(), url, HttpMethod.PUT, createRequestEntity(mensaje), getResponseEntityClass());
	}

	// -------------------------------------------------------------------------------------
	// EXCHANGE
	// -------------------------------------------------------------------------------------
	protected HttpEntity<?> createRequestEntity(S mensaje) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(HTTP_HEADER_AUTHORIZATION, getApiTokenAuthorization());
		HttpEntity<?> request = new HttpEntity<>(payload(mensaje), headers);
		return request;
	}

	protected void exchange(long mid, String url, HttpMethod method, HttpEntity<?> request, Class<T> clazz) {
		try {
			log.info("Inicio de exchange {}", request.getBody());
			MappingJackson2HttpMessageConverter mapping = new MappingJackson2HttpMessageConverter();
			try {
				log.debug(mapping.getObjectMapper().writeValueAsString(request.getBody()));
			} catch (JsonProcessingException e) {
				log.error("No fue posible convertir en JSON el mensaje {}", request.getBody().toString());
			}
			ResponseEntity<T> response = getRestTemplate().exchange(url, method, request, clazz);
			proccesSuccessIntegration(mid, getIdFromResponseBody(response.getBody()));
		} catch (RestClientResponseException e) {
			proccesRestClientResponseException(mid, e);
		} catch (ResourceAccessException e) {
			proccesResourceAccessException(mid, e);
		} catch (RuntimeException e) {
			proccesRuntimeException(mid, e);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			log.info("Fin de exchange {}", request.getBody());
		}
	}

	protected String getIdFromResponseBody(T responseBody) {
		return responseBody.getId();
	}

	// -------------------------------------------------------------------------------------
	// Success/Error
	// -------------------------------------------------------------------------------------
	protected void proccesSuccessIntegration(long mid, String id) {
		LocalDateTime fechaUltimoPush = LocalDateTime.now();
		S mensaje = getMessageRepository().findOne(mid);
		V entidad = getRepository().findOne(mensaje.getExternalId());

		mensaje.enviado(id, fechaUltimoPush);
		entidad.enviado(id, fechaUltimoPush);

		getMessageRepository().save(mensaje);
		getRepository().save(entidad);
	}

	// -------------------------------------------------------------------------------------
	// Exception
	// -------------------------------------------------------------------------------------
	protected void proccesRestClientResponseException(long mid, RestClientResponseException e) {
		JSONTokener tokener = new JSONTokener(e.getResponseBodyAsString());
		try {
			JSONObject jsonObject = new JSONObject(tokener);
			if (jsonObject.has("error")) {
				jsonObject = jsonObject.getJSONObject("error");
				proccesErrorIntegration(mid, jsonObject.getInt("code"), jsonObject.getString("message"), e);
				return;
			}
		} catch (JSONException je) {
		}
		proccesRuntimeException(mid, e);
	}

	protected void proccesResourceAccessException(long mid, ResourceAccessException e) {
		proccesRuntimeException(mid, e);
	}

	protected void proccesRuntimeException(long mid, RuntimeException e) {
		proccesErrorIntegration(mid, -1, e.getMessage(), e);
	}

	private void proccesErrorIntegration(long mid, int syncCodigo, String syncMensaje, RuntimeException e) {
		LocalDateTime fechaUltimoPush = LocalDateTime.now();
		S mensaje = getMessageRepository().findOne(mid);

		mensaje.error(syncCodigo, syncMensaje, e, fechaUltimoPush);

		S clon = clonarMensaje(mensaje);
		clon.error(syncCodigo, syncMensaje, e, fechaUltimoPush);
		clon.setEstadoCambio(nuevoEstadoDespuesDeErrorIntegracion(mensaje, syncCodigo, syncMensaje));
		clon.setIntentos(mensaje.getIntentos() + 1);
		clon.setFechaUltimoPull(fechaSiguientePull(fechaUltimoPush));
		

		V entidad = getRepository().findOne(mensaje.getExternalId());
		entidad.setFechaUltimoPush(fechaUltimoPush);
		entidad.setSincronizado(false);

		getMessageRepository().save(mensaje);
		getMessageRepository().save(clon);
		getRepository().save(entidad);
	}

	private LocalDateTime fechaSiguientePull(LocalDateTime fechaUltimoPush) {
		return fechaUltimoPush.plusMinutes(1).truncatedTo(ChronoUnit.MINUTES);
	}

	protected MessageStatusType nuevoEstadoDespuesDeErrorIntegracion(S mensaje, int syncCodigo, String syncMensaje) {
		MessageStatusType estadoCambio;
		if (mensaje.getIntentos() >= getNumeroMaximoReintentos()) {
			estadoCambio = MessageStatusType.DESCARTADO;
		} else {
			if (HttpStatus.NOT_FOUND.value() == syncCodigo) {
				estadoCambio = MessageStatusType.PENDIENTE;
			} else {
				estadoCambio = MessageStatusType.REINTENTO;
			}
		}
		return estadoCambio;
	}

	abstract protected S clonarMensaje(S a);
}