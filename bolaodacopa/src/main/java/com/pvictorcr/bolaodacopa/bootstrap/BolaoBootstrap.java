package com.pvictorcr.bolaodacopa.bootstrap;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.pvictorcr.bolaodacopa.constants.Credenciais;
import com.pvictorcr.bolaodacopa.constants.Fases;
import com.pvictorcr.bolaodacopa.constants.Provider;
import com.pvictorcr.bolaodacopa.model.Aposta;
import com.pvictorcr.bolaodacopa.model.Jogo;
import com.pvictorcr.bolaodacopa.model.JogoAposta;
import com.pvictorcr.bolaodacopa.model.Pais;
import com.pvictorcr.bolaodacopa.model.Usuario;
import com.pvictorcr.bolaodacopa.repositories.ApostaRepository;
import com.pvictorcr.bolaodacopa.repositories.JogoApostaRepository;
import com.pvictorcr.bolaodacopa.repositories.JogoRepository;
import com.pvictorcr.bolaodacopa.repositories.PaisRepository;
import com.pvictorcr.bolaodacopa.repositories.UsuarioRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BolaoBootstrap implements ApplicationListener<ContextRefreshedEvent> {

	private final PaisRepository paisRepository;
	private final JogoRepository jogoRepository;
	private final JogoApostaRepository jogoApostaRepository;
	private final ApostaRepository apostaRepository;
	private final UsuarioRepository usuarioRepository;
	
	private final List<Pais> paises;
	private final List<Jogo> jogos;
	
	public BolaoBootstrap(PaisRepository paisRepository, JogoRepository jogoRepository, JogoApostaRepository jogoApostaRepository,
			ApostaRepository apostaRepository, UsuarioRepository usuarioRepository) {
		this.paisRepository = paisRepository;
		this.jogoRepository = jogoRepository;
		this.jogoApostaRepository = jogoApostaRepository;
		this.apostaRepository = apostaRepository;
		this.usuarioRepository = usuarioRepository;
		
		this.paises = getPaises();
		this.jogos = getJogos();
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		paisRepository.saveAll(paises);
		jogoRepository.saveAll(jogos);
		usuarioRepository.saveAll(getUsuarios());
	}
	
	private List<Usuario> getUsuarios(){
		
		String[][] credenciais = {{"Pedro Victor", "pvictorcr@gmail.com"}, /*{"Gabriela Guimaraes", "gabrielaguima@gmail.com"},*/
				{"Joao Filipe", "jfilipe@gmail.com"}, {"Vinicius Guimaraes", "viniciusguima@gmail.com"},
				{"Beatriz Guimaraes", "beatrizguima@gmail.com"}, {"Solange Almeida", "solangealmeida@gmail.com"},
				{"Stephenson Bezerra Rodrigues", "flubaru@gmail.com"}, {"Maria da Conceicao", "mariadaconceicao@gmail.com"},
				{"Afonso Henrique", "henriqueguima@gmail.com"}, {"Maria Marieta Guimaraes", "mamarietaguima@gmail.com"},
				{"Alexandre Gomes", "xandyaviao@gmail.com"},
				{"Adriano Nonato Cruz de Souza", "adrianononat@teste.com"},
				{"Aida Regina de Oliveira Souza", "aidareginade@teste.com"},
				{"Akilla Brito Santos", "akillabritos@teste.com"},
				{"Alcinda Nathally Nogueira", "alcindanatha@teste.com"},
				{"Aldovan Achilles de Rosso", "aldovanachil@teste.com"},
				{"Aline Nogueira Costa Siqueira", "alinenogueir@teste.com"},
				{"Aluizio Andreatta Junior", "aluizioandre@teste.com"},
				{"Ana Carolina Balarin de Andrade", "anacarolinab@teste.com"},
				{"Ana Karolina Muniz Figueredo", "anakarolinam@teste.com"},
				{"Ana Paula Diniz Oliveira", "anapauladini@teste.com"},
				{"Ana Paula Ferreira de Magalhaes", "anapaulaferr@teste.com"},
				{"Ana Paula Guimaraes Pereira", "anapaulaguim@teste.com"},
				{"Ana Paula Lopes Ramos", "anapaulalope@teste.com"},
				{"Anderson Rodrigues Santos", "andersonrodr@teste.com"},
				{"Andre Gallon Barcelos", "andregallonb@teste.com"},
				{"Andre Luis dos Santos Moreira", "andreluisdos@teste.com"},
				{"Angelo Messias Lopes", "angelomessia@teste.com"},
				{"Anna Carolina Hilario Coser", "annacarolina@teste.com"},
				{"Arijuna Marques Costa", "arijunamarqu@teste.com"},
				{"Arthur Prata Parreira Haolla", "arthurpratap@teste.com"},
				{"Barbara Schurmann Sacca", "barbaraschur@teste.com"},
				{"Beatriz de Rezende Alves", "beatrizderez@teste.com"},
				{"Caio Cesar Taveira", "caiocesartav@teste.com"},
				{"Camila Bueno de Castro", "camilabuenod@teste.com"},
				{"Camila Vieira Ruas", "camilavieira@teste.com"},
				{"Carolina Mariano Domingues da Silva", "carolinamari@teste.com"},
				{"Carolina Nolasco Loureiro", "carolinanola@teste.com"},
				{"Charles da Silva Honorio", "charlesdasil@teste.com"},
				{"Christian Carlos Candido da Silva", "christiancar@teste.com"},
				{"Claudio Antonio Guzansky Rocha", "claudioanton@teste.com"}};
		
		List<Usuario> usuarios = new ArrayList<Usuario>();
		
		for(String[] credencial : credenciais)
			usuarios.add(new Usuario(credencial[0], credencial[1], Credenciais.USUARIO, Provider.GOOGLE, getApostas().get(0)));
		
		usuarios.get(0).setCredencial(Credenciais.ADMIN);
		
		return usuarios;
	}
	
	private List<Aposta> getApostas(){
		
		List<Aposta> apostas = new ArrayList<Aposta>();
		
		Aposta a = new Aposta();
		setJogoApostas(a);
		apostas.add(a);
		
		return apostas;
	}
	
	private void setJogoApostas(Aposta a){
		
		for(Jogo j : jogos)
			a.addJogoAposta(new JogoAposta(j, (int) (Math.random()*4), (int) (Math.random()*4)));
	}
	
	private List<Pais> getPaises(){
		
		List<Pais> paises = new ArrayList<Pais>();
		
		paises.add(new Pais("Catar", 'A'));
		paises.add(new Pais("Equador", 'A'));
		paises.add(new Pais("Senegal", 'A'));
		paises.add(new Pais("Holanda", 'A'));
		
		paises.add(new Pais("Inglaterra", 'B'));
		paises.add(new Pais("Irã", 'B'));
		paises.add(new Pais("Estados Unidos", 'B'));
		paises.add(new Pais("País de Gales", 'B'));
		
		paises.add(new Pais("Argentina", 'C'));
		paises.add(new Pais("Arábia Saudita", 'C'));
		paises.add(new Pais("México", 'C'));
		paises.add(new Pais("Polônia", 'C'));
		
		paises.add(new Pais("França", 'D'));
		paises.add(new Pais("Austrália", 'D'));
		paises.add(new Pais("Dinamarca", 'D'));
		paises.add(new Pais("Tunísia", 'D'));
		
		paises.add(new Pais("Espanha", 'E'));
		paises.add(new Pais("Costa Rica", 'E'));
		paises.add(new Pais("Alemanha", 'E'));
		paises.add(new Pais("Japão", 'E'));
		
		paises.add(new Pais("Bélgica", 'F'));
		paises.add(new Pais("Canadá", 'F'));
		paises.add(new Pais("Marrocos", 'F'));
		paises.add(new Pais("Croácia", 'F'));
		
		paises.add(new Pais("Brasil", 'G'));
		paises.add(new Pais("Sérvia", 'G'));
		paises.add(new Pais("Suíça", 'G'));
		paises.add(new Pais("Camarões", 'G'));
		
		paises.add(new Pais("Portugal", 'H'));
		paises.add(new Pais("Gana", 'H'));
		paises.add(new Pais("Uruguai", 'H'));
		paises.add(new Pais("Coréia do Sul", 'H'));
		
		return paises;
	}
	
	private List<Jogo> getJogos(){
		
		List<Jogo> jogos = new ArrayList<Jogo>();
		Fases[] fasesArray = {Fases.FINAIS, Fases.DISPUTATERCEIRO, Fases.SEMIFINAIS, Fases.QUARTAS, Fases.OITAVAS, Fases.GRUPOS};
		
		try {
			Scanner sc = new Scanner(new File("src\\main\\resources\\static\\Jogos.csv"));
			
			SimpleDateFormat formatter = new SimpleDateFormat("d-MMM-yy HH:mm");			
			formatter.setTimeZone(TimeZone.getTimeZone("Asia/Qatar"));
			
			while (sc.hasNext()){
				String[] campos = sc.nextLine().split(";");
				
				jogos.add(new Jogo(
						(Pais) paises.stream().filter(p -> p.getNome().equals(campos[2])).findFirst().get(),
						(Pais) paises.stream().filter(p -> p.getNome().equals(campos[4])).findFirst().get(),
						fasesArray[Integer.parseInt(campos[0])], formatter.parse(campos[1] + " " + campos[6]),
						Integer.parseInt(campos[3]), campos[7]));
			}
			sc.close();
		}
		catch(Exception e) {
			log.error("Não conseguiu ler o arquivo csv de jogos: " + e.toString());
			System.err.println("Não conseguiu ler o arquivo csv de jogos: " + e.toString());
		}
		
		//***************
		//APAGUE-ME!!!!
		//***************
		for(Jogo j : jogos) {
			if(j.getFase().ordinal() >= Fases.OITAVAS.ordinal()) {
				j.setGols1((int) (Math.random()*4));
				j.setGols2((int) (Math.random()*4));
				j.setTerminado(true);
				if(j.getGols1() == j.getGols2())
					j.setVencedorPenaltis((int) (Math.random()*2) == 0 ? j.getP1() : j.getP2());
			}
		}
		
		jogos.get(0).setTerminado(false);
		
		return jogos;
	}
}
