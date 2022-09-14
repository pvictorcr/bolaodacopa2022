package com.pvictorcr.bolaodacopa.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.pvictorcr.bolaodacopa.commands.JogoCommand;
import com.pvictorcr.bolaodacopa.model.Jogo;

@Component
public class JogoCommandToJogo implements Converter<JogoCommand, Jogo> {

	private final PaisCommandoToPais paisCommandoToPais;
	
	public JogoCommandToJogo(PaisCommandoToPais paisCommandoToPais) {
		
		this.paisCommandoToPais = paisCommandoToPais;
	}
	
	@Override
	public Jogo convert(JogoCommand command) {

		if(command == null)
			return new Jogo();
		
		final Jogo jogo = new Jogo(paisCommandoToPais.convert(command.getP1()), paisCommandoToPais.convert(command.getP2()),
				command.getFase(), command.getData(), command.getNumeroDoJogo(), command.getEstadio(), 
				Integer.parseInt(command.getGols1().matches("^\\d+$") ? command.getGols1() : "0"),
				Integer.parseInt(command.getGols2().matches("^\\d+$") ? command.getGols2() : "0"), command.isTerminado(),
				paisCommandoToPais.convert(command.getVencedorPenaltis()));
		jogo.setId(command.getId());
		
		return jogo;
	}

}
