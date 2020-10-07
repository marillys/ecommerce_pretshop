package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import util.Funcoes;

public class ExtentManager {

	private static ExtentReports extent;
	private static ExtentHtmlReporter htmlReporter;
	
	static Funcoes funcoes = new Funcoes();

	public static ExtentReports createInstance() {
		String nomeArquivo = "relatorio"+funcoes.dataHora()+".htm";
		String diretorio = "./relatorios/";

		// Definir caminho + nome do arquivo; titulo do arquivo, tema

		htmlReporter = new ExtentHtmlReporter(diretorio + nomeArquivo);
		htmlReporter.config().setEncoding("utf-8");
		htmlReporter.config().setDocumentTitle("Testes Selenium");
		htmlReporter.config().setReportName("Resultados dos Testes Selenium");
		htmlReporter.config().setTheme(Theme.STANDARD);

		extent = new ExtentReports();
		extent.setSystemInfo("Browser", "Chrome");
		extent.attachReporter(htmlReporter);

		return extent;
	}
}
