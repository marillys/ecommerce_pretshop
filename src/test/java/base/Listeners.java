package base;

import java.util.Arrays;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;

public class Listeners implements ITestListener {

	private static ExtentReports extent = ExtentManager.createInstance();

	private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<ExtentTest>();

	public void onTestStart(ITestResult result) {
		// Adiciona ao relatório o nome da classe e do método
		ExtentTest test = extent
				.createTest(result.getTestClass().getName() + " - " + result.getMethod().getMethodName());
		extentTest.set(test);

	}

	public void onTestSuccess(ITestResult result) {

		String texto = "<b>O método de teste " + result.getMethod().getMethodName() + " rodou com sucesso</b>";
		Markup m = MarkupHelper.createLabel(texto, ExtentColor.GREEN);
		extentTest.get().log(Status.PASS, m);

	}

	public void onTestFailure(ITestResult result) {

		String nomeMetodo = result.getMethod().getMethodName();
		String exceptionMessage = Arrays.toString(result.getThrowable().getStackTrace());

		extentTest.get().fail("<details><summary><b><font color=red>" + "Detalhes da Falha:"
				+ "</font><br></b></sumary>" + exceptionMessage.replaceAll(",", "<br>") + "</details> \n");

		BaseTests2 base = new BaseTests2();

		// Precisa alterar esse método captura de tela
		String path = base.capturarTela2(nomeMetodo, "FALHA");

		try {
			extentTest.get().fail("<b><font color=red>" + "Detalhes da Falha - Imagem:" + "</font><br></b>",
					MediaEntityBuilder.createScreenCaptureFromPath(path).build());// Anexa o screenshot ao relatório

		} catch (Exception e) {
			extentTest.get().fail("O teste falhou, mas não foi possível gerar o print.");
		}

		String texto = "<b>O método de teste " + result.getMethod().getMethodName() + " falhou</b>";
		Markup m = MarkupHelper.createLabel(texto, ExtentColor.RED);
		extentTest.get().log(Status.FAIL, m);
	}

	public void onTestSkipped(ITestResult result) {

		String texto = "<b>Test Method " + result.getMethod().getMethodName() + " Skipped</b>";
		Markup m = MarkupHelper.createLabel(texto, ExtentColor.YELLOW);
		extentTest.get().log(Status.SKIP, m);
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	public void onStart(ITestContext context) {

	}

	public void onFinish(ITestContext context) {

		if (extent != null) {
			extent.flush();
		}
	}
}
