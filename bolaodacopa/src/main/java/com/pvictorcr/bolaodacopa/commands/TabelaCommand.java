package com.pvictorcr.bolaodacopa.commands;

import java.util.ArrayList;
import java.util.List;

import com.pvictorcr.bolaodacopa.constants.Fases;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TabelaCommand extends BaseCommand {

	private String actionGrupos;
	private String actionEliminatorias;
	private List<GrupoCommand> grupos;
	private EliminatoriaCommand[] eliminatorias = new EliminatoriaCommand[5];
	private UsuarioCommand usuario;
	
	public TabelaCommand() {
		this.grupos = new ArrayList<GrupoCommand>();
		eliminatorias[Fases.FINAIS.ordinal()] = new EliminatoriaCommand();
		eliminatorias[Fases.DISPUTATERCEIRO.ordinal()] = new EliminatoriaCommand();
		eliminatorias[Fases.SEMIFINAIS.ordinal()] = new EliminatoriaCommand();
		eliminatorias[Fases.QUARTAS.ordinal()] = new EliminatoriaCommand();
		eliminatorias[Fases.OITAVAS.ordinal()] = new EliminatoriaCommand();		
	}
	
	public boolean isFaseDeGruposTerminada() {
		
		for(GrupoCommand gc : grupos)
			if(!gc.isTerminado())
				return false;
		
		return true;
	}
	
	public JogoCommand getJogo(int numeroDoJogo) {
		
		for(GrupoCommand g : grupos)
			for(JogoCommand jc : g.getJogos())
				if(jc.getNumeroDoJogo() == numeroDoJogo)
					return jc;
		
		for(EliminatoriaCommand ec : eliminatorias)
			for(JogoCommand jc : ec.getJogos())
				if(jc.getNumeroDoJogo() == numeroDoJogo)
					return jc;
		
		return null;
	}
}
