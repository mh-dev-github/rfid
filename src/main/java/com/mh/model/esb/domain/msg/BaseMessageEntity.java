package com.mh.model.esb.domain.msg;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.mh.util.converters.LocalDateTimeAttributeConverter;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@MappedSuperclass
public class BaseMessageEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long mid;
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_cambio", nullable = false, length = 50)
	private MessageType tipoCambio;
	@Enumerated(EnumType.STRING)
	@Column(name = "estado_cambio", nullable = false, length = 50)
	private MessageStatusType estadoCambio;
	private int intentos;
	@Column(name = "fecha_ultimo_pull", nullable = false)
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	private LocalDateTime fechaUltimoPull;
	@Column(name = "fecha_ultimo_push", nullable = false)
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	private LocalDateTime fechaUltimoPush;
	@Column(name = "sync_codigo")
	private int syncCodigo;
	@Setter(value = AccessLevel.NONE)
	@Column(name = "sync_mensaje", nullable = false, length = 1024)
	private String syncMensaje;
	@Setter(value = AccessLevel.NONE)
	@Column(name = "sync_exception", nullable = false, length = 256)
	private String syncException;
	@Column(length = 50, nullable = false)
	private String externalId;
	@Column(length = 50, nullable = false)
	private String id;

	public void setSyncMensaje(String syncMensaje) {
		if (syncMensaje.length() > 1024) {
			syncMensaje = syncMensaje.substring(0, 1024);
		}
		this.syncMensaje = syncMensaje;
	}

	public void setSyncException(String syncException) {
		if (syncException.length() > 256) {
			syncException = syncException.substring(0, 256);
		}
		this.syncException = syncException;
	}

	public void integrado(String id, LocalDateTime fechaUltimoPush) {
		setEstadoCambio(MessageStatusType.INTEGRADO);
		setIntentos(getIntentos() + 1);
		setFechaUltimoPush(fechaUltimoPush);
		setSyncCodigo(0);
		setSyncMensaje("");
		setSyncException("");
	}

	public void enviado(String id, LocalDateTime fechaUltimoPush) {
		if (MessageType.C.equals(getTipoCambio())) {
			setId(id);
		}
		setEstadoCambio(MessageStatusType.ENVIADO);
		setIntentos(0);
		setFechaUltimoPush(fechaUltimoPush);
		setSyncCodigo(0);
		setSyncMensaje("");
		setSyncException("");
	}

	public void error(int syncCodigo, String syncMensaje, RuntimeException e, LocalDateTime fechaUltimoPush) {
		setEstadoCambio(MessageStatusType.ERROR);
		setFechaUltimoPush(fechaUltimoPush);
		setSyncCodigo(syncCodigo);
		setSyncMensaje(syncMensaje);
		setSyncException(e.getClass().getName());
	}

	public BaseMessageEntity(BaseMessageEntity a) {
		super();
		this.setTipoCambio(a.getTipoCambio());
		this.setEstadoCambio(a.getEstadoCambio());
		this.setIntentos(0);
		this.setFechaUltimoPull(null);
		this.setFechaUltimoPush(null);
		this.setSyncCodigo(0);
		this.setSyncMensaje("");
		this.setSyncException("");
		this.setExternalId(a.getExternalId());
		this.setId(a.getId());
	}
}