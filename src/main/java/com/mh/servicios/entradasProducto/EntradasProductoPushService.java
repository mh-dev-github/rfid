package com.mh.servicios.entradasProducto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mh.dto.amqp.RequestDTO;
import com.mh.dto.servicios.sync.base.PayloadDTO;
import com.mh.dto.servicios.sync.base.SkuAmountDTO;
import com.mh.dto.servicios.sync.push.EntradaProductoPushDTO;
import com.mh.model.esb.domain.esb.EntradaProducto;
import com.mh.model.esb.domain.msg.EntradaProductoMessage;
import com.mh.model.esb.repo.esb.EntradaProductoRepository;
import com.mh.model.esb.repo.msg.EntradaProductoMessageRepository;
import com.mh.services.PushService;

@Service
public class EntradasProductoPushService
		extends PushService<EntradaProductoPushDTO, EntradaProductoMessage, EntradaProducto> {

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

	// @Override
	// protected RequestType getRequestType() {
	// return RequestType.PUSH_ENTRADAS_PT;
	// }

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
	protected Class<EntradaProductoPushDTO> getResponseEntityClass() {
		return EntradaProductoPushDTO.class;
	}

	@Override
	protected PayloadDTO payload(EntradaProductoMessage mensaje) {
		// @formatter:off
		EntradaProductoPushDTO payload = EntradaProductoPushDTO
			.builder()
			.externalId(mensaje.getExternalId())
			.id(mensaje.getId())
		
			.supplier(mensaje.getSupplier())
			.arrivalDate(mensaje.getArrivalDate())
			.concept(mensaje.getConcept())
			
			.skusAmount(new ArrayList<>())
			.build();

		List<SkuAmountDTO> lineas = mensaje
				.getLineas()
				.stream()
				.map(a -> 
					new SkuAmountDTO(
						a.getSku(),
						a.getAmount())) 
				.collect(Collectors.toList());

		payload.getSkusAmount().addAll(lineas);

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
		+ "\n FROM msg.EntradasProductoTerminado a "
		+ "\n WHERE 0 = 0 "
		+ "\n AND a.tipo_cambio IN ('C','U')   "
		+ "\n AND a.estado_cambio IN ('PENDIENTE','REINTENTO')  "
		+ "\n AND a.fecha_ultimo_pull < :fechaUltimoPull   "
		+ "\n ORDER BY  "
		+ "\n     a.intentos "
		+ "\n    ,a.mid "
		+ "\n OFFSET 0 ROWS  "
		+ "\n FETCH NEXT :fetch + 1 ROWS ONLY "
		+ "  ";
		// @formatter:on
	}

	@Override
	protected EntradaProductoMessage clonarMensaje(EntradaProductoMessage a) {
		return new EntradaProductoMessage(a);
	}
}
