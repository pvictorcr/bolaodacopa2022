package com.pvictorcr.bolaodacopa.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.pvictorcr.bolaodacopa.commands.UsuarioCommand;
import com.pvictorcr.bolaodacopa.model.Usuario;

@Component
public class UsuarioToUsuarioCommand implements Converter<Usuario, UsuarioCommand> {
	
	private final ApostaToApostaCommand apostaToApostaCommand;
	
	public UsuarioToUsuarioCommand(ApostaToApostaCommand apostaToApostaCommand) {
		
		this.apostaToApostaCommand = apostaToApostaCommand;
	}
	
	@Override
	public UsuarioCommand convert(Usuario source) {
		
		if(source == null) {
			return new UsuarioCommand();
		}
		
		UsuarioCommand command = new UsuarioCommand();
		
		command.setId(source.getId());
		command.setEmail(source.getEmail());
		command.setNome(source.getNome());
		command.setCredencial(source.getCredencial());
		command.setProvider(source.getProvider());
		command.setAposta(apostaToApostaCommand.convert(source.getAposta()));
		
		return command;
	}

}
