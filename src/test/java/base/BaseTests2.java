package base;

import java.io.File;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.google.common.io.Files;

import pages.HomePage;

public class BaseTests2 {

	private static WebDriver driver;
	protected HomePage homePage;

	static ExtentHtmlReporter reporter;
	static ExtentReports extentxReporter;
	static ExtentTest teste;

	// instanciar o driver
	@BeforeClass
	public static void inicializar() {
		/*
		 * criarInstance();
		 * 
		 * extentxReporter.attachReporter(reporter);
		 */
		System.setProperty("webdriver.chrome.driver", "C:\\driversExternos\\chromedriver\\85\\chromedriver.exe");

		driver = new ChromeDriver();
	}

	// navegar até a página inicial
	@BeforeMethod
	public void carregarPaginaInicial() {
		driver.get("https://marcelodebittencourt.com/demoprestashop/");

		// criaruma nova homePage criando um driver
		homePage = new HomePage(driver);
	}

	@AfterClass
	public static void finalizar() {
		driver.quit();
	}

	public void capturarTela(String nomeTeste, String resultado) {
		var camera = (TakesScreenshot) driver;

		File capturadeTela = camera.getScreenshotAs(OutputType.FILE);

		try {

			Files.move(capturadeTela, new File("resources/screenshots/" + nomeTeste + "_" + resultado + ".png"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String capturarTela2(String nomeTeste, String resultado) {
		var camera = (TakesScreenshot) driver;

		File capturadeTela = camera.getScreenshotAs(OutputType.FILE);

		try {

			Files.move(capturadeTela, new File("resources/screenshots/" + nomeTeste + "_" + resultado + ".png"));

			return "C:/Users/maril/OneDrive/Documentos/Projetos/ecommerce_pretshop/resources/screenshots/" + nomeTeste
					+ "_" + resultado + ".png";

		} catch (Exception e) {
			e.printStackTrace();
			return "ERRO";
		}
	}
}