package com.mh.model.esb.domain.msg;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper=true)
@NoArgsConstructor
@Entity
@Table(catalog = "msg", name = "Locaciones")
public class LocacionMessage extends BaseMessageEntity {
	private static final long serialVersionUID = 1L;

	@Column(length = 50, nullable = false)
	private String name;
	@Column(length = 100, nullable = false)
	private String address;
	@Column(length = 50, nullable = false)
	private String type;

	public LocacionMessage(LocacionMessage a) {
		super(a);
		this.name = a.getName();
		this.address = a.getAddress();
		this.type = a.getType();
	}
}
