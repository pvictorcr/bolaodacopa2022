package com.pvictorcr.bolaodacopa.repositories;

import org.springframework.data.repository.CrudRepository;

import com.pvictorcr.bolaodacopa.model.Aposta;

public interface ApostaRepository extends CrudRepository<Aposta, Long> {

	public long deleteByJogosApostasId(long id);
}
