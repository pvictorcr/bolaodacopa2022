package com.pvictorcr.bolaodacopa.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaisCommand extends BaseCommand implements Comparable<PaisCommand> {

	private String nome;
	private char grupo;
	private String nomeImg;
	private String sigla;
	
	//Estatisticas
	private int pontos;
	private int jogos;
	private int vitorias;
	private int empates;
	private int derrotas;
	private int gp;
	private int gc;
	
	@Override
	public int compareTo(PaisCommand o) {

		if(pontos > o.getPontos())
			return 1;
		if(pontos < o.getPontos())
			return -1;
		
		//Primeiro criterio de desempate: saldo de gols
		if(gp - gc > o.gp - o.gc)
			return 1;
		if(gp - gc < o.gp - o.gc)
			return -1;
		
		//Segundo criterio de desempate: gols pro
		if(gp > o.gp)
			return 1;
		if(gp < o.gp)
			return -1;
		
		//Terceiro criterio de desempate: sorteio
		return 0;
	}
	
	public String getNomeImg() {
		if(nomeImg == null)
			return "bandeira_interrogacao";
		
		return nomeImg;
	}
}
