package com.pvictorcr.bolaodacopa.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pvictorcr.bolaodacopa.commands.JogoCommand;
import com.pvictorcr.bolaodacopa.commands.TabelaCommand;
import com.pvictorcr.bolaodacopa.commands.UsuarioCommand;
import com.pvictorcr.bolaodacopa.security.AuthenticationUtils;
import com.pvictorcr.bolaodacopa.services.TabelaService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TabelaController {

	private final TabelaService tabelaService;
	private final AuthenticationUtils authenticationUtils;
	
	public TabelaController(TabelaService tabelaService, AuthenticationUtils authenticationUtils) {
		
		this.tabelaService = tabelaService;
		this.authenticationUtils = authenticationUtils;
	}
	
	@GetMapping
	@RequestMapping("tabela")
	public String listaJogos(Model model) {
		
		UsuarioCommand u = authenticationUtils.getUsario();
		
		TabelaCommand tabelaCommand = tabelaService.findTabelaGruposCommands();
		tabelaCommand.setUsuario(u);
		
		model.addAttribute("tabelaCommand", tabelaCommand);
		model.addAttribute("result", model.getAttribute("result"));
		model.addAttribute("usuario", u);
		
		return "jogos";
	}
	
	@RequestMapping(value="atualizarjogogrupo", method = RequestMethod.POST)
	public String atualizarJogoGrupo(@ModelAttribute TabelaCommand form, Model model, RedirectAttributes attributes){
		
		UsuarioCommand u = authenticationUtils.getUsario();
		
		if(!u.isAdmin())
			return "redirect:/tabela";
		
		String erro = "";
		
		try {
			int grupo = Integer.parseInt(form.getActionGrupos().split("_")[0]);
			int jogo = Integer.parseInt(form.getActionGrupos().split("_")[1]);
			int numeroDoJogo = Integer.parseInt(form.getActionGrupos().split("_")[2]);
			boolean terminado = Boolean.parseBoolean(form.getActionGrupos().split("_")[3]);
			
			JogoCommand jc = new JogoCommand();
			jc.setNumeroDoJogo(numeroDoJogo);
			jc.setTerminado(terminado);
			
			if(jc.isTerminado()) {
				erro = tabelaService.reativarJogo(jc);
			}
			else {
				jc.setGols1(form.getGrupos().get(grupo).getJogos().get(jogo).getGols1());
				jc.setGols2(form.getGrupos().get(grupo).getJogos().get(jogo).getGols2());
				erro = tabelaService.saveJogoCommand(jc);
			}
		
		}
		catch(Exception e) {
			log.debug("Comando Action em formato incorreto: '" + form.getActionGrupos() + "': " + e);
			erro = "Houve um erro no envio da atualizaçao.";
		}
		
		attributes.addFlashAttribute("result", erro);
		
		return "redirect:/tabela";
	}
	
	@RequestMapping(value="atualizarjogoeliminatoria", method = RequestMethod.POST)
	public String atualizarJogoEliminatoria(@ModelAttribute TabelaCommand form, Model model, RedirectAttributes attributes){
		
		UsuarioCommand u = authenticationUtils.getUsario();
		
		if(!u.isAdmin())
			return "redirect:/tabela";
		
		String erro = "";
		
		try {
			int fase = Integer.parseInt(form.getActionEliminatorias().split("_")[0]);
			int jogo = Integer.parseInt(form.getActionEliminatorias().split("_")[1]);
			int numeroDoJogo = Integer.parseInt(form.getActionEliminatorias().split("_")[2]);
			boolean terminado = Boolean.parseBoolean(form.getActionEliminatorias().split("_")[3]);
			int vencedorPenaltis = form.getActionEliminatorias().split("_").length == 5 ? Integer.parseInt(form.getActionEliminatorias().split("_")[4]) : -1;
			
			JogoCommand jc = new JogoCommand();
			jc.setNumeroDoJogo(numeroDoJogo);
			jc.setTerminado(terminado);
			
			if(jc.isTerminado()) {
				erro = tabelaService.reativarJogo(jc);
			}
			else {
				jc.setGols1(form.getEliminatorias()[fase].getJogos().get(jogo).getGols1());
				jc.setGols2(form.getEliminatorias()[fase].getJogos().get(jogo).getGols2());
				if(vencedorPenaltis >= 0)
					jc.setVencedorPenaltis(tabelaService.getPaisFromJogo(jc, vencedorPenaltis));
					
				erro = tabelaService.saveJogoCommand(jc);
			}
		
		}
		catch(Exception e) {
			log.debug("Comando Action em formato incorreto: '" + form.getActionEliminatorias() + "': " + e);
			erro = "Houve um erro no envio da atualizaçao.";
		}
		
		attributes.addFlashAttribute("result", erro);
		
		return "redirect:/tabela";
	}
}
