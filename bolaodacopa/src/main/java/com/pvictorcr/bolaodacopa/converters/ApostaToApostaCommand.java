package com.pvictorcr.bolaodacopa.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.pvictorcr.bolaodacopa.commands.ApostaCommand;
import com.pvictorcr.bolaodacopa.model.Aposta;
import com.pvictorcr.bolaodacopa.model.JogoAposta;

@Component
public class ApostaToApostaCommand implements Converter<Aposta, ApostaCommand> {

	private final JogoApostaToJogoApostaCommand jogoApostaToJogoApostaCommand;
	private final PaisToPaisCommand paisToPaisCommand;
	
	public ApostaToApostaCommand(JogoApostaToJogoApostaCommand jogoApostaToJogoApostaCommand, PaisToPaisCommand paisToPaisCommand) {
		
		this.jogoApostaToJogoApostaCommand = jogoApostaToJogoApostaCommand;
		this.paisToPaisCommand = paisToPaisCommand;
	}
	
	@Override
	public ApostaCommand convert(Aposta source) {
		
		if(source == null) {
			return new ApostaCommand();
		}
		
		ApostaCommand command = new ApostaCommand();
		
		command.setCampeao(paisToPaisCommand.convert(source.getCampeao()));
		command.setViceCampeao(paisToPaisCommand.convert(source.getViceCampeao()));
		
		command.setId(source.getId());
		for(JogoAposta ja : source.getJogosApostas())
			command.getJogosApostas().add(jogoApostaToJogoApostaCommand.convert(ja));
		
		return command;
	}

}
