package com.pvictorcr.bolaodacopa.commands;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

import com.pvictorcr.bolaodacopa.constants.Credenciais;
import com.pvictorcr.bolaodacopa.constants.NomeImagens;
import com.pvictorcr.bolaodacopa.constants.Pontos;
import com.pvictorcr.bolaodacopa.constants.Provider;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioCommand  extends BaseCommand implements Comparable<UsuarioCommand> {

	private String nome;
	
	private String email;
	
	private Credenciais credencial;
	
	@Enumerated(EnumType.STRING)
	private Provider provider;
	
	@OneToOne(cascade = CascadeType.ALL)
	private ApostaCommand aposta;
	
	private int deslocamentoClassificacao;
	
	private boolean classificacaoAnterior;
	
	public boolean isAdmin() {
		return credencial == Credenciais.ADMIN;
	}
	
	public int getPontuacao() {
		
		int pontos = 0;
		JogoCommand ultimoJogo = new JogoCommand();
		List<JogoApostaCommand> apostas = aposta.getJogosApostas();
		
		if(classificacaoAnterior) {
			for(int i = apostas.size() - 1; i >=0; i--) 
				if(apostas.get(i).getJogo().isTerminado()) {
					ultimoJogo = apostas.get(i).getJogo();
					i = -1;
				}
		}
		
		for(JogoApostaCommand jac : apostas) {
			if(jac.getJogo().isTerminado() && jac.getJogo().getNumeroDoJogo() != ultimoJogo.getNumeroDoJogo()) {
				if(jac.getGols1() == Integer.parseInt(jac.getJogo().getGols1()) && 
						jac.getGols2() == Integer.parseInt(jac.getJogo().getGols2())) //placar
					pontos += Pontos.PONTOS[jac.getJogo().getFase().ordinal()][0];
				else if((jac.getGols1() < jac.getGols2() && 
					Integer.parseInt(jac.getJogo().getGols1()) < Integer.parseInt(jac.getJogo().getGols2())) ||
						(jac.getGols1() > jac.getGols2() && 
							Integer.parseInt(jac.getJogo().getGols1()) > Integer.parseInt(jac.getJogo().getGols2())) ||
						(jac.getGols1() == jac.getGols2() && 
								Integer.parseInt(jac.getJogo().getGols1()) == Integer.parseInt(jac.getJogo().getGols2()))) //Resultado
					pontos += Pontos.PONTOS[jac.getJogo().getFase().ordinal()][1];
			}
		}	
		
		return pontos;
	}
	
	public List<Integer> getHistoricoPontuacao() {
		
		List<Integer> pontos = new ArrayList<Integer>();
		
		for(JogoApostaCommand jac : aposta.getJogosApostas()) {
			if(jac.getJogo().isTerminado()) {
				if(jac.getGols1() == Integer.parseInt(jac.getJogo().getGols1()) && 
						jac.getGols2() == Integer.parseInt(jac.getJogo().getGols2())) //placar
					pontos.add((pontos.size() > 0 ? pontos.get(pontos.size()-1) : 0) + Pontos.PONTOS[jac.getJogo().getFase().ordinal()][0]);
				else if((jac.getGols1() < jac.getGols2() && 
					Integer.parseInt(jac.getJogo().getGols1()) < Integer.parseInt(jac.getJogo().getGols2())) ||
						(jac.getGols1() > jac.getGols2() && 
							Integer.parseInt(jac.getJogo().getGols1()) > Integer.parseInt(jac.getJogo().getGols2())) ||
						(jac.getGols1() == jac.getGols2() && 
								Integer.parseInt(jac.getJogo().getGols1()) == Integer.parseInt(jac.getJogo().getGols2()))) //Resultado
					pontos.add((pontos.size() > 0 ? pontos.get(pontos.size()-1) : 0) + Pontos.PONTOS[jac.getJogo().getFase().ordinal()][1]);
				else
					pontos.add((pontos.size() > 0 ? pontos.get(pontos.size()-1) : 0));
			}
		}
		
		return pontos;
	}

	@Override
	public int compareTo(UsuarioCommand o) {
		
		int pontos = getPontuacao();
		int adv = o.getPontuacao();
		
		if(pontos > adv)
			return 1;
		if(pontos < adv)
			return -1;
		
		return 0;
	}
	
	public String getNomeImagemClassificacao() {
		
		if(deslocamentoClassificacao > 0)
			return NomeImagens.PARACIMA;
		if(deslocamentoClassificacao < 0)
			return NomeImagens.PARABAIXO;
		
		return NomeImagens.MANTEM;
	}
}
