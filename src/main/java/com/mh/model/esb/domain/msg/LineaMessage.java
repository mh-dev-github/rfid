package com.mh.model.esb.domain.msg;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@MappedSuperclass
public class LineaMessage {
	private String sku;
	@Column(name = "match_sku")
	private boolean matchSku;
	private int amount;
}