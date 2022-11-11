package com.pvictorcr.bolaodacopa.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.pvictorcr.bolaodacopa.commands.EliminatoriaCommand;
import com.pvictorcr.bolaodacopa.commands.GrupoCommand;
import com.pvictorcr.bolaodacopa.commands.JogoCommand;
import com.pvictorcr.bolaodacopa.commands.TabelaCommand;
import com.pvictorcr.bolaodacopa.commands.UsuarioCommand;
import com.pvictorcr.bolaodacopa.converters.UsuarioToUsuarioCommand;
import com.pvictorcr.bolaodacopa.model.Jogo;
import com.pvictorcr.bolaodacopa.repositories.UsuarioRepository;

@Service
public class SimulacaoServiceImpl implements SimulacaoService {

	private final UsuarioRepository usuarioRepository;
	private final UsuarioToUsuarioCommand usuarioToUsuarioCommand;
	
	public SimulacaoServiceImpl(UsuarioRepository usuarioRepository, UsuarioToUsuarioCommand usuarioToUsuarioCommand) {
		
		this.usuarioRepository = usuarioRepository;
		this.usuarioToUsuarioCommand = usuarioToUsuarioCommand;
	}
	
	@Override
	public Set<Jogo> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Jogo findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Jogo save(Jogo object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Jogo object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<UsuarioCommand> getUsuariosOrdenados(TabelaCommand form) {

		List<UsuarioCommand> classificacaoSim = new ArrayList<UsuarioCommand>();
		List<UsuarioCommand> classificacaoAtual = new ArrayList<UsuarioCommand>();
		
		usuarioRepository.findAll().forEach(u -> classificacaoAtual.add(usuarioToUsuarioCommand.convert(u)));
		Collections.sort(classificacaoAtual, Collections.reverseOrder());
		
		for(UsuarioCommand uc : classificacaoAtual) {
			
			classificacaoSim.add(uc);
			
			for(GrupoCommand gc : form.getGrupos())
				for(JogoCommand jc : gc.getJogos())
					if(jc.getGols1() != null && !jc.getGols1().trim().isEmpty() &&
							jc.getGols2() != null && !jc.getGols2().trim().isEmpty())
						if(uc.getAposta().getJogo(jc.getNumeroDoJogo()).getJogo() != null)
							uc.getAposta().getJogo(jc.getNumeroDoJogo()).getJogo().setGols(jc.getGols1(), jc.getGols2(), true);
			
			for(EliminatoriaCommand ec : form.getEliminatorias())
				for(JogoCommand jc : ec.getJogos())
					if(jc.getGols1() != null && !jc.getGols1().trim().isEmpty() &&
							jc.getGols2() != null && !jc.getGols2().trim().isEmpty())
						if(uc.getAposta().getJogo(jc.getNumeroDoJogo()).getJogo() != null)
							uc.getAposta().getJogo(jc.getNumeroDoJogo()).getJogo().setGols(jc.getGols1(), jc.getGols2(), true);
			
		}
		Collections.sort(classificacaoSim, Collections.reverseOrder());
		
		for(int i = 0; i < classificacaoSim.size(); i++) {
			for(int j = 0; j < classificacaoAtual.size(); j++) {
				if(classificacaoSim.get(i).getEmail().equals(classificacaoAtual.get(j).getEmail())) {
					classificacaoSim.get(i).setDeslocamentoClassificacao(j-i);
					j = classificacaoAtual.size();
				}
			}
		}
		
		return classificacaoSim;
	}

}
