package com.pvictorcr.bolaodacopa.services;

import java.util.List;

import com.pvictorcr.bolaodacopa.commands.ApostaCommand;
import com.pvictorcr.bolaodacopa.commands.JogoApostaCommand;
import com.pvictorcr.bolaodacopa.commands.UsuarioCommand;
import com.pvictorcr.bolaodacopa.model.JogoAposta;

public interface ApostaService  extends CrudService<JogoAposta, Long> {

	public List<UsuarioCommand> getUsuariosOrdenados();
	
	public int getPosicao(UsuarioCommand u);
	
	public String saveApostaCommand(ApostaCommand aposta, JogoApostaCommand command);
	
	public boolean apagaJogoAposta(ApostaCommand aposta, JogoApostaCommand command);
}
