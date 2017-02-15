package com.mh.api.sync.servicios.push;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mh.amqp.dto.RequestDTO;
import com.mh.api.sync.dto.SkuAmountDTO;
import com.mh.api.sync.dto.base.PayloadDTO;
import com.mh.api.sync.servicios.push.dto.OrdenProduccionPushDTO;
import com.mh.model.esb.domain.esb.OrdenProduccion;
import com.mh.model.esb.domain.msg.OrdenProduccionMessage;
import com.mh.model.esb.repo.esb.OrdenProduccionRepository;
import com.mh.model.esb.repo.msg.OrdenProduccionMessageRepository;

@Service
public class OrdenesProduccionPushService
		extends PushService<OrdenProduccionPushDTO, OrdenProduccionMessage, OrdenProduccion> {

	@Value("${sync.reintentos.ORDENES_DE_PRODUCCION}")
	private int NUMERO_MAXIMO_REINTENTOS;

	@Value("${apes.rest.uri.path.ordenes-de-produccion}")
	private String API_URI_PATH;

	@Autowired
	private OrdenProduccionMessageRepository messageRepository;

	@Autowired
	private OrdenProduccionRepository repository;

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	@Override
	protected JpaRepository<OrdenProduccionMessage, Long> getMessageRepository() {
		return messageRepository;
	}

	@Override
	protected JpaRepository<OrdenProduccion, String> getRepository() {
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
	protected Class<OrdenProduccionPushDTO> getResponseEntityClass() {
		return OrdenProduccionPushDTO.class;
	}

	@Override
	protected PayloadDTO payload(OrdenProduccionMessage mensaje) {
		// @formatter:off
		OrdenProduccionPushDTO payload = OrdenProduccionPushDTO
			.builder()
			.externalId(mensaje.getExternalId())
			.id(mensaje.getId())
		
			.supplier(mensaje.getSupplier())
			.arrivalDate(mensaje.getArrivalDate())
		
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
		+ "\n FROM msg.OrdenesProduccion a "
		+ "\n WHERE 0 = 0 "
		+ "\n AND a.tipo_cambio IN ('C','U') "
		+ "\n AND a.estado_cambio IN ('PENDIENTE','REINTENTO') "
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
	protected OrdenProduccionMessage clonarMensaje(OrdenProduccionMessage a) {
		return new OrdenProduccionMessage(a);
	}
}

