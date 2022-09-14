package com.pvictorcr.bolaodacopa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.pvictorcr.bolaodacopa.constants.Fases;
import com.pvictorcr.bolaodacopa.model.Jogo;

public interface JogoRepository extends CrudRepository<Jogo, Long> {

	List<Jogo> findByFase(Fases fase);
	
	Optional<Jogo> findByNumeroDoJogo(int numeroJogo);
}
