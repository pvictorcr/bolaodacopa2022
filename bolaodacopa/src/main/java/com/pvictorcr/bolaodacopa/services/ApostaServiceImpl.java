package com.pvictorcr.bolaodacopa.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.pvictorcr.bolaodacopa.commands.ApostaCommand;
import com.pvictorcr.bolaodacopa.commands.JogoApostaCommand;
import com.pvictorcr.bolaodacopa.commands.UsuarioCommand;
import com.pvictorcr.bolaodacopa.converters.JogoApostaCommandToJogoAposta;
import com.pvictorcr.bolaodacopa.converters.UsuarioToUsuarioCommand;
import com.pvictorcr.bolaodacopa.model.Aposta;
import com.pvictorcr.bolaodacopa.model.JogoAposta;
import com.pvictorcr.bolaodacopa.model.Usuario;
import com.pvictorcr.bolaodacopa.repositories.ApostaRepository;
import com.pvictorcr.bolaodacopa.repositories.JogoApostaRepository;
import com.pvictorcr.bolaodacopa.repositories.UsuarioRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApostaServiceImpl implements ApostaService {

	private final UsuarioRepository usuarioRepository;
	private final UsuarioToUsuarioCommand usuarioToUsuarioCommand;
	private final ApostaRepository apostaRepository;
	private final JogoApostaRepository jogoApostaRepository;
	private final JogoApostaCommandToJogoAposta jogoApostaCommandToJogoAposta;
	
	public ApostaServiceImpl(ApostaRepository apostaRepository, UsuarioRepository usuarioRepository, UsuarioToUsuarioCommand usuarioToUsuarioCommand,
			JogoApostaRepository jogoApostaRepository, JogoApostaCommandToJogoAposta jogoApostaCommandToJogoAposta) {
		
		this.usuarioRepository = usuarioRepository;
		this.usuarioToUsuarioCommand = usuarioToUsuarioCommand;
		this.apostaRepository = apostaRepository;
		this.jogoApostaRepository = jogoApostaRepository;
		this.jogoApostaCommandToJogoAposta = jogoApostaCommandToJogoAposta;
	}
	
	@Override
	public Set<JogoAposta> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JogoAposta findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JogoAposta save(JogoAposta object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(JogoAposta object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
	}
	
	public List<UsuarioCommand> getUsuariosOrdenados(){
		
		List<UsuarioCommand> commands = new ArrayList<UsuarioCommand>();
		List<Usuario> usuarios = new ArrayList<Usuario>();
		usuarioRepository.findAll().iterator().forEachRemaining(usuarios::add);
		
		for(Usuario u : usuarios) {
			commands.add(usuarioToUsuarioCommand.convert(u));
		}
		
		Collections.sort(commands, Collections.reverseOrder());
		
		return commands;
	}
	
	public int getPosicao(UsuarioCommand u) {
		
		List<UsuarioCommand> classificacao = getUsuariosOrdenados();
		
		int posicao = 1;
		for(UsuarioCommand uc : classificacao)
			if(u.getId() != uc.getId())
				++posicao;
			else
				return posicao;
		
		return 0;
	}
	
	public String saveApostaCommand(ApostaCommand aposta, JogoApostaCommand command) {
		
		Optional<Aposta> a = apostaRepository.findById(aposta.getId());
		
		if(!a.isPresent()) {
			log.error("Aposta numero '" + aposta.getId() + "' nao esta presente no BD");
			return "Aposta numero '" + aposta.getId() + "' nao esta presente no BD";
		}
		
		Optional<JogoAposta> detachedJogo = jogoApostaRepository.findByApostaIdAndJogoNumeroDoJogo(aposta.getId(), command.getJogo().getNumeroDoJogo());
		JogoAposta j;
		
		if(!detachedJogo.isPresent())
			j = jogoApostaCommandToJogoAposta.convert(command);
		else
			j = detachedJogo.get();

		a.get().addJogoAposta(j);
		
		try {
			Aposta saved = apostaRepository.save(a.get());
			log.info("Jogo-Aposta numero '" + j.getId() + "' salvo com placar '" + j.getGols1() + "x" + j.getGols2() + "' na aposta '" + saved.getId() + "'");
			
			return "";
		}
		catch(Exception e) {
			log.error("Tentativa de salvar o jogo '" + command.getJogo().getNumeroDoJogo() + "' falhou: " + e);
			return "Erro ao tentar salvar o jogo-aposta '" + command.getJogo().getP1().getNome() + " x " + command.getJogo().getP2().getNome() + "'";
		}
	}
	
	@Transactional
	public boolean apagaJogoAposta(ApostaCommand aposta, JogoApostaCommand command) {
		
		Optional<JogoAposta> ap = jogoApostaRepository.findByApostaIdAndJogoNumeroDoJogo(aposta.getId(), command.getJogo().getNumeroDoJogo());
		
		if(!ap.isPresent())
			return false;
		
		Optional<Aposta> a = apostaRepository.findById(aposta.getId());
		a.get().removeJogoAposta(ap.get());
		
		apostaRepository.save(a.get());
		
		return true;
	}
}
