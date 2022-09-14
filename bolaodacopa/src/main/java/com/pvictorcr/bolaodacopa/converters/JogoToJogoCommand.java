package com.pvictorcr.bolaodacopa.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.pvictorcr.bolaodacopa.commands.JogoCommand;
import com.pvictorcr.bolaodacopa.model.Jogo;

import lombok.Synchronized;

@Component
public class JogoToJogoCommand implements Converter<Jogo, JogoCommand> {

	private final PaisToPaisCommand paisToPaisCommand;
	
	public JogoToJogoCommand(PaisToPaisCommand paisToPaisCommand) {
		
		this.paisToPaisCommand = paisToPaisCommand;
	}
	
	@Synchronized
    @Nullable
    @Override
	public JogoCommand convert(Jogo source) {
		
		if(source == null) {
			return new JogoCommand();
		}
		
		JogoCommand command = new JogoCommand();
		
		command.setP1(paisToPaisCommand.convert(source.getP1()));
		command.setP2(paisToPaisCommand.convert(source.getP2()));
		command.setFase(source.getFase());
		command.setData(source.getData());
		command.setNumeroDoJogo(source.getNumeroDoJogo());
		command.setEstadio(source.getEstadio());
		command.setGols1(source.isTerminado() ? ""+source.getGols1() : "");
		command.setGols2(source.isTerminado() ? ""+source.getGols2() : "");
		command.setTerminado(source.isTerminado());
		command.setVencedorPenaltis(paisToPaisCommand.convert(source.getVencedorPenaltis()));
		
		command.setId(source.getId());
		
		return command;
	}
	
	public void setAllFields(Jogo toSet, JogoCommand toBeSet) {
		
		toBeSet.setId(toSet.getId());
		
		if(toSet.isTerminado()) {
			toBeSet.setP1(paisToPaisCommand.convert(toSet.getP1()));
			toBeSet.setP2(paisToPaisCommand.convert(toSet.getP2()));
			toBeSet.setGols1(toSet.isTerminado() ? ""+toSet.getGols1() : "");
			toBeSet.setGols2(toSet.isTerminado() ? ""+toSet.getGols2() : "");
		}
		toBeSet.setFase(toSet.getFase());
		toBeSet.setData(toSet.getData());
		toBeSet.setNumeroDoJogo(toSet.getNumeroDoJogo());
		toBeSet.setEstadio(toSet.getEstadio());
		toBeSet.setTerminado(toSet.isTerminado());
		toBeSet.setVencedorPenaltis(paisToPaisCommand.convert(toSet.getVencedorPenaltis()));
	}
}
