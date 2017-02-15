package com.mh.api.sync.servicios.check;

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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import com.mh.amqp.dto.RequestDTO;
import com.mh.api.sync.dto.base.PayloadDTO;
import com.mh.api.sync.servicios.BaseSyncService;
import com.mh.model.esb.domain.esb.BaseEntity;
import com.mh.model.esb.domain.msg.BaseMessageEntity;
import com.mh.model.esb.domain.msg.MessageStatusType;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class CheckService<T extends PayloadDTO, S extends BaseMessageEntity, V extends BaseEntity>
		extends BaseSyncService<T> {

	protected CheckService() {
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

	protected int getPendingChecksBatchSize() {
		return PENDING_CHANGES_BATCH_SIZE;
	}

	abstract protected int getNumeroMaximoReintentos();

	// -------------------------------------------------------------------------------------
	// Check
	// -------------------------------------------------------------------------------------
	public boolean check(RequestDTO request) {
		boolean result = false;
		log.info("Inicio Check");

		LocalDateTime fechaUltimoPush = LocalDateTime.now();
		if (isRoundRobin()) {
			if (checkBatch(fechaUltimoPush)) {
				result = true;
			}
		} else {
			while (checkBatch(fechaUltimoPush)) {
				result = true;
			}
		}
		log.info("Fin Check");
		return result;
	}

	public boolean checkBatch(LocalDateTime fechaUltimoPush) {
		List<S> mensajes = getPendingChecks(fechaUltimoPush);

		for (S mensaje : mensajes) {
			conciliarPost(mensaje);
		}
		return (mensajes.size() == getPendingChecksBatchSize());
	}

	private List<S> getPendingChecks(LocalDateTime fechaUltimoPush) {

		String sql = getSQLSelectFromPendingChecks();

		// @formatter:off
		val parametros = new MapSqlParameterSource()
				.addValue("fetch", getPendingChecksBatchSize() - 1)
				.addValue("fechaUltimoPush", fechaUltimoPush);
		// @formatter:on

		List<Long> mids = esbJdbcTemplate.queryForList(sql, parametros, Long.class);
		List<S> rows = getMessageRepository().findAll(mids);

		return rows;
	}

	abstract protected String getSQLSelectFromPendingChecks();

	// -------------------------------------------------------------------------------------
	// GET
	// -------------------------------------------------------------------------------------
	@Transactional(value = "transactionManager")
	private boolean conciliarPost(S mensaje) {
		if (!MessageStatusType.ENVIADO.equals(mensaje.getEstadoCambio())) {
			return false;
		}

		String url = getApiURI() + "/" + mensaje.getExternalId() + getApiGetFields();
		exchange(mensaje, url, HttpMethod.GET, createRequestEntity(mensaje), getResponseEntityClass());
		return true;
	}

	protected String getApiGetFields() {
		return "";
	}

	// -------------------------------------------------------------------------------------
	// EXCHANGE
	// -------------------------------------------------------------------------------------
	protected HttpEntity<?> createRequestEntity(S mensaje) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(HTTP_HEADER_AUTHORIZATION, getApiTokenAuthorization());
		HttpEntity<?> request = new HttpEntity<>(headers);
		return request;
	}

	protected void exchange(S mensaje, String url, HttpMethod method, HttpEntity<?> request, Class<T> clazz) {
		try {
			log.info("Inicio de exchange {}", request.getBody());
			ResponseEntity<T> response = getRestTemplate().exchange(url, method, request, clazz);
			V entidad = getRepository().findOne(mensaje.getExternalId());
			evaluar(entidad, response.getBody());
			proccesSuccessCheck(mensaje.getMid(), response.getBody().getId());
		} catch (RestClientResponseException e) {
			proccesRestClientResponseException(mensaje.getMid(), e);
		} catch (ResourceAccessException e) {
			proccesResourceAccessException(mensaje.getMid(), e);
		} catch (RuntimeException e) {
			proccesRuntimeException(mensaje.getMid(), e);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			log.info("Fin de exchange {}", request.getBody());
		}
	}

	abstract protected void evaluar(V entidad, T body);

	protected void appendError(StringBuilder sb, String property, String sourceValue, String destinationValue) {
		// @formatter:off
		sb
		.append("\"")
		.append(property)
		.append("\"")
		.append(":{")
		.append("rfid:")
		.append("\"")
		.append(sourceValue)
		.append("\"")
		.append(",apes:")
		.append("\"")
		.append(destinationValue)
		.append("\"")
		.append("}")
		.append(",")
		.append("\n");
		// @formatter:on
	}

	// -------------------------------------------------------------------------------------
	// Success/Error
	// -------------------------------------------------------------------------------------
	protected void proccesSuccessCheck(long mid, String id) {
		LocalDateTime fechaUltimoPush = LocalDateTime.now();
		S mensaje = getMessageRepository().findOne(mid);
		V entidad = getRepository().findOne(mensaje.getExternalId());

		mensaje.integrado(id, fechaUltimoPush);
		entidad.integrado(fechaUltimoPush);

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
				proccesErrorCheck(mid, jsonObject.getInt("code"), jsonObject.getString("message"), e);
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
		proccesErrorCheck(mid, -1, e.getMessage(), e);
	}

	private void proccesErrorCheck(long mid, int syncCodigo, String syncMensaje, RuntimeException e) {
		LocalDateTime fechaUltimoPush = LocalDateTime.now();
		S mensaje = getMessageRepository().findOne(mid);

		mensaje.error(syncCodigo, syncMensaje, e, fechaUltimoPush);

		S clon = clonarMensaje(mensaje);
		clon.setEstadoCambio(nuevoEstadoDespueDeErrorCheck(mensaje, syncCodigo, syncMensaje, e));
		clon.setIntentos(mensaje.getIntentos() + 1);
		clon.setFechaUltimoPull(fechaSiguientePush(fechaUltimoPush));

		V entidad = getRepository().findOne(mensaje.getExternalId());
		entidad.setFechaUltimoPush(fechaUltimoPush);
		entidad.setSincronizado(false);

		getMessageRepository().save(mensaje);
		getMessageRepository().save(clon);
		getRepository().save(entidad);
	}

	private LocalDateTime fechaSiguientePush(LocalDateTime fechaUltimoPush) {
		return fechaUltimoPush.plusMinutes(1).truncatedTo(ChronoUnit.MINUTES);
	}

	protected MessageStatusType nuevoEstadoDespueDeErrorCheck(S mensaje, int syncCodigo, String syncMensaje,
			RuntimeException e) {
		MessageStatusType estadoCambio;
		if (e instanceof CheckException) {
			estadoCambio = MessageStatusType.INCONSISTENTE;
		} else {
			if (mensaje.getIntentos() >= getNumeroMaximoReintentos()) {
				estadoCambio = MessageStatusType.INCONSISTENTE;
			} else {
				if (HttpStatus.NOT_FOUND.value() == syncCodigo) {
					estadoCambio = MessageStatusType.INCONSISTENTE;
				} else {
					estadoCambio = MessageStatusType.ENVIADO;
				}
			}
		}
		return estadoCambio;
	}

	abstract protected S clonarMensaje(S a);
}