package com.pvictorcr.bolaodacopa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "pais")
public class Pais extends BaseEntity {

	@Column(name = "nome")
	private String nome;
	
	@Column(name = "grupo")
	private char grupo;
}
