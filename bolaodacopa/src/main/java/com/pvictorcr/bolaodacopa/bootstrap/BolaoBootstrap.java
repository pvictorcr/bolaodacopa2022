package com.pvictorcr.bolaodacopa.bootstrap;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.pvictorcr.bolaodacopa.commands.ApostaCommand;
import com.pvictorcr.bolaodacopa.commands.JogoApostaCommand;
import com.pvictorcr.bolaodacopa.commands.JogoCommand;
import com.pvictorcr.bolaodacopa.commands.PaisCommand;
import com.pvictorcr.bolaodacopa.commands.UsuarioCommand;
import com.pvictorcr.bolaodacopa.constants.Credenciais;
import com.pvictorcr.bolaodacopa.constants.Fases;
import com.pvictorcr.bolaodacopa.constants.Provider;
import com.pvictorcr.bolaodacopa.converters.PaisToPaisCommand;
import com.pvictorcr.bolaodacopa.model.Aposta;
import com.pvictorcr.bolaodacopa.model.Jogo;
import com.pvictorcr.bolaodacopa.model.JogoAposta;
import com.pvictorcr.bolaodacopa.model.Pais;
import com.pvictorcr.bolaodacopa.model.Regras;
import com.pvictorcr.bolaodacopa.model.Usuario;
import com.pvictorcr.bolaodacopa.repositories.JogoRepository;
import com.pvictorcr.bolaodacopa.repositories.PaisRepository;
import com.pvictorcr.bolaodacopa.repositories.RegrasRepository;
import com.pvictorcr.bolaodacopa.repositories.UsuarioRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BolaoBootstrap implements ApplicationListener<ContextRefreshedEvent> {

	private final PaisRepository paisRepository;
	private final JogoRepository jogoRepository;
	private final UsuarioRepository usuarioRepository;
	private final RegrasRepository regrasRepository;
	private final PaisToPaisCommand paisToPaisCommand;
	
	private final List<Pais> paises;
	private final List<Jogo> jogos;
	
	public BolaoBootstrap(PaisRepository paisRepository, JogoRepository jogoRepository,
			UsuarioRepository usuarioRepository, RegrasRepository regrasRepository,
			PaisToPaisCommand paisToPaisCommand) {
		this.paisRepository = paisRepository;
		this.jogoRepository = jogoRepository;
		this.usuarioRepository = usuarioRepository;
		this.regrasRepository = regrasRepository;
		this.paisToPaisCommand = paisToPaisCommand;
		
		this.paises = getPaises();
		this.jogos = getJogos();
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		paisRepository.saveAll(paises);
		jogoRepository.saveAll(jogos);
		usuarioRepository.saveAll(getUsuarios());
		setRegras();
		log.warn("Carregamento da Base de Dados terminado.");
		teste();
	}
	
	private void teste() {
		
		UsuarioCommand usuarioCommand = new UsuarioCommand();
		JogoApostaCommand jac = new JogoApostaCommand();
		ApostaCommand a = new ApostaCommand();
		PaisCommand bra = paisToPaisCommand.convert(paisRepository.findByNome("Brasil").get());
		PaisCommand ale = paisToPaisCommand.convert(paisRepository.findByNome("Alemanha").get());
		PaisCommand gan = paisToPaisCommand.convert(paisRepository.findByNome("Gana").get());
		a.setCampeao(bra);
		a.setViceCampeao(ale);
		jac.setJogo(new JogoCommand(bra, ale, Fases.GRUPOS, new Date(), 1, "", "0", "1", true, null));
		
		jac.setGols1(2);
		jac.setGols2(1);
		jac.getJogo().setGols("1", "3", true);
		System.out.println(usuarioCommand.calculaPontosGanhos(jac));
		jac.setGols1(2);
		jac.setGols2(1);
		jac.getJogo().setGols("2", "1", true);
		System.out.println(usuarioCommand.calculaPontosGanhos(jac));
		jac.setGols1(1);
		jac.setGols2(1);
		jac.getJogo().setGols("1", "1", true);
		System.out.println(usuarioCommand.calculaPontosGanhos(jac));
		jac.setGols1(2);
		jac.setGols2(0);
		jac.getJogo().setGols("2", "1", true);
		System.out.println(usuarioCommand.calculaPontosGanhos(jac));
		jac.setGols1(2);
		jac.setGols2(0);
		jac.getJogo().setGols("1", "0", true);
		System.out.println(usuarioCommand.calculaPontosGanhos(jac));
		jac.setGols1(2);
		jac.setGols2(0);
		jac.getJogo().setGols("3", "1", true);
		System.out.println(usuarioCommand.calculaPontosGanhos(jac));
		jac.setGols1(1);
		jac.setGols2(1);
		jac.getJogo().setGols("2", "2", true);
		System.out.println(usuarioCommand.calculaPontosGanhos(jac));
		jac.setGols1(2);
		jac.setGols2(1);
		jac.getJogo().setGols("0", "1", true);
		System.out.println(usuarioCommand.calculaPontosGanhos(jac));
		jac.setGols1(4);
		jac.setGols2(1);
		jac.getJogo().setGols("6", "1", true);
		System.out.println(usuarioCommand.calculaPontosGanhos(jac));//18 pontos
		jac.setGols1(5);
		jac.setGols2(4);
		jac.getJogo().setGols("2", "1", true);
		System.out.println(usuarioCommand.calculaPontosGanhos(jac));//9 pontos
		jac.setGols1(5);
		jac.setGols2(0);
		jac.getJogo().setGols("7", "1", true);
		System.out.println(usuarioCommand.calculaPontosGanhos(jac)); //12 pontos
		jac.setGols1(7);
		jac.setGols2(1);
		jac.getJogo().setGols("4", "5", true);
		System.out.println(usuarioCommand.calculaPontosGanhos(jac)); //3 pontos
	}
	
	private List<Usuario> getUsuarios(){
		
		String[][] credenciais = {{"Pedro Victor", "pvictorcr@gmail.com"}, /*{"Gabriela Guimaraes", "gabrielaguima@gmail.com"},*/
				{"Joao Filipe", "jfilipe@gmail.com"}, {"Vinicius Guimaraes", "viniciusguima@gmail.com"}/*,
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
				{"Claudio Antonio Guzansky Rocha", "claudioanton@teste.com"}*/};
		
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
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy HH:mm");			
			formatter.setTimeZone(TimeZone.getTimeZone("Asia/Qatar"));
			
			try (InputStream in = getClass().getResourceAsStream("/static/Jogos.csv");
				    BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
				reader.lines().forEach(l -> {
					String[] campos = l.split(";");
					try {
					jogos.add(new Jogo(
							(Pais) paises.stream().filter(p -> p.getNome().equals(campos[2])).findFirst().get(),
							(Pais) paises.stream().filter(p -> p.getNome().equals(campos[4])).findFirst().get(),
							fasesArray[Integer.parseInt(campos[0])], formatter.parse(campos[1] + " " + campos[6]),
							Integer.parseInt(campos[3]), campos[7]));
					}
					catch(Exception e) {
						log.error(e.getMessage());
					}
				});
			}
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
	
	private void setRegras() {
		
		Regras r = new Regras();
		r.setTexto(String.join("\r\n","*Regulamento do bolão:* ",
				"",
				"*1* - No início do bolão, o(a) participante irá preencher os palpites de todos os jogos da fase de grupos, além de preencher também os palpites de quais seleções irão disputar a final da copa e o palpite de quem será o campeão.",
				"",
				"*2* - O bolão continua durante as fases de mata-mata, abrindo para o preenchimento dos palpites, conforme os confrontos forem sendo definidos.",
				"",
				"*OBS:* ",
				"*- Os jogos a partir das oitavas de finais e o jogo da disputa de terceiro lugar terão um peso de 1,5 na pontuação*(A pontuação que o participante fizer em cada jogo dessas fases será multiplicada por 1,5)",
				"",
				"",
				"- *O jogo da final terá o peso de 2,0 na pontuação* (A pontuação que o participante fizer na final será multiplicada por 2)",
				"",
				"*3* - Pontuação ",
				"",
				"*(A)* Acerto do placar exato: 18 pontos ",
				"",
				"*Exemplo 1*: O participante colocou o palpite Brasil 2x1 Alemanha e o jogo terminou Brasil 2x1 Alemanha",
				"",
				"*Exemplo 2*: O participante preenche o palpite Brasil 1x1 Alemanha e jogo terminou Brasil 1x1 Alemanha ",
				"",
				"*(B)* Acerto do vencedor da partida e do número de gols de uma seleção: 12 pontos",
				"",
				"*Exemplo 1*: O participante preenche o palpite Brasil 2x0 Alemanha e o jogo terminou Brasil 2x1 Alemanha",
				"",
				"*Exemplo 2*: O participante preencheu o palpite Brasil 2x0 Alemanha e o jogo terminou Brasil 1x0 Alemanha ",
				"",
				"*(C)* Acerto apenas do vencedor da partida ou se o jogo terminou empate: 9 pontos",
				"",
				"*Exemplo 1*: O participante preencheu o palpite Brasil 2x0 Alemanha e o jogo terminou Brasil 3x1 Alemanha",
				"",
				"*Exemplo 2*: O participante preencheu o palpite Brasil 1x1 Alemanha e o jogo terminou Brasil 2x2 Alemanha ",
				"",
				"*(D)* Acerto apenas do número de gols de uma seleção: 3 pontos",
				"",
				"*Exemplo*: O participante preencheu o palpite Brasil 2x1 Alemanha e o jogo terminou Brasil 0x1 Alemanha",
				"",
				"*OBS: Para efeito de pontuação, em qualquer fase da copa, os jogos so irão valer até o final do segundo tempo (90 minutos + Acréscimos)*",
				"",
				"",
				"*Se liga na Novidade ⚠️⚠️⚠️⚠️*",
				"👇🏻👇🏻👇🏻👇🏻",
				" ",
				"*Regra do 4 ou + gols*",
				"",
				"Serão considerados acertos de placares os casos em que o participante preencha o palpite com um placar de 4 gols ou mais para um das seleções e essa seleção faça pelo menos 4 gols.",
				"",
				"*Exemplo 1*: ",
				"O participante preenche o palpite Brasil 4x1 Alemanha. ",
				"Se o Brasil vencer por 5x1, 6x1, 7x1, etc... O participante irá marcar 18 pontos (Acertou que o Brasil faria 4 gols ou mais e acertou também o número de gols da Alemanha)",
				"",
				"*Exemplo 2*:",
				"O participante preenche em seu palpite o placar Brasil 5x4 Alemanha. Se o Brasil vencer por 2x1, participante marca somente 9 pontos (acertou só que ganhou, mas não acertou o número de gols de nenhuma das seleções)",
				"",
				"*Exemplo 3:* ",
				"Se o participante preenche o palpite Brasil 5x0 Alemanha e jogo terminar Brasil 7x1 Alemanha, o participante marca 12 pontos. (acertou que o Brasil faria 4 gols o mais, mas errou o número de gols da Alemanha)",
				"",
				"*Exemplo 4:*",
				"Se o o participante preencher o palpite Brasil 7x1 Alemanha e o jogo terminar Brasil 4x5 Alemanha, só marca 3 pontos (acertou que o Brasil faria 4 gols ou mais, mas errou quem seria o vencedor)",
				"",
				"",
				"*4* - Pontuação da final e do campeão:",
				"",
				"(a) Acerto de apenas um finalista: 18 pontos",
				"",
				"(b) Acerto de dois finalistas: 54 pontos",
				"",
				"(c) Acerto do campeão: 36 pontos",
				"",
				"*5* - Prazo para pagamento e preenchimento dos placares: *18 de novembro.*",
				"",
				"*Obs: Não há a possibilidade desse prazo ser estendido*",
				"",
				"",
				"",
				"*Qualquer dúvida, contactar os responsáveis!*"));
		
		regrasRepository.save(r);
	}
}
