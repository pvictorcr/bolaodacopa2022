package com.pvictorcr.bolaodacopa.controllers;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pvictorcr.bolaodacopa.commands.JogoApostaCommand;
import com.pvictorcr.bolaodacopa.commands.TabelaCommand;
import com.pvictorcr.bolaodacopa.commands.UsuarioCommand;
import com.pvictorcr.bolaodacopa.converters.JogoApostaToJogoApostaCommand;
import com.pvictorcr.bolaodacopa.converters.JogoToJogoCommand;
import com.pvictorcr.bolaodacopa.model.Jogo;
import com.pvictorcr.bolaodacopa.repositories.JogoApostaRepository;
import com.pvictorcr.bolaodacopa.repositories.JogoRepository;
import com.pvictorcr.bolaodacopa.security.AuthenticationUtils;
import com.pvictorcr.bolaodacopa.services.ApostaService;
import com.pvictorcr.bolaodacopa.services.TabelaService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ApostaController {
	
	private final TabelaService tabelaService;
	private final ApostaService apostaService;
	private final AuthenticationUtils authenticationUtils;
	private final JogoRepository jogoRepository;
	private final JogoToJogoCommand jogoToJogoCommand;
	
	public ApostaController(ApostaService apostaService, TabelaService tabelaService, AuthenticationUtils authenticationUtils, JogoRepository jogoRepository,
			JogoToJogoCommand jogoToJogoCommand, JogoApostaRepository jogoApostaRepository, JogoApostaToJogoApostaCommand jogoApostaToJogoApostaCommand) {
		
		this.apostaService = apostaService;
		this.tabelaService = tabelaService;
		this.authenticationUtils = authenticationUtils;
		this.jogoRepository = jogoRepository;
		this.jogoToJogoCommand = jogoToJogoCommand;
	}
	
	@GetMapping
	@RequestMapping("apostas")
	public String listaApostas(Model model) {

		UsuarioCommand u = authenticationUtils.getUsario();

		TabelaCommand tabelaCommand = tabelaService.findTabelaGruposCommands();
		tabelaCommand.setUsuario(u);

		model.addAttribute("usuario", u);
		model.addAttribute("tabelaCommand", tabelaCommand);
		model.addAttribute("result", model.getAttribute("result"));

		return "apostas";
	}
	
	@RequestMapping(value="atualizarapostagrupo", method = RequestMethod.POST)
	public String atualizarJogoGrupo(@ModelAttribute TabelaCommand form, RedirectAttributes attributes){
		
		UsuarioCommand u = authenticationUtils.getUsario();
		
		String erro = "";
		
		try {
			int grupo = Integer.parseInt(form.getActionGrupos().split("_")[0]);
			int jogo = Integer.parseInt(form.getActionGrupos().split("_")[1]);
			int numeroDoJogo = Integer.parseInt(form.getActionGrupos().split("_")[2]);
			boolean apostado = Boolean.parseBoolean(form.getActionGrupos().split("_")[3]);
			
			JogoApostaCommand jac = new JogoApostaCommand();
			Optional<Jogo> jogoOpt = jogoRepository.findByNumeroDoJogo(numeroDoJogo);
			
			if(!jogoOpt.isPresent()) {
				erro = "Jogo " + numeroDoJogo + " não encontrado";
			}
			else {
				if(jogoOpt.get().isTerminado()) {
					erro = "Não é possível fazer aposta no jogo finalizado " + numeroDoJogo;
				}
				else {
					jac.setJogo(jogoToJogoCommand.convert(jogoOpt.get()));
					
					if(jac.getJogo().isPrazoApostaAcabado()) {
						erro = "O prazo para apostar nesse jogo expirou";
					}
					else if(!apostado) {						
						jac.setGols1(Integer.parseInt(form.getGrupos().get(grupo).getJogos().get(jogo).getGols1()));
						jac.setGols2(Integer.parseInt(form.getGrupos().get(grupo).getJogos().get(jogo).getGols2()));
						erro = apostaService.saveApostaCommand(u.getAposta(), jac);
					}
					else {
						if(!apostaService.apagaJogoAposta(u.getAposta(), jac))
							erro = "Falhou ao tentar apagar o jogo-aposta '" + jac.getId() + "' do BD";
					}
				}
			}
		}
		catch(Exception e) {
			log.error("Falhou ao tentar atualizar o jogo do grupo: " + e);
			erro = "Houve um erro no envio da atualizaçao.";
		}
		
		attributes.addFlashAttribute("result", erro);
		
		return "redirect:/apostas";
	}
	
	@RequestMapping(value="atualizarapostaeliminatoria", method = RequestMethod.POST)
	public String atualizarJogoEliminatoria(@ModelAttribute TabelaCommand form, RedirectAttributes attributes){
		
		UsuarioCommand u = authenticationUtils.getUsario();
		
		String erro = "";
		
		try {
			int fase = Integer.parseInt(form.getActionEliminatorias().split("_")[0]);
			int jogo = Integer.parseInt(form.getActionEliminatorias().split("_")[1]);
			int numeroDoJogo = Integer.parseInt(form.getActionEliminatorias().split("_")[2]);
			boolean apostado = Boolean.parseBoolean(form.getActionEliminatorias().split("_")[3]);
			
			JogoApostaCommand jac = new JogoApostaCommand();
			Optional<Jogo> jogoOpt = jogoRepository.findByNumeroDoJogo(numeroDoJogo);
			
			if(!jogoOpt.isPresent()) {
				erro = "Jogo " + numeroDoJogo + " não encontrado";
			}
			else {
				if(jogoOpt.get().isTerminado()) {
					erro = "Não é possível fazer aposta no jogo finalizado " + numeroDoJogo;
				}
				else {
					jac.setJogo(jogoToJogoCommand.convert(jogoOpt.get()));
					
					if(jac.getJogo().isPrazoApostaAcabado()) {
						erro = "O prazo para apostar nesse jogo expirou";
					}
					else if(!apostado) {						
						jac.setGols1(Integer.parseInt(form.getEliminatorias()[fase].getJogos().get(jogo).getGols1()));
						jac.setGols2(Integer.parseInt(form.getEliminatorias()[fase].getJogos().get(jogo).getGols2()));
						erro = apostaService.saveApostaCommand(u.getAposta(), jac);
					}
					else {
						if(!apostaService.apagaJogoAposta(u.getAposta(), jac))
							erro = "Falhou ao tentar apagar o jogo-aposta '" + jac.getId() + "' do BD";
					}
				}
			}
		}
		catch(Exception e) {
			log.error("Falhou ao tentar atualizar o jogo do grupo: " + e);
			erro = "Houve um erro no envio da atualizaçao.";
		}
		
		attributes.addFlashAttribute("result", erro);
		
		return "redirect:/apostas";
	}
}
