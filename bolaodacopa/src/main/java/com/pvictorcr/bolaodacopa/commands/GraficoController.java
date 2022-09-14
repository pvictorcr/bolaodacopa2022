package com.pvictorcr.bolaodacopa.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GraficoController extends BaseCommand {

	private int[][] dados;
	private String[] nomes;
	private String titulo;
	private String subtitulo;
}
