package com.mh.servicios.pedidos;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mh.dto.servicios.sync.check.PedidoCheckDTO;
import com.mh.model.esb.domain.esb.Pedido;
import com.mh.model.esb.domain.msg.PedidoMessage;
import com.mh.model.esb.repo.esb.PedidoRepository;
import com.mh.model.esb.repo.msg.PedidoMessageRepository;
import com.mh.services.CheckException;
import com.mh.services.CheckService;

import lombok.val;

@Service
public class PedidosCheckService extends CheckService<PedidoCheckDTO, PedidoMessage, Pedido> {

	@Value("${sync.reintentos.PEDIDOS}")
	private int NUMERO_MAXIMO_REINTENTOS;

	@Value("${apes.rest.uri.path.pedidos}")
	private String API_URI_PATH;

	@Autowired
	private PedidoMessageRepository messageRepository;

	@Autowired
	private PedidoRepository repository;

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	@Override
	protected JpaRepository<PedidoMessage, Long> getMessageRepository() {
		return messageRepository;
	}

	@Override
	protected JpaRepository<Pedido, String> getRepository() {
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
	protected Class<PedidoCheckDTO> getResponseEntityClass() {
		return PedidoCheckDTO.class;
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
        + "\n     FROM esb.Despachos a  "
        + "\n     INNER JOIN msg.Despachos b ON "
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
		return "?fields=id,externalId,sourceId,destinationId,properties,skuCount,itemCount";
	}

	@Override
	protected void evaluar(Pedido entidad, PedidoCheckDTO body) {
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

		final Map<String, String> properties = new HashMap<>();
		properties.put("agencia", entidad.getAgencia());
		properties.put("cliente", entidad.getCliente());
		if (body.getProperties() != null) {
			body.getProperties().forEach(a -> {
				String property = a.get("name").toLowerCase();
				String destinationValue = a.get("value");
				if (properties.containsKey(property)) {
					String sourceValue = properties.get(property);
					if (!sourceValue.equals(destinationValue)) {
						appendError(sb, property, sourceValue, destinationValue);
					}
					properties.remove(property);
				}
			});

		}
		for (val e : properties.entrySet()) {
			appendError(sb, e.getKey(), e.getValue(), "null");
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
	protected PedidoMessage clonarMensaje(PedidoMessage a) {
		return new PedidoMessage(a);
	}
}