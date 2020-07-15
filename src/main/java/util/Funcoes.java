package util;

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
}
