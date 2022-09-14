package com.pvictorcr.bolaodacopa.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pvictorcr.bolaodacopa.commands.GraficoController;
import com.pvictorcr.bolaodacopa.commands.TabelaCommand;
import com.pvictorcr.bolaodacopa.commands.UsuarioCommand;
import com.pvictorcr.bolaodacopa.converters.UsuarioToUsuarioCommand;
import com.pvictorcr.bolaodacopa.model.Usuario;
import com.pvictorcr.bolaodacopa.repositories.UsuarioRepository;
import com.pvictorcr.bolaodacopa.security.AuthenticationUtils;
import com.pvictorcr.bolaodacopa.services.ClassificacaoService;
import com.pvictorcr.bolaodacopa.services.TabelaService;

@Controller
public class ClassificacaoController {

	private final ClassificacaoService classificacaoService;
	private final AuthenticationUtils authenticationUtils;
	private final UsuarioRepository usuarioRepository;
	private final UsuarioToUsuarioCommand usuarioToUsuarioCommand;
	private final TabelaService tabelaService;
	
	public ClassificacaoController(ClassificacaoService classificacaoService, AuthenticationUtils authenticationUtils,
			UsuarioRepository usuarioRepository, UsuarioToUsuarioCommand usuarioToUsuarioCommand, TabelaService tabelaService) {
		this.classificacaoService = classificacaoService;
		this.authenticationUtils = authenticationUtils;
		this.usuarioRepository = usuarioRepository;
		this.usuarioToUsuarioCommand = usuarioToUsuarioCommand;
		this.tabelaService = tabelaService;
	}
	
	@GetMapping
	@RequestMapping("classificacao")
	public String listaApostas(Model model) {

		UsuarioCommand u = authenticationUtils.getUsario();
		List<UsuarioCommand> classificacao = classificacaoService.getUsuariosOrdenados();

		model.addAttribute("usuario", u);
		model.addAttribute("classificacao", classificacao);
		model.addAttribute("simulacao", false);

		return "classificacao";
	}
	
	@GetMapping
	@RequestMapping("grafico")
	public String criaGrafico(Model model) {

		final int max = 12;
		
		UsuarioCommand atual = authenticationUtils.getUsario();
		List<UsuarioCommand> jogadores = classificacaoService.getUsuariosOrdenados();
		
		GraficoController grafico = new GraficoController();
		
		int[][] dados = new int[jogadores.get(0).getHistoricoPontuacao().size()][Math.min(jogadores.size(), max)+1];
		for(int i = 0; i < dados.length; i++)
			dados[i][0] = i+1;
		String[] nomes = new String[Math.min(jogadores.size(), max)];
		for(int i = 0; i < Math.min(jogadores.size(), max); i++) {
			List<Integer> historico = jogadores.get(i).getHistoricoPontuacao();
			for(int j = 0; j < historico.size(); j++)
				dados[j][i+1] = historico.get(j);
			nomes[i] = jogadores.get(i).getNome();
		}
		
		grafico.setDados(dados);
		grafico.setNomes(nomes);
		grafico.setTitulo("Evolução da pontuação dos " + max + " melhores colocados");
		grafico.setSubtitulo("");
				
        model.addAttribute("grafico", grafico);
		model.addAttribute("usuario", atual);

		return "grafico";
	}
	
	@GetMapping
	@RequestMapping("detalheusuario")
	public String detalheUsuario(String email, Model model) {

		UsuarioCommand u = authenticationUtils.getUsario();
		
		Optional<Usuario> usuOpt = usuarioRepository.getUsuarioByEmail(email);
		if(!usuOpt.isPresent())
			return "classificacao";

		TabelaCommand tabelaCommand = tabelaService.findTabelaGruposCommands();
		UsuarioCommand uc = usuarioToUsuarioCommand.convert(usuOpt.get());
		model.addAttribute("usuario_detalhe", uc);
		model.addAttribute("usuario", u);
		model.addAttribute("tabelaCommand", tabelaCommand);

		return "detalhe_usuario";
	}
}
