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
import com.mh.api.sync.servicios.push.dto.SalidaTiendaPushDTO;
import com.mh.model.esb.domain.esb.SalidaTienda;
import com.mh.model.esb.domain.msg.SalidaTiendaMessage;
import com.mh.model.esb.repo.esb.SalidaTiendaRepository;
import com.mh.model.esb.repo.msg.SalidaTiendaMessageRepository;

@Service
public class SalidasTiendaPushService extends PushService<SalidaTiendaPushDTO, SalidaTiendaMessage, SalidaTienda> {

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

//	@Override
//	protected RequestType getRequestType() {
//		return RequestType.PUSH_SALIDAS_TIENDA;
//	}

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
	protected Class<SalidaTiendaPushDTO> getResponseEntityClass() {
		return SalidaTiendaPushDTO.class;
	}

	@Override
	protected PayloadDTO payload(SalidaTiendaMessage mensaje) {
		// @formatter:off
		SalidaTiendaPushDTO payload = SalidaTiendaPushDTO
			.builder()
			.externalId(mensaje.getExternalId())
			.id(mensaje.getId())
		
			.sourceId(mensaje.getSourceId())
			.destinationId(mensaje.getDestinationId())
			.expectedShipmentDate(mensaje.getExpectedShipmentDate())
			
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
		+ "\n FROM msg.SalidasTiendas a "
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
	protected SalidaTiendaMessage clonarMensaje(SalidaTiendaMessage a) {
		return new SalidaTiendaMessage(a);
	}
}