package com.pvictorcr.bolaodacopa.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pvictorcr.bolaodacopa.commands.UsuarioCommand;
import com.pvictorcr.bolaodacopa.model.Usuario;
import com.pvictorcr.bolaodacopa.repositories.UsuarioRepository;
import com.pvictorcr.bolaodacopa.security.AuthenticationUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class IndexController {

	private final AuthenticationUtils authenticationUtils;
	private final UsuarioRepository usuarioRepository;
	
	public IndexController(AuthenticationUtils authenticationUtils, UsuarioRepository usuarioRepository) {
		
		this.authenticationUtils = authenticationUtils;
		this.usuarioRepository = usuarioRepository;
	}
	
	@RequestMapping({"", "/", "/index", "/index.html"})
	public String getIndexPage(Model model) {

		log.debug("Entering index page");

		return "redirect:/oauth_login";
	}
	
	@GetMapping("/oauth_login")
	public String getLoginPage(Model model) {
		
		return "redirect:/oauth2/authorization/google";
	}
	
	@RequestMapping("/loginsuccess")
	public String getLoginSuccess(Model model, HttpSession session, @RequestParam(name = "newuser", defaultValue = "false") boolean newuser) {
		
		UsuarioCommand u = authenticationUtils.getUsario();
		
		if(!newuser)		
			return "redirect:/apostas";
		
		model.addAttribute("usuario", u);
		
		return "cadastro";
	}
	
	@GetMapping
	@RequestMapping("/cadastrar")
	public String cadastraUsuarioNovo(String nomeUsuario) {
		
		Usuario usuario = authenticationUtils.getUsarioModel();
		usuario.setNome(nomeUsuario);
		usuarioRepository.save(usuario);
		
		return "redirect:/apostas";
	}
	
	@RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.setAuthenticated(false);
        new SecurityContextLogoutHandler().logout(request,response,authentication);
        SecurityContextHolder.clearContext();
        request.logout();
        request.getSession().invalidate();
        
        return "redirect:/";
    }
}
