package com.pvictorcr.bolaodacopa.services;

import java.util.List;

import com.pvictorcr.bolaodacopa.commands.TabelaCommand;
import com.pvictorcr.bolaodacopa.commands.UsuarioCommand;
import com.pvictorcr.bolaodacopa.model.Jogo;

public interface SimulacaoService extends CrudService<Jogo, Long> {

	public List<UsuarioCommand> getUsuariosOrdenados(TabelaCommand form);
}
