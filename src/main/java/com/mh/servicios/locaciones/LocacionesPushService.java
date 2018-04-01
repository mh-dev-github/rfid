package com.mh.servicios.locaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mh.dto.amqp.RequestDTO;
import com.mh.dto.servicios.sync.base.PayloadDTO;
import com.mh.dto.servicios.sync.pull.LocacionPullDTO;
import com.mh.dto.servicios.sync.push.LocacionPushDTO;
import com.mh.model.esb.domain.esb.Locacion;
import com.mh.model.esb.domain.msg.LocacionMessage;
import com.mh.model.esb.repo.esb.LocacionRepository;
import com.mh.model.esb.repo.msg.LocacionMessageRepository;
import com.mh.services.PushService;

@Service
public class LocacionesPushService extends PushService<LocacionPushDTO, LocacionMessage, Locacion> {

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
	protected Class<LocacionPushDTO> getResponseEntityClass() {
		return LocacionPushDTO.class;
	}

	@Override
	protected PayloadDTO payload(LocacionMessage mensaje) {
		// @formatter:off
		LocacionPullDTO payload = LocacionPullDTO
			.builder()
			.externalId(mensaje.getExternalId())
			.id(mensaje.getId())
		
			.name(mensaje.getName())
			.address(mensaje.getAddress())
			.type(mensaje.getType())
		
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
		+ "\n FROM msg.Locaciones a "
		+ "\n WHERE 0 = 0 "
		+ "\n AND a.tipo_cambio IN ('C','U') "
		+ "\n AND a.estado_cambio IN ('PENDIENTE','REINTENTO') "
		+ "\n AND a.fecha_ultimo_pull < :fechaUltimoPull "
		+ "\n ORDER BY "
		+ "\n     a.intentos "
		+ "\n    ,a.mid "
		+ "\n OFFSET 0 ROWS  "
		+ "\n FETCH NEXT :fetch + 1 ROWS ONLY "
		+ "  ";
		// @formatter:on
	}

	@Override
	protected LocacionMessage clonarMensaje(LocacionMessage a) {
		return new LocacionMessage(a);
	}
}
