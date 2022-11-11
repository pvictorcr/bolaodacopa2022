package com.pvictorcr.bolaodacopa.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.pvictorcr.bolaodacopa.commands.PaisCommand;
import com.pvictorcr.bolaodacopa.converters.PaisToPaisCommand;
import com.pvictorcr.bolaodacopa.model.Pais;
import com.pvictorcr.bolaodacopa.repositories.PaisRepository;

@Service
public class PaisServiceImpl implements PaisService {
	
	private final PaisRepository paisRepository;
	private final PaisToPaisCommand paisToPaisCommand;
	
	public PaisServiceImpl(PaisRepository paisRepository, PaisToPaisCommand paisToPaisCommand) {
		
		this.paisRepository = paisRepository;
		this.paisToPaisCommand = paisToPaisCommand;
	}

	@Override
	public List<PaisCommand> findAllOrdered() {
		
		List<PaisCommand> paises = new ArrayList<PaisCommand>();
		
		paisRepository.findByOrderByNomeAsc().stream().forEach(p -> paises.add(paisToPaisCommand.convert(p)));
				
		return paises;
		
	}
	
	@Override
	public PaisCommand findByName(String nome) {
		
		Optional<Pais> op = paisRepository.findByNome(nome);
		PaisCommand pc = new PaisCommand();
		
		if(op.isPresent())
			pc = paisToPaisCommand.convert(op.get());
		
		return pc;
	}

	@Override
	public PaisCommand findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaisCommand save(PaisCommand object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(PaisCommand object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<PaisCommand> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
