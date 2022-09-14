package com.pvictorcr.bolaodacopa.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.pvictorcr.bolaodacopa.commands.ApostaCommand;
import com.pvictorcr.bolaodacopa.model.Aposta;
import com.pvictorcr.bolaodacopa.model.JogoAposta;

@Component
public class ApostaToApostaCommand implements Converter<Aposta, ApostaCommand> {

	private final JogoApostaToJogoApostaCommand jogoApostaToJogoApostaCommand;
	
	public ApostaToApostaCommand(JogoApostaToJogoApostaCommand jogoApostaToJogoApostaCommand) {
		
		this.jogoApostaToJogoApostaCommand = jogoApostaToJogoApostaCommand;
	}
	
	@Override
	public ApostaCommand convert(Aposta source) {
		
		if(source == null) {
			return new ApostaCommand();
		}
		
		ApostaCommand command = new ApostaCommand();
		
		command.setId(source.getId());
		for(JogoAposta ja : source.getJogosApostas())
			command.getJogosApostas().add(jogoApostaToJogoApostaCommand.convert(ja));
		
		return command;
	}

}
