package com.mh.model.esb.domain.esb;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mh.util.converters.LocalDateTimeAttributeConverter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(catalog = "esb", name = "Integraciones")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Integracion implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_integracion", nullable = false, length = 50)
	private int id;
	
	@Column(name = "codigo", nullable = false, length = 50)
	private String codigo;
	
	@Column(name = "nombre", nullable = false, length = 100)
	private String nombre;
	
	@Column(name = "descripcion", nullable = false, length = 200)
	private String descripcion;
	
	@Column(name = "fecha_ultimo_pull", nullable = false)
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	private LocalDateTime fechaUltimoPull;
}
