package com.pvictorcr.bolaodacopa.converters;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.pvictorcr.bolaodacopa.commands.ApostaCommand;
import com.pvictorcr.bolaodacopa.commands.JogoApostaCommand;
import com.pvictorcr.bolaodacopa.model.Aposta;
import com.pvictorcr.bolaodacopa.model.JogoAposta;

@Component
public class ApostaCommandToAposta implements Converter<ApostaCommand, Aposta> {

	private final JogoApostaCommandToJogoAposta jogoApostaCommandToJogoAposta;
	
	public ApostaCommandToAposta(JogoApostaCommandToJogoAposta jogoApostaCommandToJogoAposta) {
		
		this.jogoApostaCommandToJogoAposta = jogoApostaCommandToJogoAposta;
	}
	
	@Override
	public Aposta convert(ApostaCommand command) {

		if(command == null)
			return new Aposta();
		
		List<JogoAposta> jogos = new ArrayList<JogoAposta>();
		for(JogoApostaCommand jac : command.getJogosApostas())
			jogos.add(jogoApostaCommandToJogoAposta.convert(jac));
		
		final Aposta aposta = new Aposta(jogos);
		aposta.setId(command.getId());
		
		return aposta;
	}

}
