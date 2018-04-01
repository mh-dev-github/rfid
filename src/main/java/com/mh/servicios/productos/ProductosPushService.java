package com.mh.servicios.productos;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mh.dto.amqp.RequestDTO;
import com.mh.dto.servicios.sync.base.AttributeDTO;
import com.mh.dto.servicios.sync.base.PayloadDTO;
import com.mh.dto.servicios.sync.push.ProductoPushDTO;
import com.mh.model.esb.domain.esb.Producto;
import com.mh.model.esb.domain.msg.ProductoMessage;
import com.mh.model.esb.repo.esb.ProductoRepository;
import com.mh.model.esb.repo.msg.ProductoMessageRepository;
import com.mh.services.PushService;

@Service
public class ProductosPushService extends PushService<ProductoPushDTO,ProductoMessage, Producto> {

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
	protected Class<ProductoPushDTO> getResponseEntityClass() {
		return ProductoPushDTO.class;
	}
	
	@Override
	protected PayloadDTO payload(ProductoMessage mensaje) {
		// @formatter:off
		List<AttributeDTO> attributes = new ArrayList<>();

		attributes.add(new AttributeDTO("color", mensaje.getColor()));
		attributes.add(new AttributeDTO("codigoColor", mensaje.getCodigoColor()));
		attributes.add(new AttributeDTO("talla", mensaje.getTalla()));
		attributes.add(new AttributeDTO("tipoProducto", mensaje.getTipoProducto()));
		attributes.add(new AttributeDTO("coleccion", mensaje.getColeccion()));
		attributes.add(new AttributeDTO("grupoProducto", mensaje.getGrupoProducto()));
		attributes.add(new AttributeDTO("subGrupoProducto", mensaje.getSubGrupoProducto()));
		attributes.add(new AttributeDTO("fabricante", mensaje.getFabricante()));
		attributes.add(new AttributeDTO("temporada", mensaje.getTemporada()));
		attributes.add(new AttributeDTO("referencia", mensaje.getReferencia()));
		attributes.add(new AttributeDTO("modelo", mensaje.getModelo()));
		attributes.add(new AttributeDTO("genero", mensaje.getGenero()));
		
		
		ProductoPushDTO payload = ProductoPushDTO
			.builder()
			.externalId(mensaje.getExternalId())
			.id(mensaje.getId())
		
			.companyPrefix(mensaje.getCompanyPrefix())
			.name(mensaje.getName())
			.reference(mensaje.getReference())
			.ean(mensaje.getEan())
			.attributes(attributes)
		
			.build();

		return payload;
		// @formatter:on
	}
	
	// -------------------------------------------------------------------------------------
	// Pending Changes
	// -------------------------------------------------------------------------------------
	@Override
	protected String getSQLSelectFromPendingChanges(RequestDTO request) {
		// @formatter:off
		return ""
		+ "\n SELECT "
		+ "\n     a.mid "
		+ "\n FROM msg.Productos a "
		+ "\n WHERE 0 = 0 "
		+ "\n AND a.tipo_cambio IN ('C','U') "
		+ "\n AND a.estado_cambio IN ('PENDIENTE','REINTENTO') "
		+ "\n AND a.fecha_ultimo_pull < :fechaUltimoPull "
		+ "\n ORDER BY  "
		+ "\n     a.intentos "
		+ "\n    ,a.mid "
		+ "\n OFFSET 0 ROWS  "
		+ "\n FETCH NEXT :fetch + 1 ROWS ONLY "
		+ "  ";
		// @formatter:on
	}

	
	
	@Override
	protected String getIdFromResponseBody(ProductoPushDTO responseBody) {
		return responseBody.getEan();
	}

	@Override
	protected ProductoMessage clonarMensaje(ProductoMessage a) {
		return new ProductoMessage(a);
	}
}
