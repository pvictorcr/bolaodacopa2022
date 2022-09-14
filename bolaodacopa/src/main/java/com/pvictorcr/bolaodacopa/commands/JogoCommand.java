package com.pvictorcr.bolaodacopa.commands;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.pvictorcr.bolaodacopa.constants.Fases;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JogoCommand extends BaseCommand {

	private PaisCommand p1;
	
	private PaisCommand p2;
	
	private Fases fase;
	
	private Date data;
	
	private int numeroDoJogo;
	
	private String estadio;
	
	private String gols1;
	
	private String gols2;
	
	private boolean terminado;
	
	private PaisCommand vencedorPenaltis;
	
	public String getFormatedDate() {
		
		SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM HH:mm");
		dt1.setTimeZone(TimeZone.getTimeZone("Brazil/East"));
		return dt1.format(this.data);
	}
	
	public boolean p1GanhouPenaltis() {
		if(p1 == null || p1.getNome() == null || vencedorPenaltis == null || vencedorPenaltis.getNome() == null)
			return false;
		
		return p1.getNome().equals(vencedorPenaltis.getNome());
	}
	
	public boolean p2GanhouPenaltis() {
		if(p2 == null || p2.getNome() == null || vencedorPenaltis == null || vencedorPenaltis.getNome() == null)
			return false;
		
		return p2.getNome().equals(vencedorPenaltis.getNome());
	}
	
	public void setGols(String gols1, String gols2, boolean terminado) {
		this.gols1 = gols1;
		this.gols2 = gols2;
		this.setTerminado(terminado);
	}
	
	public boolean isPrazoApostaAcabado() {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0); //data do jogo as 00h00
		
		Calendar now = Calendar.getInstance();
		now.setTime(new Date());

		return now.compareTo(calendar) >= 0; //agora è maior que jogo as 00h00?
	}
}
