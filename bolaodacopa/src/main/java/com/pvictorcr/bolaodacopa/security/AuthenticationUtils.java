package com.pvictorcr.bolaodacopa.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.pvictorcr.bolaodacopa.commands.UsuarioCommand;
import com.pvictorcr.bolaodacopa.constants.Credenciais;
import com.pvictorcr.bolaodacopa.constants.Provider;
import com.pvictorcr.bolaodacopa.converters.UsuarioToUsuarioCommand;
import com.pvictorcr.bolaodacopa.model.Usuario;
import com.pvictorcr.bolaodacopa.repositories.UsuarioRepository;

@Component
public class AuthenticationUtils {
	
	private final UsuarioRepository usuarioRepository;
	private final UsuarioToUsuarioCommand usuarioToUsuarioCommand;
	
	public AuthenticationUtils(UsuarioRepository usuarioRepository, UsuarioToUsuarioCommand usuarioToUsuarioCommand) {

		this.usuarioRepository = usuarioRepository;
		this.usuarioToUsuarioCommand = usuarioToUsuarioCommand;
	}
	
	public UsuarioCommand getUsario() {
		
		Usuario u = getUsarioModel();
		
		if(u == null)
			return null;
		
		return usuarioToUsuarioCommand.convert(u);
	}
	
	public Usuario getUsarioModel() {
		
		String email = this.getEmail();
		Optional<Usuario> u = usuarioRepository.getUsuarioByEmail(email);
		if(!u.isPresent())
			return null;
		
		return u.get();
	}
	
	public boolean isAdmin() {
		
		UsuarioCommand u = getUsario();
		
		return (u != null && u.getCredencial() == Credenciais.ADMIN);
	}

	public boolean isLoggedIn() {
		if(!SecurityContextHolder.getContext().getAuthentication().getName().trim().equals(""))
			return true;

		return false;
	}

	public void addRole(String role) {

		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
		List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
		updatedAuthorities.add(authority);

		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(
						SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
						SecurityContextHolder.getContext().getAuthentication().getCredentials(),
						updatedAuthorities)
				);
	}

	public Provider getIss() {

		if(getField(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString(), "\\, iss=").contains("https://accounts.google.com"))
			return Provider.GOOGLE;

		return Provider.FACEBOOK;
	}

	public String getId() {

		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	public String getEmail() {

		return getField(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString(), "\\, email=").replaceAll("[^\\w\\@\\.]", "");
	}

	public String getGivenName() {

		return getField(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString(), "\\, given_name=");
	}

	public String getFamilyName() {

		return getField(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString(), "\\, family_name=");
	}

	public String getFullName() {

		return getField(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString(), "\\, name=");
	}

	public String getPicture() {

		return getField(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString(), "\\, picture=");
	}

	public String getPrincipal() {

		return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
	}

	private String getField(String principal, String regex) {

		String[] aux = principal.split(regex);

		if(aux.length > 1) {
			return aux[1].split("[\\],]")[0];
		}

		return "";
	}
}

