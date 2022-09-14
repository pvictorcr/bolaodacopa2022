package com.pvictorcr.bolaodacopa.commands;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GrupoApostaCommand extends BaseCommand {

	List<JogoApostaCommand> jogos;
	List<PaisCommand> classificacao;
	char grupo;
	
	public GrupoApostaCommand() {
		this.jogos = new ArrayList<JogoApostaCommand>();
		this.classificacao = new ArrayList<PaisCommand>();
	}
}
