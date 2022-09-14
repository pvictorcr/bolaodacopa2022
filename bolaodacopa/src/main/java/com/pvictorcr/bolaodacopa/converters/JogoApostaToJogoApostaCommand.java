package com.pvictorcr.bolaodacopa.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.pvictorcr.bolaodacopa.commands.JogoApostaCommand;
import com.pvictorcr.bolaodacopa.model.JogoAposta;

@Component
public class JogoApostaToJogoApostaCommand implements Converter<JogoAposta, JogoApostaCommand> {

	private final JogoToJogoCommand jogoToJogoCommand;
	
	public JogoApostaToJogoApostaCommand(JogoToJogoCommand jogoToJogoCommand) {
		
		this.jogoToJogoCommand = jogoToJogoCommand;
	}
	
	@Override
	public JogoApostaCommand convert(JogoAposta source) {

		if(source == null) {
			return new JogoApostaCommand();
		}
		
		JogoApostaCommand command = new JogoApostaCommand();
		
		command.setId(source.getId());
		command.setGols1(source.getGols1());
		command.setGols2(source.getGols2());
		command.setJogo(jogoToJogoCommand.convert(source.getJogo()));
		
		return command;
	}
}
