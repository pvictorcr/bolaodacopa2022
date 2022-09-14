package com.pvictorcr.bolaodacopa.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JogoApostaCommand extends BaseCommand {

	private ApostaCommand aposta;
	
	private JogoCommand jogo;
	
	private int gols1;
	
	private int gols2;
}
