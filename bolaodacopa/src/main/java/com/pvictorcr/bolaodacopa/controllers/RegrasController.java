package com.pvictorcr.bolaodacopa.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pvictorcr.bolaodacopa.commands.UsuarioCommand;
import com.pvictorcr.bolaodacopa.model.Regras;
import com.pvictorcr.bolaodacopa.repositories.RegrasRepository;
import com.pvictorcr.bolaodacopa.security.AuthenticationUtils;

@Controller
public class RegrasController {

	private final RegrasRepository regrasRepository;
	private final AuthenticationUtils authenticationUtils;
	
	public RegrasController(RegrasRepository regrasRepository, AuthenticationUtils authenticationUtils) {
		
		this.regrasRepository = regrasRepository;
		this.authenticationUtils = authenticationUtils;
	}
	
	@GetMapping
	@RequestMapping("regras")
	public String listaJogos(Model model) {
		
		UsuarioCommand u = authenticationUtils.getUsario();
		String regras = "";
		
		for(Regras r : regrasRepository.findAll())
			regras = r.getTexto();
		
		model.addAttribute("usuario", u);
		model.addAttribute("regras", regras);
		
		return "regras";
	}
}
