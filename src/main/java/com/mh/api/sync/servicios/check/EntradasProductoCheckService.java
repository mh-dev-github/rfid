package com.mh.api.sync.servicios.check;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mh.api.sync.servicios.check.dto.EntradaProductoCheckDTO;
import com.mh.model.esb.domain.esb.EntradaProducto;
import com.mh.model.esb.domain.msg.EntradaProductoMessage;
import com.mh.model.esb.repo.esb.EntradaProductoRepository;
import com.mh.model.esb.repo.msg.EntradaProductoMessageRepository;

@Service
public class EntradasProductoCheckService
		extends CheckService<EntradaProductoCheckDTO, EntradaProductoMessage, EntradaProducto> {

	@Value("${sync.reintentos.ENTRADAS_PT}")
	private int NUMERO_MAXIMO_REINTENTOS;

	@Value("${apes.rest.uri.path.entradas-producto-terminado}")
	private String API_URI_PATH;

	@Autowired
	private EntradaProductoMessageRepository messageRepository;

	@Autowired
	private EntradaProductoRepository repository;

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	@Override
	protected JpaRepository<EntradaProductoMessage, Long> getMessageRepository() {
		return messageRepository;
	}

	@Override
	protected JpaRepository<EntradaProducto, String> getRepository() {
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
	protected Class<EntradaProductoCheckDTO> getResponseEntityClass() {
		return EntradaProductoCheckDTO.class;
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
        + "\n     FROM esb.EntradasProductoTerminado a  "
        + "\n     INNER JOIN msg.EntradasProductoTerminado b ON "
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
		return "?fields=id,externalId,supplier,arrivalDate,concept,skuCount,itemCount";
	}

	@Override
	protected void evaluar(EntradaProducto entidad, EntradaProductoCheckDTO body) {
		StringBuilder sb = new StringBuilder();
		if (!entidad.getId().equals(body.getId())) {
			appendError(sb, "id", entidad.getId(), body.getId());
		}
		if (!entidad.getSupplier().equals(body.getSupplier())) {
			appendError(sb, "supplier", entidad.getSupplier(), body.getSupplier());
		}
		if (!entidad.getArrivalDate().equals(body.getArrivalDate())) {
			appendError(sb, "arrivalDate", entidad.getArrivalDate(), body.getArrivalDate());
		}
		if (!entidad.getConcept().equals(body.getConcept())) {
			appendError(sb, "concept", entidad.getConcept(), body.getConcept());
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
	protected EntradaProductoMessage clonarMensaje(EntradaProductoMessage a) {
		return new EntradaProductoMessage(a);
	}
}
