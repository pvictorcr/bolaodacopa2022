package com.pvictorcr.bolaodacopa.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.pvictorcr.bolaodacopa.commands.GrupoCommand;
import com.pvictorcr.bolaodacopa.commands.JogoCommand;
import com.pvictorcr.bolaodacopa.commands.PaisCommand;
import com.pvictorcr.bolaodacopa.commands.TabelaCommand;
import com.pvictorcr.bolaodacopa.constants.Fases;
import com.pvictorcr.bolaodacopa.converters.JogoCommandToJogo;
import com.pvictorcr.bolaodacopa.converters.JogoToJogoCommand;
import com.pvictorcr.bolaodacopa.converters.PaisCommandoToPais;
import com.pvictorcr.bolaodacopa.model.Jogo;
import com.pvictorcr.bolaodacopa.repositories.JogoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TabelaServiceImpl implements TabelaService {

	private final JogoRepository jogoRepository;
	private final JogoToJogoCommand jogoToJogoCommand;
	private final JogoCommandToJogo jogoCommandoToJogo;
	private final PaisCommandoToPais paisCommandoToPais;
	
	private final SimpleDateFormat formatter = new SimpleDateFormat("d-MMM-yy HH:mm");
	
	public TabelaServiceImpl(JogoRepository jogoRepository, JogoToJogoCommand jogoToJogoCommand,
			PaisCommandoToPais paisCommandoToPais, JogoCommandToJogo jogoCommandoToJogo) {
		
		this.jogoRepository = jogoRepository;
		this.jogoToJogoCommand = jogoToJogoCommand;
		this.jogoCommandoToJogo = jogoCommandoToJogo;
		this.paisCommandoToPais = paisCommandoToPais;
	}
	
	public List<JogoCommand> findAllCommands() {

		List<JogoCommand> jogoComs = new ArrayList<JogoCommand>();
        List<Jogo> jogos = new ArrayList<Jogo>();
        jogoRepository.findAll().iterator().forEachRemaining(jogos::add);
        
        for(Jogo j : jogos)
        	jogoComs.add(jogoToJogoCommand.convert(j));
        
        return jogoComs;
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
	public TabelaCommand findTabelaGruposCommands() {

		TabelaCommand tabela = new TabelaCommand();
		GrupoCommand[] grupos = new GrupoCommand['H' - 'A' + 1];
		
        List<Jogo> jogos = new ArrayList<Jogo>();
        jogoRepository.findByFase(Fases.GRUPOS).iterator().forEachRemaining(jogos::add);
        
        if(jogos.size() > 0) {

	        for(char grupo = 'A'; grupo <= 'H'; grupo++) {
	        	grupos[grupo - 'A'] = new GrupoCommand();
	        	grupos[grupo - 'A'].setGrupo(grupo);
	        }
	        
	        for(Jogo j : jogos) {
	        	grupos[j.getP1().getGrupo() - 'A'].getJogos().add(jogoToJogoCommand.convert(j)); 
	        }
	        
	        for(GrupoCommand grupo : grupos)
	        	grupo.setClassificacao(getClassificacao(grupo.getJogos()));
	        
	        tabela.setGrupos(new ArrayList<GrupoCommand>(Arrays.asList(grupos)));
	        
	        if(tabela.isFaseDeGruposTerminada()) {
		        try {
					this.preencherOitavas(tabela);
				} catch (ParseException e) {
					log.error("ERRO: " + e);
					e.printStackTrace();
				}
	        }
        }
        
        return tabela;
	}
	
	public String saveJogoCommand(JogoCommand command) {
		
		Optional<Jogo> detachedJogo = jogoRepository.findByNumeroDoJogo(command.getNumeroDoJogo());
		
		if(!detachedJogo.isPresent()) {
			log.error("Tentativa de salvar o jogo '" + command.getNumeroDoJogo() + "' falhou: jogo nao encontrado.");
			return "Erro ao tentar salvar o jogo '" + command.getP1().getNome() + " x " + command.getP2().getNome() + "'";
		}
		
		try {
			Jogo j = detachedJogo.get();
			j.setGols1(Integer.parseInt(command.getGols1()));
			j.setGols2(Integer.parseInt(command.getGols2()));
			j.setTerminado(true);
			if(command.getVencedorPenaltis() != null)
				j.setVencedorPenaltis(paisCommandoToPais.convert(command.getVencedorPenaltis()));
			
			j = jogoRepository.save(j);
			log.debug("Jogo numero '" + j.getNumeroDoJogo() + "' salvo com placar '" + j.getGols1() + "x" + j.getGols2() + "'");
			
			return "";
		}
		catch(Exception e) {
			log.error("Tentativa de salvar o jogo '" + command.getNumeroDoJogo() + "' falhou: " + e);
			return "Erro ao tentar salvar o jogo '" + command.getP1().getNome() + " x " + command.getP2().getNome() + "'";
		}
	}
	
	public String reativarJogo(JogoCommand command) {
		
		Optional<Jogo> detachedJogo = jogoRepository.findByNumeroDoJogo(command.getNumeroDoJogo());
		
		if(!detachedJogo.isPresent()) {
			log.error("Tentativa de reativar o jogo '" + command.getNumeroDoJogo() + "' falhou: jogo nao encontrado.");
			return "Erro ao tentar reativar o jogo '" + command.getP1().getNome() + " x " + command.getP2().getNome() + "'";
		}
		
		try {
			Jogo j = detachedJogo.get();
			j.setTerminado(false);
			j.setVencedorPenaltis(null);
			
			j = jogoRepository.save(j);
			log.debug("Jogo numero '" + j.getNumeroDoJogo() + "' reativado.");
			
			return "";
		}
		catch(Exception e) {
			log.error("Tentativa de reativar o jogo '" + command.getNumeroDoJogo() + "' falhou: " + e);
			return "Erro ao tentar reativar o jogo '" + command.getP1().getNome() + " x " + command.getP2().getNome() + "'";
		}
	}
	
	public PaisCommand getPaisFromJogo(JogoCommand jc, int pais) {
		
		Optional<Jogo> j = jogoRepository.findByNumeroDoJogo(jc.getNumeroDoJogo());
		
		if(!j.isPresent())
			log.error("Tentativa de pegar o pais " + pais + " do Jogo '" + jc.getNumeroDoJogo() + "' falhou: nao existe no BD.");
		
		PaisCommand pc;
		
		if(pais == 0)
			pc = jogoToJogoCommand.convert(j.get()).getP1();
		else
			pc = jogoToJogoCommand.convert(j.get()).getP2();
		
		return pc;
	}
	
	private List<PaisCommand> getClassificacao(List<JogoCommand> jogoComs){
		
		
		List<PaisCommand> ret = new ArrayList<PaisCommand>();
		PaisCommand p1;
		PaisCommand p2;
		
		for(JogoCommand jc : jogoComs) {
			
			if(jc.isTerminado()) {
				p1 = elementInList(ret, jc.getP1());
				if(p1 == null) {
					p1 = jc.getP1();
					ret.add(p1);
				}
				
				p2 = elementInList(ret, jc.getP2());
				if(p2 == null) {
					p2 = jc.getP2();
					ret.add(p2);
				}
				
				p1.setJogos(p1.getJogos()+1);
				p2.setJogos(p2.getJogos()+1);
				
				if(Integer.parseInt(jc.getGols1()) > Integer.parseInt(jc.getGols2())) { //Time 1 ganhou
					p1.setVitorias(p1.getVitorias()+1);
					p1.setPontos(p1.getPontos()+3);
					p2.setDerrotas(p2.getDerrotas()+1);
				}
				else if(Integer.parseInt(jc.getGols1()) < Integer.parseInt(jc.getGols2())) { //Time 2 ganhou
					p2.setVitorias(p2.getVitorias()+1);
					p2.setPontos(p2.getPontos()+3);
					p1.setDerrotas(p1.getDerrotas()+1);
				}
				else { //empate
					p1.setEmpates(p1.getEmpates()+1);
					p1.setPontos(p1.getPontos()+1);
					p2.setEmpates(p2.getEmpates()+1);
					p2.setPontos(p2.getPontos()+1);
				}
				
				p1.setGp(p1.getGp()+Integer.parseInt(jc.getGols1()));
				p1.setGc(p1.getGc()+Integer.parseInt(jc.getGols2()));
				p2.setGp(p2.getGp()+Integer.parseInt(jc.getGols2()));
				p2.setGc(p2.getGc()+Integer.parseInt(jc.getGols1()));
			}
			
			Collections.sort(ret, Collections.reverseOrder());
		}
		
		return ret;
	}
	
	private PaisCommand elementInList(List<PaisCommand> list, PaisCommand element) {
		
		for(PaisCommand p : list)
			if(p.getNome().equals(element.getNome()))
				return p;
		
		return null;
	}
	
	private void preencherOitavas(TabelaCommand tabela) throws ParseException {
		
		PaisCommand[] primeiro = new PaisCommand[8]; //primeiros dos grupos
		PaisCommand[] segundo = new PaisCommand[8]; //segundos dos grupos
		
		
		//define os times classificados
		for(int i = 0; i < primeiro.length; i++) {
			primeiro[i] = tabela.getGrupos().get(i).isTerminado() ? tabela.getGrupos().get(i).getClassificacao().get(0) : new PaisCommand();
			segundo[i] = tabela.getGrupos().get(i).isTerminado() ? tabela.getGrupos().get(i).getClassificacao().get(1) : new PaisCommand();
		}
		
		//define as partidas das 8as
		tabela.getEliminatorias()[Fases.OITAVAS.ordinal()].getJogos().add(new JogoCommand(primeiro[0], segundo[1], //A1 x B2
				Fases.OITAVAS, formatter.parse("03-Dez-22 18:00"), 49, "Khalifa International Stadium", "0", "0", false, null));
		tabela.getEliminatorias()[Fases.OITAVAS.ordinal()].getJogos().add(new JogoCommand(primeiro[2], segundo[3], //C1 x D2
				Fases.OITAVAS, formatter.parse("03-Dez-22 22:00"), 50, "Ahmad Bin Ali Stadium", "0", "0", false, null));
		tabela.getEliminatorias()[Fases.OITAVAS.ordinal()].getJogos().add(new JogoCommand(primeiro[1], segundo[0], //B1 x A2
				Fases.OITAVAS, formatter.parse("04-Dez-22 22:00"), 51, "Al Bayt Stadium", "0", "0", false, null));
		tabela.getEliminatorias()[Fases.OITAVAS.ordinal()].getJogos().add(new JogoCommand(primeiro[3], segundo[2], //D1 x C2
				Fases.OITAVAS, formatter.parse("04-Dez-22 18:00"), 52, "Al Thumama Stadium", "0", "0", false, null));
		tabela.getEliminatorias()[Fases.OITAVAS.ordinal()].getJogos().add(new JogoCommand(primeiro[4], segundo[5], //E1 x F2
				Fases.OITAVAS, formatter.parse("05-Dez-22 18:00"), 53, "Al Janoub Stadium", "0", "0", false, null));
		tabela.getEliminatorias()[Fases.OITAVAS.ordinal()].getJogos().add(new JogoCommand(primeiro[6], segundo[7], //G1 x H2
				Fases.OITAVAS, formatter.parse("05-Dez-22 22:00"), 54, "Stadium 974", "0", "0", false, null));
		tabela.getEliminatorias()[Fases.OITAVAS.ordinal()].getJogos().add(new JogoCommand(primeiro[5], segundo[4], //F1 x E2
				Fases.OITAVAS, formatter.parse("06-Dez-22 18:00"), 55, "Education City Stadium", "0", "0", false, null));
		tabela.getEliminatorias()[Fases.OITAVAS.ordinal()].getJogos().add(new JogoCommand(primeiro[7], segundo[6], //H1 x G2
				Fases.OITAVAS, formatter.parse("06-Dez-22 22:00"), 56, "Lusail Stadium", "0", "0", false, null));
		
		List<Jogo> oitavas = new ArrayList<Jogo>();
		jogoRepository.findByFase(Fases.OITAVAS).iterator().forEachRemaining(oitavas::add);
		
		//coloca os valores dos jogos do BD na tabela
		for(JogoCommand jc : tabela.getEliminatorias()[Fases.OITAVAS.ordinal()].getJogos()) {
			Optional<Jogo> jogo = oitavas.stream().filter(j -> j.getNumeroDoJogo() == jc.getNumeroDoJogo()).findFirst();
			if(!jogo.isPresent()) {
				Jogo js = jogoCommandoToJogo.convert(jc); //salva se o jogo ainda nao existir no BD
				jogoRepository.save(js);
			}
			else {
				jogoToJogoCommand.setAllFields(jogo.get(), jc);
			}
		}
		
		//Ativa a eliminatoria
        tabela.getEliminatorias()[Fases.OITAVAS.ordinal()].setAtivo(true);
		
		if(tabela.getEliminatorias()[Fases.OITAVAS.ordinal()].isTerminada())
			preencherQuartas(tabela);
	}
	
	private void preencherQuartas(TabelaCommand tabela) throws ParseException {
		
		//Pega os classificados
		PaisCommand[] classificados = new PaisCommand[8];
		
		for(int i = 0; i < classificados.length; i++) {
			if(tabela.getEliminatorias()[Fases.OITAVAS.ordinal()].getJogos().get(i).isTerminado()) {
				if(Integer.parseInt(tabela.getEliminatorias()[Fases.OITAVAS.ordinal()].getJogos().get(i).getGols1()) > Integer.parseInt(tabela.getEliminatorias()[Fases.OITAVAS.ordinal()].getJogos().get(i).getGols2()))
					classificados[i] = tabela.getEliminatorias()[Fases.OITAVAS.ordinal()].getJogos().get(i).getP1();
				else if(Integer.parseInt(tabela.getEliminatorias()[Fases.OITAVAS.ordinal()].getJogos().get(i).getGols1()) < Integer.parseInt(tabela.getEliminatorias()[Fases.OITAVAS.ordinal()].getJogos().get(i).getGols2()))
					classificados[i] = tabela.getEliminatorias()[Fases.OITAVAS.ordinal()].getJogos().get(i).getP2();
				else classificados[i] = tabela.getEliminatorias()[Fases.OITAVAS.ordinal()].getJogos().get(i).getVencedorPenaltis();
			}
			else {
				classificados[i] = new PaisCommand();
			}
		}
		
		//adiciona os jogos na eliminatoria
		tabela.getEliminatorias()[Fases.QUARTAS.ordinal()].getJogos().add(new JogoCommand(classificados[0], classificados[1], //49x50
				Fases.QUARTAS, formatter.parse("09-Dez-22 22:00"), 57, "Lusail Stadium", "0", "0", false, null));
		tabela.getEliminatorias()[Fases.QUARTAS.ordinal()].getJogos().add(new JogoCommand(classificados[4], classificados[5], //53x54
				Fases.QUARTAS, formatter.parse("09-Dez-22 18:00"), 58, "Education City Stadium", "0", "0", false, null));
		tabela.getEliminatorias()[Fases.QUARTAS.ordinal()].getJogos().add(new JogoCommand(classificados[2], classificados[3], //51x52
				Fases.QUARTAS, formatter.parse("10-Dez-22 22:00"), 59, "Al Bayt Stadium", "0", "0", false, null));
		tabela.getEliminatorias()[Fases.QUARTAS.ordinal()].getJogos().add(new JogoCommand(classificados[6], classificados[7], //55x56
				Fases.QUARTAS, formatter.parse("10-Dez-22 18:00"), 60, "Al Thumama Stadium", "0", "0", false, null));
		
		
		List<Jogo> quartas = new ArrayList<Jogo>();
		jogoRepository.findByFase(Fases.QUARTAS).iterator().forEachRemaining(quartas::add);
		
		//coloca os valores dos jogos do BD na tabela
		for(JogoCommand jc : tabela.getEliminatorias()[Fases.QUARTAS.ordinal()].getJogos()) {
			Optional<Jogo> jogo = quartas.stream().filter(j -> j.getNumeroDoJogo() == jc.getNumeroDoJogo()).findFirst();
			if(!jogo.isPresent()) {
				Jogo js = jogoCommandoToJogo.convert(jc); //salva se o jogo ainda nao existir no BD
				jogoRepository.save(js);
			}
			else {
				jogoToJogoCommand.setAllFields(jogo.get(), jc);
			}
		}
		
		//Ativa a eliminatoria
		tabela.getEliminatorias()[Fases.QUARTAS.ordinal()].setAtivo(true);
		
		if(tabela.getEliminatorias()[Fases.QUARTAS.ordinal()].isTerminada())
			preencherSemis(tabela);
	}

	private void preencherSemis(TabelaCommand tabela) throws ParseException {
		
		//Pega os classificados
		PaisCommand[] classificados = new PaisCommand[4];
		
		for(int i = 0; i < classificados.length; i++) {
			if(tabela.getEliminatorias()[Fases.QUARTAS.ordinal()].getJogos().get(i).isTerminado()) {
				if(Integer.parseInt(tabela.getEliminatorias()[Fases.QUARTAS.ordinal()].getJogos().get(i).getGols1()) > Integer.parseInt(tabela.getEliminatorias()[Fases.QUARTAS.ordinal()].getJogos().get(i).getGols2()))
					classificados[i] = tabela.getEliminatorias()[Fases.QUARTAS.ordinal()].getJogos().get(i).getP1();
				else if(Integer.parseInt(tabela.getEliminatorias()[Fases.QUARTAS.ordinal()].getJogos().get(i).getGols1()) < Integer.parseInt(tabela.getEliminatorias()[Fases.QUARTAS.ordinal()].getJogos().get(i).getGols2()))
					classificados[i] = tabela.getEliminatorias()[Fases.QUARTAS.ordinal()].getJogos().get(i).getP2();
				else classificados[i] = tabela.getEliminatorias()[Fases.QUARTAS.ordinal()].getJogos().get(i).getVencedorPenaltis();
			}
			else {
				classificados[i] = new PaisCommand();
			}
		}
		
		//adiciona os jogos na eliminatoria
		tabela.getEliminatorias()[Fases.SEMIFINAIS.ordinal()].getJogos().add(new JogoCommand(classificados[0], classificados[1],
				Fases.SEMIFINAIS, formatter.parse("13-Dez-22 22:00"), 61, "Lusail Stadium", "0", "0", false, null));
		tabela.getEliminatorias()[Fases.SEMIFINAIS.ordinal()].getJogos().add(new JogoCommand(classificados[2], classificados[3],
				Fases.SEMIFINAIS, formatter.parse("14-Dez-22 22:00"), 62, "Al Bayt Stadium", "0", "0", false, null));
		
		//coloca os valores dos jogos do BD na tabela
		List<Jogo> semis = new ArrayList<Jogo>();
		jogoRepository.findByFase(Fases.SEMIFINAIS).iterator().forEachRemaining(semis::add);
		
		//coloca os valores dos jogos do BD na tabela
		for(JogoCommand jc : tabela.getEliminatorias()[Fases.SEMIFINAIS.ordinal()].getJogos()) {
			Optional<Jogo> jogo = semis.stream().filter(j -> j.getNumeroDoJogo() == jc.getNumeroDoJogo()).findFirst();
			if(!jogo.isPresent()) {
				Jogo js = jogoCommandoToJogo.convert(jc); //salva se o jogo ainda nao existir no BD
				jogoRepository.save(js);
			}
			else {
				jogoToJogoCommand.setAllFields(jogo.get(), jc);
			}
		}
		
		//Ativa a eliminatoria
		tabela.getEliminatorias()[Fases.SEMIFINAIS.ordinal()].setAtivo(true);
		
		if(tabela.getEliminatorias()[Fases.SEMIFINAIS.ordinal()].isTerminada())
			preencherFinais(tabela);
	}
	
	private void preencherFinais(TabelaCommand tabela) throws ParseException {
		
		//Pega os classificados
		PaisCommand[] classificados = new PaisCommand[2];
		PaisCommand[] disputaTerceiro = new PaisCommand[2];
		
		for(int i = 0; i < classificados.length; i++) {
			if(tabela.getEliminatorias()[Fases.SEMIFINAIS.ordinal()].getJogos().get(i).isTerminado()) {
				if(Integer.parseInt(tabela.getEliminatorias()[Fases.SEMIFINAIS.ordinal()].getJogos().get(i).getGols1()) > Integer.parseInt(tabela.getEliminatorias()[Fases.SEMIFINAIS.ordinal()].getJogos().get(i).getGols2())) {
					classificados[i] = tabela.getEliminatorias()[Fases.SEMIFINAIS.ordinal()].getJogos().get(i).getP1();
					disputaTerceiro[i] = tabela.getEliminatorias()[Fases.SEMIFINAIS.ordinal()].getJogos().get(i).getP2();
				}
				else if(Integer.parseInt(tabela.getEliminatorias()[Fases.SEMIFINAIS.ordinal()].getJogos().get(i).getGols1()) < Integer.parseInt(tabela.getEliminatorias()[Fases.SEMIFINAIS.ordinal()].getJogos().get(i).getGols2())) {
					classificados[i] = tabela.getEliminatorias()[Fases.SEMIFINAIS.ordinal()].getJogos().get(i).getP2();
					disputaTerceiro[i] = tabela.getEliminatorias()[Fases.SEMIFINAIS.ordinal()].getJogos().get(i).getP1();
				}
				else {
					classificados[i] = tabela.getEliminatorias()[Fases.SEMIFINAIS.ordinal()].getJogos().get(i).getVencedorPenaltis();
					disputaTerceiro[i] = 
							classificados[i].getNome().equals(tabela.getEliminatorias()[Fases.SEMIFINAIS.ordinal()].getJogos().get(i).getP1().getNome()) ? 
											tabela.getEliminatorias()[Fases.SEMIFINAIS.ordinal()].getJogos().get(i).getP2() : tabela.getEliminatorias()[Fases.SEMIFINAIS.ordinal()].getJogos().get(i).getP1();
				}
			}
			else {
				classificados[i] = new PaisCommand();
				disputaTerceiro[i] = new PaisCommand();
			}
		}
		
		//adiciona os jogos na eliminatoria
		tabela.getEliminatorias()[Fases.DISPUTATERCEIRO.ordinal()].getJogos().add(new JogoCommand(disputaTerceiro[0], disputaTerceiro[1],
				Fases.DISPUTATERCEIRO, formatter.parse("17-Dez-22 18:00"), 63, "Khalifa International Stadium", "0", "0", false, null));
		tabela.getEliminatorias()[Fases.FINAIS.ordinal()].getJogos().add(new JogoCommand(classificados[0], classificados[1],
				Fases.FINAIS, formatter.parse("18-Dez-22 18:00"), 64, "Lusail Stadium", "0", "0", false, null));
		
		//coloca os valores dos jogos do BD na tabela
		List<Jogo> finalissima = new ArrayList<Jogo>();
		List<Jogo> terceiro = new ArrayList<Jogo>();
		jogoRepository.findByFase(Fases.DISPUTATERCEIRO).iterator().forEachRemaining(terceiro::add);
		jogoRepository.findByFase(Fases.FINAIS).iterator().forEachRemaining(finalissima::add);
		
		//coloca os valores dos jogos do BD na tabela
		for(JogoCommand jc : tabela.getEliminatorias()[Fases.DISPUTATERCEIRO.ordinal()].getJogos()) {
			Optional<Jogo> jogo = terceiro.stream().filter(j -> j.getNumeroDoJogo() == jc.getNumeroDoJogo()).findFirst();
			if(!jogo.isPresent()) {
				Jogo js = jogoCommandoToJogo.convert(jc); //salva se o jogo ainda nao existir no BD
				jogoRepository.save(js);
			}
			else {
				jogoToJogoCommand.setAllFields(jogo.get(), jc);
			}
		}
		
		//coloca os valores dos jogos do BD na tabela
		for(JogoCommand jc : tabela.getEliminatorias()[Fases.FINAIS.ordinal()].getJogos()) {
			Optional<Jogo> jogo = finalissima.stream().filter(j -> j.getNumeroDoJogo() == jc.getNumeroDoJogo()).findFirst();
			if(!jogo.isPresent()) {
				Jogo js = jogoCommandoToJogo.convert(jc); //salva se o jogo ainda nao existir no BD
				jogoRepository.save(js);
			}
			else {
				jogoToJogoCommand.setAllFields(jogo.get(), jc);
			}
		}
		
		//Ativa as finais
		tabela.getEliminatorias()[Fases.DISPUTATERCEIRO.ordinal()].setAtivo(true);
		tabela.getEliminatorias()[Fases.FINAIS.ordinal()].setAtivo(true);
	}
}
