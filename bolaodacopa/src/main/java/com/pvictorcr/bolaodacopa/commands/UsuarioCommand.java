package com.pvictorcr.bolaodacopa.commands;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

import com.pvictorcr.bolaodacopa.constants.Credenciais;
import com.pvictorcr.bolaodacopa.constants.Fases;
import com.pvictorcr.bolaodacopa.constants.NomeImagens;
import com.pvictorcr.bolaodacopa.constants.Pontos;
import com.pvictorcr.bolaodacopa.constants.Provider;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioCommand extends BaseCommand implements Comparable<UsuarioCommand> {

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
	
	public String getPontuacaoFormatada() {
		
		float pontuacao = getPontuacao();
		
		if(pontuacao == (long) pontuacao)
	        return String.format("%d",(long)pontuacao);
	    else
	        return String.format("%s",pontuacao);
	}
	
	public float getPontuacao() {
		
		float pontos = 0;
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
				pontos += calculaPontosGanhos(jac);
			}
		}	
		
		return pontos;
	}
	
	public List<Float> getHistoricoPontuacao() {
		
		List<Float> pontos = new ArrayList<Float>();
		pontos.add(0f);
		
		for(JogoApostaCommand jac : aposta.getJogosApostas()) {
			if(jac.getJogo().isTerminado()) {
				pontos.add(pontos.get(pontos.size()-1) + calculaPontosGanhos(jac));
			}
		}
		
		return pontos;
	}
	
	public float calculaPontosGanhos(JogoApostaCommand jac) {
		
		float acc = 0;
		int gols1A = jac.getGols1();
		int gols2A = jac.getGols2();
		int gols1R = Integer.parseInt(jac.getJogo().getGols1());
		int gols2R = Integer.parseInt(jac.getJogo().getGols2());
		
		if((gols1A == gols1R || (gols1A >= 4 && gols1R >= 4 )) && 
				(gols2A == gols2R || (gols2A >= 4 && gols2R >= 4 ))) //placar exato
			acc += Pontos.PONTOS[jac.getJogo().getFase().ordinal()][0];
		else {
			if((gols1A < gols2A && gols1R < gols2R) ||
					(gols1A > gols2A && gols1R > gols2R) ||
					(gols1A == gols2A && gols1R == gols2R)) //Resultado
				acc += Pontos.PONTOS[jac.getJogo().getFase().ordinal()][1];
			
			if(gols1A == gols1R || (gols1A >= 4 && gols1R >= 4 ) || gols2A == gols2R
					|| (gols2A >= 4 && gols2R >= 4 )) //1 placar
				acc += Pontos.PONTOS[jac.getJogo().getFase().ordinal()][2];
		}
		
		if(jac.getJogo().getFase() == Fases.FINAIS) {
			
			String campeaoA = aposta.getCampeao().getNome();
			String viceA = aposta.getViceCampeao().getNome();
			String time1R = jac.getJogo().getP1().getNome();
			String time2R = jac.getJogo().getP2().getNome();
		
			if((campeaoA.equals(time1R) && viceA.equals(time2R)) ||
					(campeaoA.equals(time2R) && viceA.equals(time1R))) //2 finalistas
				acc += 54;
			
			if((campeaoA.equals(time1R) && 
					((Integer.parseInt(jac.getJogo().getGols1()) > Integer.parseInt(jac.getJogo().getGols2())) ||
					(jac.getJogo().p1GanhouPenaltis()))) ||
					((campeaoA.equals(time2R) &&
					((Integer.parseInt(jac.getJogo().getGols2()) > Integer.parseInt(jac.getJogo().getGols1())) ||
					(jac.getJogo().p2GanhouPenaltis()))))) //campeao
				acc += 36;
			else if(campeaoA.equals(time1R) || viceA.equals(time2R) || campeaoA.equals(time2R) ||
					viceA.equals(time1R)) // finalista
				acc += 18;
		}
		
		
		return acc;
	}

	@Override
	public int compareTo(UsuarioCommand o) {
		
		float pontos = getPontuacao();
		float adv = o.getPontuacao();
		
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
	
	public String getPorcentagemJogosPreenchidos() {
		
		int apostados = 0;
		
		for(JogoApostaCommand ja : aposta.getJogosApostas()) {
			if(aposta.jogoExiste(ja.getJogo().getNumeroDoJogo())) {
				++apostados;
			}
		}
		
		return (100*apostados/TabelaCommand.TOTAL_JOGOS) + "%";
	}
}
