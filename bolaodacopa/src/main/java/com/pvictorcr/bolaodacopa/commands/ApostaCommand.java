package com.pvictorcr.bolaodacopa.commands;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApostaCommand extends BaseCommand {

	private PaisCommand campeao;
	private PaisCommand viceCampeao;
	
	private String actionGrupos;
	private String actionEliminatorias;
	private List<JogoApostaCommand> jogosApostas = new ArrayList<JogoApostaCommand>();
	
	public JogoApostaCommand getJogo(int numeroDoJogo) {
		for(JogoApostaCommand jac : jogosApostas)
			if(jac.getJogo().getNumeroDoJogo() == numeroDoJogo)
				return jac;
		
		return new JogoApostaCommand();
	}
	
	public boolean jogoExiste(int numeroDoJogo) {
		for(JogoApostaCommand jac : jogosApostas)
			if(jac.getJogo().getNumeroDoJogo() == numeroDoJogo)
				return true;
		
		return false;
	}
	
	public boolean finalJaApostada() {
		
		if(campeao == null || campeao.getId() == 0 ||
			viceCampeao == null || viceCampeao.getId() == 0)
			return false;
		
		return true;
	}
}
