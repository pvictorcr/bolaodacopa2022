package com.pvictorcr.bolaodacopa.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.pvictorcr.bolaodacopa.commands.PaisCommand;
import com.pvictorcr.bolaodacopa.model.Pais;

@Component
public class PaisCommandoToPais implements Converter<PaisCommand, Pais> {

	@Override
	public Pais convert(PaisCommand command) {

		if(command == null)
			return null;
		
		final Pais pais = new Pais(command.getNome(), command.getGrupo());
		pais.setId(command.getId());
		
		return pais;
	}

}
