package com.pvictorcr.bolaodacopa.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "jogo_aposta")
public class JogoAposta extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	private Aposta aposta;
	
	@ManyToOne
	@JoinColumn(name = "JOGO_ID")
	private Jogo jogo;
	
	private int gols1;
	
	private int gols2;

	public JogoAposta(Jogo jogo, int gols1, int gols2) {
		this.jogo = jogo;
		this.gols1 = gols1;
		this.gols2 = gols2;
	}
}
