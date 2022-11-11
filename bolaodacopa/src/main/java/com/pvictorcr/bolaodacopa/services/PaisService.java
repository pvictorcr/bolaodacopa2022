package com.pvictorcr.bolaodacopa.services;

import java.util.List;

import com.pvictorcr.bolaodacopa.commands.PaisCommand;

public interface PaisService extends CrudService<PaisCommand, Long> {

	public List<PaisCommand> findAllOrdered();
	public PaisCommand findByName(String nome);
}
