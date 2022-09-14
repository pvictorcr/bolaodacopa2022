package com.pvictorcr.bolaodacopa.converters;

import java.text.Normalizer;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.pvictorcr.bolaodacopa.commands.PaisCommand;
import com.pvictorcr.bolaodacopa.model.Pais;

@Component
public class PaisToPaisCommand implements Converter<Pais, PaisCommand> {

	@Override
	public PaisCommand convert(Pais source) {

		if(source == null) {
			return new PaisCommand();
		}
		
		PaisCommand command = new PaisCommand();
		
		command.setNome(source.getNome());
		command.setGrupo(source.getGrupo());
		String semAcentos = Normalizer.normalize(source.getNome(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
		command.setNomeImg(semAcentos.toLowerCase().replace(' ', '_'));
		command.setSigla(semAcentos.toUpperCase().substring(0, 3));
		
		command.setId(source.getId());
		
		return command;
	}

}
