package util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Funcoes {

	public static Double removeCifraoDevolveDouble(String texto) 
	{
		texto = texto.replace("$", "");
		Double precoProduto = Double.parseDouble(texto);
		
		return precoProduto;
	}
	
	public static int removeTextoItemsDevolveInt(String texto) 
	{
		texto = texto.replace(" items", "");
		
		int quantidade = Integer.parseInt(texto);
		
		return quantidade;
	}
	
	public static String removeTexto(String texto, String textoParaRemover) 
	{
		texto = texto.replace(textoParaRemover, "");
		return texto;
	}
	
	public String dataHora() 
	{
		//cria objeto calendar e pega a data e hora do sistema
		Calendar calendar = Calendar.getInstance();
		//transforma objeto Calendar em objeto Date
		Date data = calendar.getTime();

		//cria objetos de formatação de data e hora
		SimpleDateFormat sddia = new SimpleDateFormat("ddMMyyyy");
		SimpleDateFormat sdhora = new SimpleDateFormat("HHmmss");
		//formata 
		String dia = sddia.format(data);
		String hora = sdhora.format(data);
		
		return dia+hora;
	}
}
