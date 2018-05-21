package com.mh.model.esb.domain.esb;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mh.util.converters.LocalDateTimeAttributeConverter;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Entidad Serial. Almacena los seriales creados para los articulos
 * 
 * @author arosorio@gmail.com
 *
 */
@Data
@NoArgsConstructor
@Entity
@Table(catalog = "esb", name = "Seriales")
public class Serial implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 50, nullable = false, unique = true)
	private String serial;
	@Column(length = 13, nullable = false)
	private String sku;
	@Column(name = "fecha_actualizacion", nullable = false)
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	private LocalDateTime fechaActualizacion;

	public Serial(String serial, String sku, LocalDateTime fechaActualizacion) {
		super();
		this.serial = serial;
		this.sku = sku;
		this.fechaActualizacion = fechaActualizacion;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Serial other = (Serial) obj;
		if (serial == null) {
			if (other.serial != null)
				return false;
		} else if (!serial.equals(other.serial))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((serial == null) ? 0 : serial.hashCode());
		return result;
	}
}
