package com.pvictorcr.bolaodacopa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "regras")
public class Regras extends BaseEntity {

	@Lob 
	@Column(name="CONTENT", length=4096)
	private String texto;
}
