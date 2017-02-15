package com.mh.model.esb.domain.esb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Table(catalog = "esb", name = "Locaciones")
public class Locacion extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Column(length = 50, nullable = false)
	private String name;
	@Column(length = 100, nullable = false)
	private String address;
	@Column(length = 50, nullable = false)
	private String type;
	@Column(length = 300, nullable = false, name = "directorio_salidas")
	private String directorioSalidas;
}
