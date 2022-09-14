package com.pvictorcr.bolaodacopa.services;

import java.util.List;

import com.pvictorcr.bolaodacopa.commands.UsuarioCommand;
import com.pvictorcr.bolaodacopa.model.Usuario;

public interface ClassificacaoService extends CrudService<Usuario, Long> {

	public List<UsuarioCommand> getUsuariosOrdenados();
	
	public List<UsuarioCommand> getUsuariosOrdenadosPorNome();
}
