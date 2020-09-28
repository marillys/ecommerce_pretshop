package base;

import java.io.File;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
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
		
		reporter = new ExtentHtmlReporter("./relatorios/relatorio.htm");
		extentxReporter = new ExtentReports();
		
		extentxReporter.attachReporter(reporter);
		
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

	@AfterMethod
	public void preencherRelatorio(ITestResult testResult) 
	{
		teste = extentxReporter.createTest("TEST");
		
		teste.log(Status.PASS, "AVALIAÇÃO");
		
		teste = extentxReporter.createTest(testResult.getMethod().getMethodName());
		
		if(testResult.getStatus() == ITestResult.FAILURE)
			teste.log(Status.FAIL, "FALHA");
		else
			teste.log(Status.PASS, "PASSOU");
	}
	@AfterClass
	public static void finalizar() {
		driver.quit();
		
		//Fecha o arquivo de relatório
		extentxReporter.flush();
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
}