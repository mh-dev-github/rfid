package com.mh.api.sync.servicios.push;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mh.amqp.dto.RequestDTO;
import com.mh.api.sync.dto.SkuAmountShipmentDateDTO;
import com.mh.api.sync.dto.base.PayloadDTO;
import com.mh.api.sync.servicios.push.dto.PedidoPushDTO;
import com.mh.model.esb.domain.esb.Pedido;
import com.mh.model.esb.domain.msg.PedidoMessage;
import com.mh.model.esb.repo.esb.PedidoRepository;
import com.mh.model.esb.repo.msg.PedidoMessageRepository;

@Service("create")
public class PedidosPushService extends PushService<PedidoPushDTO, PedidoMessage, Pedido> {

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
	protected Class<PedidoPushDTO> getResponseEntityClass() {
		return PedidoPushDTO.class;
	}

	@Override
	protected PayloadDTO payload(PedidoMessage mensaje) {
		// @formatter:off
		PedidoPushDTO payload = PedidoPushDTO
			.builder()
			.externalId(mensaje.getExternalId())
			.id(mensaje.getId())
		
			.sourceId(mensaje.getSourceId())
			.destinationId(mensaje.getDestinationId())
		
			.skusAmount(new ArrayList<>())
			.properties(new ArrayList<>())
			.build();

		List<SkuAmountShipmentDateDTO> lineas = mensaje
				.getLineas()
				.stream()
				.map(a -> 
					new SkuAmountShipmentDateDTO(
						a.getSku(),
						a.getAmount(), 
						a.getExpectedShipmentDate()))
				.collect(Collectors.toList());

		payload.getSkusAmount().addAll(lineas);

		Map<String, String> property;
		
		property = new LinkedHashMap<>();
		property.put("name", "Cliente");
		property.put("value", mensaje.getCliente());
		payload.getProperties().add(property);

		property = new LinkedHashMap<>();
		property.put("name", "Agencia");
		property.put("value", mensaje.getAgencia());
		payload.getProperties().add(property);

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
		+ "\n FROM msg.Despachos a "
		+ "\n WHERE 0 = 0 "
		+ "\n AND a.tipo_cambio IN ('C') "
		+ "\n AND a.estado_cambio IN ('PENDIENTE','REINTENTO') "
		+ "\n AND a.fecha_ultimo_pull < :fechaUltimoPull "
		+ "\n ORDER BY "
		+ "\n     a.intentos "
		+ "\n    ,a.mid "
		+ "\n OFFSET 0 ROWS "
		+ "\n FETCH NEXT :fetch + 1 ROWS ONLY "
		+ "  ";
		// @formatter:on
	}

	@Override
	protected PedidoMessage clonarMensaje(PedidoMessage a) {
		return new PedidoMessage(a);
	}
}
