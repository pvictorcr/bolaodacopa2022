package com.pvictorcr.bolaodacopa.commands;

import java.util.ArrayList;
import java.util.List;

import com.pvictorcr.bolaodacopa.constants.Fases;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EliminatoriaApostaCommand extends BaseCommand {

	private List<JogoApostaCommand> jogos;
	
	public EliminatoriaApostaCommand() {
		this.jogos = new ArrayList<JogoApostaCommand>();
	}
	
	public Fases getFase() {
		if(jogos.size() > 0)
			return jogos.get(0).getJogo().getFase();
		
		return null;
	}
}
