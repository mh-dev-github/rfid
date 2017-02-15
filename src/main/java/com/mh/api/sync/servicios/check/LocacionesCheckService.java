package com.mh.api.sync.servicios.check;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mh.api.sync.servicios.check.dto.LocacionCheckDTO;
import com.mh.model.esb.domain.esb.Locacion;
import com.mh.model.esb.domain.msg.LocacionMessage;
import com.mh.model.esb.repo.esb.LocacionRepository;
import com.mh.model.esb.repo.msg.LocacionMessageRepository;

@Service
public class LocacionesCheckService extends CheckService<LocacionCheckDTO, LocacionMessage, Locacion> {

	@Value("${sync.reintentos.LOCACIONES}")
	private int NUMERO_MAXIMO_REINTENTOS;

	@Value("${apes.rest.uri.path.locaciones}")
	private String API_URI_PATH;

	@Autowired
	private LocacionMessageRepository messageRepository;

	@Autowired
	private LocacionRepository repository;

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	@Override
	protected JpaRepository<LocacionMessage, Long> getMessageRepository() {
		return messageRepository;
	}

	@Override
	protected JpaRepository<Locacion, String> getRepository() {
		return repository;
	}

	@Override
	protected boolean isRoundRobin() {
		return false;
	}

	@Override
	protected String getApiURIPath() {
		return API_URI_PATH;
	}

	@Override
	protected int getNumeroMaximoReintentos() {
		return NUMERO_MAXIMO_REINTENTOS;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	@Override
	protected Class<LocacionCheckDTO> getResponseEntityClass() {
		return LocacionCheckDTO.class;
	}

	// -------------------------------------------------------------------------------------
	// Pending Changes
	// -------------------------------------------------------------------------------------
	@Override
	protected String getSQLSelectFromPendingChecks() {
		// @formatter:off
		return ""
        + "\n WITH "
        + "\n cte_00 AS "
        + "\n ( "
        + "\n     SELECT  "
        + "\n         b.mid,b.externalId,b.estado_cambio,b.intentos,ROW_NUMBER() OVER(PARTITION BY b.externalId ORDER BY b.mid DESC) AS orden  "
        + "\n     FROM esb.Locaciones a  "
        + "\n     INNER JOIN msg.Locaciones b ON "
        + "\n         b.externalId = a.externalId "
        + "\n     AND b.tipo_cambio <> 'X' "
        + "\n     WHERE 0 = 0 "
        + "\n     AND a.sincronizado = 0 "
        + "\n     AND COALESCE(b.fecha_ultimo_push,CAST('1900-01-01' AS DATE)) < :fechaUltimoPush "
        + "\n ) "
        + "\n SELECT "
        + "\n     a.mid "
        + "\n FROM cte_00 a "
        + "\n WHERE "
        + "\n     a.orden = 1 "
        + "\n AND a.estado_cambio IN ('ENVIADO')  "
        + "\n ORDER BY "
        + "\n      a.intentos DESC "
        + "\n     ,a.mid  "
        + "\n OFFSET 0 ROWS  "
        + "\n FETCH NEXT :fetch + 1 ROWS ONLY "
		+ "  ";
		// @formatter:on
	}

	@Override
	protected void evaluar(Locacion entidad, LocacionCheckDTO body) {
		StringBuilder sb = new StringBuilder();
		if (!entidad.getId().equals(body.getId())) {
			appendError(sb, "id", entidad.getId(), body.getId());
		}
		if (!entidad.getName().equals(body.getName())) {
			appendError(sb, "name", entidad.getName(), body.getName());
		}
		if (!entidad.getAddress().equals(body.getAddress())) {
			appendError(sb, "address", entidad.getAddress(), body.getAddress());
		}
		if (!entidad.getType().equals(body.getType())) {
			appendError(sb, "type", entidad.getType(), body.getType());
		}
		
		if (sb.length() > 0) {
			sb.insert(0, "{\n");
			sb.append("\"externalId\":");
			sb.append("\"");
			sb.append(entidad.getExternalId());
			sb.append("\"");
			sb.append("\n}");
			throw new CheckException(sb.toString());
		}
	}

	@Override
	protected LocacionMessage clonarMensaje(LocacionMessage a) {
		return new LocacionMessage(a);
	}
}
