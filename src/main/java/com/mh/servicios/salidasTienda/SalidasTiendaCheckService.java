package com.mh.servicios.salidasTienda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mh.dto.servicios.sync.check.SalidaTiendaCheckDTO;
import com.mh.model.esb.domain.esb.SalidaTienda;
import com.mh.model.esb.domain.msg.SalidaTiendaMessage;
import com.mh.model.esb.repo.esb.SalidaTiendaRepository;
import com.mh.model.esb.repo.msg.SalidaTiendaMessageRepository;
import com.mh.services.CheckException;
import com.mh.services.CheckService;

@Service
public class SalidasTiendaCheckService extends CheckService<SalidaTiendaCheckDTO, SalidaTiendaMessage, SalidaTienda> {

	@Value("${sync.reintentos.SALIDAS_TIENDA}")
	private int NUMERO_MAXIMO_REINTENTOS;

	@Value("${apes.rest.uri.path.salidas-tiendas}")
	private String API_URI_PATH;

	@Autowired
	private SalidaTiendaMessageRepository messageRepository;

	@Autowired
	private SalidaTiendaRepository repository;

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	@Override
	protected JpaRepository<SalidaTiendaMessage, Long> getMessageRepository() {
		return messageRepository;
	}

	@Override
	protected JpaRepository<SalidaTienda, String> getRepository() {
		return repository;
	}

	@Override
	protected boolean isRoundRobin() {
		return true;
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
	protected Class<SalidaTiendaCheckDTO> getResponseEntityClass() {
		return SalidaTiendaCheckDTO.class;
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
        + "\n     FROM esb.SalidasTiendas a  "
        + "\n     INNER JOIN msg.SalidasTiendas b ON "
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
	protected String getApiGetFields() {
		return "?fields=id,externalId,sourceId,destinationId,expectedShipmentDate,skuCount,itemCount";
	}

	@Override
	protected void evaluar(SalidaTienda entidad, SalidaTiendaCheckDTO body) {
		StringBuilder sb = new StringBuilder();
		if (!entidad.getId().equals(body.getId())) {
			appendError(sb, "id", entidad.getId(), body.getId());
		}
		if (!entidad.getSourceId().equals(body.getSourceId())) {
			appendError(sb, "sourceId", entidad.getSourceId(), body.getSourceId());
		}
		if (!entidad.getDestinationId().equals(body.getDestinationId())) {
			appendError(sb, "destinationId", entidad.getDestinationId(), body.getDestinationId());
		}
		if (!entidad.getExpectedShipmentDate().equals(body.getExpectedShipmentDate())) {
			appendError(sb, "expectedShipmentDate", entidad.getExpectedShipmentDate(), body.getExpectedShipmentDate());
		}
		
		int skuCount = entidad.getLineas().size();
		if (skuCount != body.getSkuCount()) {
			appendError(sb, "skuCount", String.valueOf(skuCount), String.valueOf(body.getSkuCount()));
		}

		int itemCount = entidad.getLineas().stream().mapToInt(a -> a.getAmount()).sum();
		if (itemCount != body.getItemCount()) {
			appendError(sb, "itemCount", String.valueOf(itemCount), String.valueOf(body.getItemCount()));
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
	protected SalidaTiendaMessage clonarMensaje(SalidaTiendaMessage a) {
		return new SalidaTiendaMessage(a);
	}
}