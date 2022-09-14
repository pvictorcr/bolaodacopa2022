package com.pvictorcr.bolaodacopa.services;

import java.util.List;

import com.pvictorcr.bolaodacopa.commands.JogoCommand;
import com.pvictorcr.bolaodacopa.commands.PaisCommand;
import com.pvictorcr.bolaodacopa.commands.TabelaCommand;
import com.pvictorcr.bolaodacopa.model.Jogo;

public interface TabelaService extends CrudService<Jogo, Long> {

	public List<JogoCommand> findAllCommands();
	
	public String saveJogoCommand(JogoCommand command);
	
	public String reativarJogo(JogoCommand command);
	
	public PaisCommand getPaisFromJogo(JogoCommand jc, int pais);

	public TabelaCommand findTabelaGruposCommands();
}
