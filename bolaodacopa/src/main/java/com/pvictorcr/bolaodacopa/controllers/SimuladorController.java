package com.pvictorcr.bolaodacopa.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pvictorcr.bolaodacopa.commands.TabelaCommand;
import com.pvictorcr.bolaodacopa.commands.UsuarioCommand;
import com.pvictorcr.bolaodacopa.security.AuthenticationUtils;
import com.pvictorcr.bolaodacopa.services.SimulacaoService;
import com.pvictorcr.bolaodacopa.services.TabelaService;

@Controller
public class SimuladorController {

	private final TabelaService tabelaService;
	private final AuthenticationUtils authenticationUtils;
	private final SimulacaoService simulacaoService;
	
	public SimuladorController(TabelaService tabelaService, AuthenticationUtils authenticationUtils, SimulacaoService simulacaoService) {
		
		this.tabelaService = tabelaService;
		this.authenticationUtils = authenticationUtils;
		this.simulacaoService = simulacaoService;
	}
	
	@GetMapping
	@RequestMapping("simulacao")
	public String listaJogos(Model model) {
		
		UsuarioCommand u = authenticationUtils.getUsario();
		
		TabelaCommand tabelaCommand = tabelaService.findTabelaGruposCommands();
		tabelaCommand.setUsuario(u);
		
		model.addAttribute("tabelaCommand", tabelaCommand);
		model.addAttribute("usuario", u);
		
		return "simulador";
	}
	
	@GetMapping
	@RequestMapping("simular")
	public String simulaJogos(@ModelAttribute TabelaCommand form, Model model) {
		
		UsuarioCommand u = authenticationUtils.getUsario();
		List<UsuarioCommand> classificacao = simulacaoService.getUsuariosOrdenados(form);

		model.addAttribute("usuario", u);
		model.addAttribute("classificacao", classificacao);
		model.addAttribute("simulacao", true);

		return "classificacao";
	}
}
