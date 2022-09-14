package com.pvictorcr.bolaodacopa.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.pvictorcr.bolaodacopa.model.JogoAposta;

public interface JogoApostaRepository extends CrudRepository<JogoAposta, Long> {

	Optional<JogoAposta> findByApostaIdAndJogoNumeroDoJogo(long id, int numeroJogo);
}
