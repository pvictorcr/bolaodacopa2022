package com.pvictorcr.bolaodacopa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.pvictorcr.bolaodacopa.model.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

	public Optional<Usuario> getUsuarioByEmail(@Param("email") String email);
	
	public List<Usuario> findByOrderByNomeAsc();
}
