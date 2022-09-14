package com.pvictorcr.bolaodacopa.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.pvictorcr.bolaodacopa.commands.JogoApostaCommand;
import com.pvictorcr.bolaodacopa.model.JogoAposta;

@Component
public class JogoApostaCommandToJogoAposta implements Converter<JogoApostaCommand, JogoAposta> {

	private final JogoCommandToJogo jogoCommandToJogo;
	
	public JogoApostaCommandToJogoAposta(JogoCommandToJogo jogoCommandoToJogo) {
		this.jogoCommandToJogo = jogoCommandoToJogo;
	}
	
	@Override
	public JogoAposta convert(JogoApostaCommand command) {

		if(command == null)
			return new JogoAposta();
		
		
		final JogoAposta jogo = new JogoAposta(jogoCommandToJogo.convert(command.getJogo()),command.getGols1(), command.getGols2());
		jogo.setId(command.getId());
		
		return jogo;
	}

}
