package com.pvictorcr.bolaodacopa.commands;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GrupoCommand extends BaseCommand {

	List<JogoCommand> jogos;
	List<PaisCommand> classificacao;
	char grupo;
	
	public GrupoCommand() {
		this.jogos = new ArrayList<JogoCommand>();
		this.classificacao = new ArrayList<PaisCommand>();
	}
	
	public boolean isTerminado() {
		for(JogoCommand jc : jogos)
			if(!jc.isTerminado())
				return false;
		
		return true;
	}
}
