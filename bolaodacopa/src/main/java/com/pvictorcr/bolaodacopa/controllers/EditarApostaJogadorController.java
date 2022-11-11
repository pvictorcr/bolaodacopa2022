package com.pvictorcr.bolaodacopa.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pvictorcr.bolaodacopa.commands.JogoApostaCommand;
import com.pvictorcr.bolaodacopa.commands.PaisCommand;
import com.pvictorcr.bolaodacopa.commands.TabelaCommand;
import com.pvictorcr.bolaodacopa.commands.UsuarioCommand;
import com.pvictorcr.bolaodacopa.converters.JogoApostaToJogoApostaCommand;
import com.pvictorcr.bolaodacopa.converters.JogoToJogoCommand;
import com.pvictorcr.bolaodacopa.converters.UsuarioToUsuarioCommand;
import com.pvictorcr.bolaodacopa.model.Jogo;
import com.pvictorcr.bolaodacopa.model.Usuario;
import com.pvictorcr.bolaodacopa.repositories.JogoApostaRepository;
import com.pvictorcr.bolaodacopa.repositories.JogoRepository;
import com.pvictorcr.bolaodacopa.repositories.UsuarioRepository;
import com.pvictorcr.bolaodacopa.security.AuthenticationUtils;
import com.pvictorcr.bolaodacopa.services.ApostaService;
import com.pvictorcr.bolaodacopa.services.ClassificacaoService;
import com.pvictorcr.bolaodacopa.services.PaisService;
import com.pvictorcr.bolaodacopa.services.TabelaService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class EditarApostaJogadorController {

	private final ClassificacaoService classificacaoService;
	private final TabelaService tabelaService;
	private final ApostaService apostaService;
	private final AuthenticationUtils authenticationUtils;
	private final JogoRepository jogoRepository;
	private final JogoToJogoCommand jogoToJogoCommand;
	private final UsuarioRepository usuarioRepository;
	private final UsuarioToUsuarioCommand usuarioToUsuarioCommand;
	private final PaisService paisService;
	
	public EditarApostaJogadorController(ApostaService apostaService, TabelaService tabelaService, 
			AuthenticationUtils authenticationUtils, JogoRepository jogoRepository,	JogoToJogoCommand jogoToJogoCommand,
			JogoApostaRepository jogoApostaRepository, JogoApostaToJogoApostaCommand jogoApostaToJogoApostaCommand,
			ClassificacaoService classificacaoService, UsuarioRepository usuarioRepository, 
			UsuarioToUsuarioCommand usuarioToUsuarioCommand, PaisService paisService) {
		
		this.apostaService = apostaService;
		this.tabelaService = tabelaService;
		this.authenticationUtils = authenticationUtils;
		this.jogoRepository = jogoRepository;
		this.jogoToJogoCommand = jogoToJogoCommand;
		this.classificacaoService = classificacaoService;
		this.usuarioRepository = usuarioRepository;
		this.usuarioToUsuarioCommand = usuarioToUsuarioCommand;
		this.paisService = paisService;
	}
	
	@GetMapping
	@RequestMapping("alterar_aposta_usuario")
	public String listaApostas(Model model) {

		UsuarioCommand u = authenticationUtils.getUsario();
		if(!u.isAdmin())
			return "redirect:/";
		
		List<UsuarioCommand> usuarios = classificacaoService.getUsuariosOrdenadosPorNome();

		model.addAttribute("usuario", u);
		model.addAttribute("classificacao", usuarios);

		return "listausuarios";
	}
	
	@GetMapping
	@RequestMapping("apostasusuario")
	public String listaApostasUsuario(String email, Model model) {

		UsuarioCommand u = authenticationUtils.getUsario();
		if(!u.isAdmin())
			return "redirect:/";
		
		Optional<Usuario> usuOpt = usuarioRepository.getUsuarioByEmail(email);
		if(!usuOpt.isPresent()) {
			log.error("Usuario " + email + " nao presente no BD para edicao");
			return "listausuarios";
		}
		
		List<PaisCommand> paises = paisService.findAllOrdered();

		TabelaCommand tabelaCommand = tabelaService.findTabelaGruposCommands();
		UsuarioCommand uc = usuarioToUsuarioCommand.convert(usuOpt.get());
		model.addAttribute("usuario_detalhe", uc);
		model.addAttribute("usuario", u);
		model.addAttribute("tabelaCommand", tabelaCommand);
		model.addAttribute("paises", paises);

		return "editar_usuario";
	}
	
	@RequestMapping(value="atualizargrupousuario", method = RequestMethod.POST)
	public String atualizarJogoGrupo(String email, @ModelAttribute TabelaCommand form, Model model){
		
		UsuarioCommand u = authenticationUtils.getUsario();
		if(!u.isAdmin())
			return "redirect:/";
		
		String erro = "";
		
		Optional<Usuario> usuOpt = usuarioRepository.getUsuarioByEmail(email);
		if(!usuOpt.isPresent()) {
			erro = "Usuario " + email + " nao presente no BD para edicao";
			log.error(erro);
			model.addAttribute("result", erro);
			return "redirect:/listausuarios";
		}
		UsuarioCommand paraAlterar = usuarioToUsuarioCommand.convert(usuOpt.get());
		
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
				jac.setJogo(jogoToJogoCommand.convert(jogoOpt.get()));
				
				if(!apostado) {						
					jac.setGols1(Integer.parseInt(form.getGrupos().get(grupo).getJogos().get(jogo).getGols1()));
					jac.setGols2(Integer.parseInt(form.getGrupos().get(grupo).getJogos().get(jogo).getGols2()));
					erro = apostaService.saveApostaCommand(paraAlterar.getAposta(), jac);
				}
				else {
					if(!apostaService.apagaJogoAposta(paraAlterar.getAposta(), jac))
						erro = "Falhou ao tentar apagar o jogo-aposta '" + jac.getId() + "' do BD";
				}
			}
		}
		catch(Exception e) {
			log.error("Comando Action em formato incorreto: '" + form.getActionGrupos() + "': " + e);
			erro = "Houve um erro no envio da atualizaçao.";
		}
		
		TabelaCommand tabelaCommand = tabelaService.findTabelaGruposCommands();
		paraAlterar = usuarioToUsuarioCommand.convert(usuarioRepository.getUsuarioByEmail(email).get());
		
		List<PaisCommand> paises = paisService.findAllOrdered();
		
		model.addAttribute("usuario", u);
		model.addAttribute("usuario_detalhe", paraAlterar);
		model.addAttribute("tabelaCommand", tabelaCommand);
		model.addAttribute("result", erro);
		model.addAttribute("paises", paises);
		
		return "editar_usuario";
	}
	
	@RequestMapping(value="atualizareliminatoriausuario", method = RequestMethod.POST)
	public String atualizarJogoEliminatoria(String email, @ModelAttribute TabelaCommand form, Model model){
		
		UsuarioCommand u = authenticationUtils.getUsario();
		if(!u.isAdmin())
			return "redirect:/";
		
		String erro = "";
		
		Optional<Usuario> usuOpt = usuarioRepository.getUsuarioByEmail(email);
		if(!usuOpt.isPresent()) {
			log.error("Usuario " + email + " nao presente no BD para edicao");
			return "listausuarios";
		}
		UsuarioCommand paraAlterar = usuarioToUsuarioCommand.convert(usuOpt.get());
		
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
				jac.setJogo(jogoToJogoCommand.convert(jogoOpt.get()));
				
				if(!apostado) {						
					jac.setGols1(Integer.parseInt(form.getEliminatorias()[fase].getJogos().get(jogo).getGols1()));
					jac.setGols2(Integer.parseInt(form.getEliminatorias()[fase].getJogos().get(jogo).getGols2()));
					erro = apostaService.saveApostaCommand(paraAlterar.getAposta(), jac);
				}
				else {
					if(!apostaService.apagaJogoAposta(paraAlterar.getAposta(), jac))
						erro = "Falhou ao tentar apagar o jogo-aposta '" + jac.getId() + "' do BD";
				}
			}
		}
		catch(Exception e) {
			log.error("Comando Action em formato incorreto: '" + form.getActionEliminatorias() + "': " + e);
			erro = "Houve um erro no envio da atualizaçao.";
		}
		
		TabelaCommand tabelaCommand = tabelaService.findTabelaGruposCommands();
		paraAlterar = usuarioToUsuarioCommand.convert(usuarioRepository.getUsuarioByEmail(email).get());
		
		List<PaisCommand> paises = paisService.findAllOrdered();
		
		model.addAttribute("usuario", u);
		model.addAttribute("usuario_detalhe", paraAlterar);
		model.addAttribute("tabelaCommand", tabelaCommand);
		model.addAttribute("result", erro);
		model.addAttribute("paises", paises);
		
		return "editar_usuario";
	}
	
	@RequestMapping(value="atualizarFinalUsuario", method = RequestMethod.POST)
	public String atualizarFinal(String email, @ModelAttribute TabelaCommand form, Model model){
		
		UsuarioCommand u = authenticationUtils.getUsario();
		if(!u.isAdmin())
			return "redirect:/";
		
		String erro = "";
		
		Optional<Usuario> usuOpt = usuarioRepository.getUsuarioByEmail(email);
		if(!usuOpt.isPresent()) {
			log.error("Usuario " + email + " nao presente no BD para edicao");
			return "listausuarios";
		}
		UsuarioCommand paraAlterar = usuarioToUsuarioCommand.convert(usuOpt.get());
		
		paraAlterar.getAposta().setViceCampeao(paisService.findByName(form.getActionEliminatorias().split("_")[0]));
		paraAlterar.getAposta().setCampeao(paisService.findByName(form.getActionEliminatorias().split("_")[1]));
		
		erro = apostaService.saveApostaCommandFinais(paraAlterar.getAposta());
		
		List<PaisCommand> paises = paisService.findAllOrdered();
		
		TabelaCommand tabelaCommand = tabelaService.findTabelaGruposCommands();
		model.addAttribute("usuario", u);
		model.addAttribute("usuario_detalhe", paraAlterar);
		model.addAttribute("tabelaCommand", tabelaCommand);
		model.addAttribute("result", erro);
		model.addAttribute("paises", paises);
		
		return "editar_usuario";
	}
}
