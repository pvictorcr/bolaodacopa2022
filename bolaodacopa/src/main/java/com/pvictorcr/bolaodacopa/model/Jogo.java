package com.pvictorcr.bolaodacopa.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.pvictorcr.bolaodacopa.constants.Fases;

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
@Table(name = "jogo")
public class Jogo extends BaseEntity {
	
	@ManyToOne
	private Pais p1;
	
	@ManyToOne
	private Pais p2;
	
	private Fases fase;
	
	private Date data;
	
	private int numeroDoJogo;
	
	private String estadio;
	
	private int gols1;
	
	private int gols2;
	
	private boolean terminado;
	
	@ManyToOne
	private Pais vencedorPenaltis;
	
	public Jogo(Pais p1, Pais p2, Fases fase, Date data, int numeroDoJogo, String estadio) {
		this.p1 = p1;
		this.p2 = p2;
		this.fase = fase;
		this.data = data;
		this.numeroDoJogo = numeroDoJogo;
		this.estadio = estadio;
		this.terminado = false;
		this.vencedorPenaltis = null;
	}
}
