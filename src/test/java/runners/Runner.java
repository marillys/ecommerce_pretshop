package runners;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

//Executa os passos e salva o resultados em relatórios com formato html, json, xml
@RunWith(Cucumber.class)
@CucumberOptions(
		features = "src\\test\\resources\\features\\comprarProduto.feature",
		glue = "steps",
		tags = "@fluxopadrao",
		plugin = {"pretty",
				"html:target/cucumber.html","json:target/cucumber.json","junit:target/cucumber.xml"},
		monochrome = true
		)
public class Runner {
	
}
