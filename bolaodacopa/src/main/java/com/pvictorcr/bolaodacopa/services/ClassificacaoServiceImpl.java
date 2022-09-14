package com.pvictorcr.bolaodacopa.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.pvictorcr.bolaodacopa.commands.UsuarioCommand;
import com.pvictorcr.bolaodacopa.converters.UsuarioToUsuarioCommand;
import com.pvictorcr.bolaodacopa.model.Usuario;
import com.pvictorcr.bolaodacopa.repositories.UsuarioRepository;

@Service
public class ClassificacaoServiceImpl implements ClassificacaoService {

	private final UsuarioRepository usuarioRepository;
	private final UsuarioToUsuarioCommand usuarioToUsuarioCommand;
	
	public ClassificacaoServiceImpl(UsuarioRepository usuarioRepository, UsuarioToUsuarioCommand usuarioToUsuarioCommand) {
		this.usuarioRepository = usuarioRepository;
		this.usuarioToUsuarioCommand = usuarioToUsuarioCommand;
	}
	
	@Override
	public Set<Usuario> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario save(Usuario object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Usuario object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<UsuarioCommand> getUsuariosOrdenados() {

		List<UsuarioCommand> classificacao = new ArrayList<UsuarioCommand>();
		List<UsuarioCommand> classificacaoAnterior = new ArrayList<UsuarioCommand>();
		
		usuarioRepository.findAll().forEach(u -> classificacao.add(usuarioToUsuarioCommand.convert(u)));
		Collections.sort(classificacao, Collections.reverseOrder());
		
		for(UsuarioCommand uc : classificacao) {
			classificacaoAnterior.add(uc);
			uc.setClassificacaoAnterior(true);
			
		}
		Collections.sort(classificacaoAnterior, Collections.reverseOrder());
		
		for(int i = 0; i < classificacao.size(); i++) {
			for(int j = 0; j < classificacaoAnterior.size(); j++) {
				if(classificacao.get(i).getEmail().equals(classificacaoAnterior.get(j).getEmail())) {
					classificacao.get(i).setDeslocamentoClassificacao(j-i);
					j = classificacaoAnterior.size();
				}
			}
		}
		
		return classificacao;
	}

	@Override
	public List<UsuarioCommand> getUsuariosOrdenadosPorNome() {

		List<UsuarioCommand> classificacao = new ArrayList<UsuarioCommand>();
		
		usuarioRepository.findByOrderByNomeAsc().forEach(u -> classificacao.add(usuarioToUsuarioCommand.convert(u)));
		
		return classificacao;
	}
}
