package com.mh.servicios.productos;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mh.dto.servicios.sync.check.ProductoCheckDTO;
import com.mh.model.esb.domain.esb.Producto;
import com.mh.model.esb.domain.msg.ProductoMessage;
import com.mh.model.esb.repo.esb.ProductoRepository;
import com.mh.model.esb.repo.msg.ProductoMessageRepository;
import com.mh.services.CheckException;
import com.mh.services.CheckService;

import lombok.val;

@Service
public class ProductosCheckService extends CheckService<ProductoCheckDTO, ProductoMessage, Producto> {

	@Value("${sync.reintentos.PRODUCTOS}")
	private int NUMERO_MAXIMO_REINTENTOS;

	@Value("${apes.rest.uri.path.productos}")
	private String API_URI_PATH;

	@Autowired
	private ProductoMessageRepository messageRepository;

	@Autowired
	private ProductoRepository repository;

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	@Override
	protected JpaRepository<ProductoMessage, Long> getMessageRepository() {
		return messageRepository;
	}

	@Override
	protected JpaRepository<Producto, String> getRepository() {
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
	protected Class<ProductoCheckDTO> getResponseEntityClass() {
		return ProductoCheckDTO.class;
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
        + "\n     FROM esb.Productos a  "
        + "\n     INNER JOIN msg.Productos b ON "
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
	protected void evaluar(Producto entidad, ProductoCheckDTO body) {
		body.setId(body.getEan());
		body.setExternalId(body.getEan());

		StringBuilder sb = new StringBuilder();
		if (!entidad.getCompanyPrefix().equals(body.getCompanyPrefix())) {
			appendError(sb, "companyPrefix", entidad.getCompanyPrefix(), body.getCompanyPrefix());
		}
		if (!entidad.getName().equals(body.getName())) {
			appendError(sb, "name", entidad.getName(), body.getName());
		}
		String reference = entidad.getReference();
		reference = reference.substring(0, reference.length() - 1);
		if (!reference.equals(body.getReference())) {
			appendError(sb, "reference", entidad.getReference(), body.getReference());
		}
		if (!entidad.getEan().equals(body.getEan())) {
			appendError(sb, "ean", entidad.getEan(), body.getEan());
		}

		final Map<String, String> properties = new HashMap<>();
		properties.put("color", entidad.getColor());
		properties.put("codigoColor", entidad.getCodigoColor());
		properties.put("talla", entidad.getTalla());
		properties.put("tipoProducto", entidad.getTipoProducto());
		properties.put("coleccion", entidad.getColeccion());
		properties.put("grupoProducto", entidad.getGrupoProducto());
		properties.put("subGrupoProducto", entidad.getSubGrupoProducto());
		properties.put("fabricante", entidad.getFabricante());
		properties.put("temporada", entidad.getTemporada());
		properties.put("referencia", entidad.getReferencia());
		properties.put("modelo", entidad.getModelo());
		properties.put("genero", entidad.getGenero());

		if (body.getAttributes() != null) {
			body.getAttributes().forEach(a -> {
				String property = a.getName();
				String destinationValue = a.getValue();
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
	protected ProductoMessage clonarMensaje(ProductoMessage a) {
		return new ProductoMessage(a);
	}
}
