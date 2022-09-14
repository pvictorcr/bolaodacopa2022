package com.pvictorcr.bolaodacopa.commands;

import java.util.ArrayList;
import java.util.List;

import com.pvictorcr.bolaodacopa.constants.Fases;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EliminatoriaCommand extends BaseCommand {

	private List<JogoCommand> jogos;
	private boolean ativo;
	
	public EliminatoriaCommand() {
		this.jogos = new ArrayList<JogoCommand>();
	}
	
	public Fases getFase() {
		if(jogos.size() > 0)
			return jogos.get(0).getFase();
		
		return null;
	}
	
	public boolean isTerminada() {
		
		if(jogos.size() <= 0)
			return false;
		
		for(JogoCommand j : jogos)
			if(!j.isTerminado())
				return false;
		
		return true;
	}
	
	public String getNomeFase() {
		
		switch(getFase()) {
		
		case OITAVAS :
			return "Oitavas de Final";
		case QUARTAS :
			return "Quartas de Final";
		case SEMIFINAIS :
			return "Semifinais";
		case DISPUTATERCEIRO :
			return "Terceiro Lugar";
		case FINAIS :
			return "Final";
		default:
			return "";
		}
	}
}
