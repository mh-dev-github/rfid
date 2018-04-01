package com.mh.model.esb.domain.esb;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.mh.util.converters.LocalDateTimeAttributeConverter;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(length = 50, nullable = false)
	private String externalId;
	
	@Column(length = 50, nullable = false)
	private String id;
	
	@Column(nullable = false, name = "fecha_ultimo_pull")
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	private LocalDateTime fechaUltimoPull;
	
	@Column(nullable = false, name = "fecha_ultimo_push")
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	private LocalDateTime fechaUltimoPush;
	
	private boolean sincronizado;

	public void enviado(String id, LocalDateTime fechaUltimoPush) {
		if (getId().isEmpty()) {
			setId(id);
		}
		setFechaUltimoPush(fechaUltimoPush);
		setSincronizado(false);
	}

	public void integrado(LocalDateTime fechaUltimoPush) {
		setFechaUltimoPush(fechaUltimoPush);
		setSincronizado(true);
	}
}