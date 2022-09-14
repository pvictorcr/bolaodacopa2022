package com.pvictorcr.bolaodacopa.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.pvictorcr.bolaodacopa.constants.Credenciais;
import com.pvictorcr.bolaodacopa.constants.Provider;

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
@Table(name = "usuario")
public class Usuario extends BaseEntity {

	private String nome;
	
	private String email;
	
	private Credenciais credencial;
	
	@Enumerated(EnumType.STRING)
	private Provider provider;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Aposta aposta;
	
	
}
