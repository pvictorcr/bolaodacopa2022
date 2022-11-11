package com.pvictorcr.bolaodacopa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.pvictorcr.bolaodacopa.model.Pais;

public interface PaisRepository extends CrudRepository<Pais, Long> {

	public List<Pais> findByOrderByNomeAsc();
	
	public Optional<Pais> findByNome(String nome);
}
