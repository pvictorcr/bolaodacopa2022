package com.pvictorcr.bolaodacopa.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "aposta")
public class Aposta extends BaseEntity {

	@OneToMany(mappedBy = "aposta", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<JogoAposta> jogosApostas;
	
	@OneToOne
	private Pais campeao;
	
	@OneToOne
	private Pais viceCampeao;
	
	public Aposta() {
		this.jogosApostas = new ArrayList<JogoAposta>();
	}
	
	public Aposta addJogoAposta(JogoAposta jogoAposta) {
		jogosApostas.add(jogoAposta);
		jogoAposta.setAposta(this);
		
		return this;
	}
	
	public Aposta removeJogoAposta(JogoAposta jogoAposta) {
		jogosApostas.remove(jogoAposta);
		jogoAposta.setAposta(null);
		
		return this;
	}
}
