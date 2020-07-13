package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

public class ModalProdutoPage {
	
	private WebDriver driver;
	
	private By mensagemProdutoAdicionado = By.id("myModalLabel");
	
	public ModalProdutoPage(WebDriver driver) 
	{
		this.driver = driver;
	}
	
	public String obterMensagemProdutoAdicionado() 
	{
		//espera de 5 segundos, a cada 1 segundo ignora o erro NoSuchElementException de objeto não encontrado
		FluentWait wait = new FluentWait(driver).withTimeout(
				Duration.ofSeconds(15))
				.pollingEvery(Duration.ofSeconds(1))
				.ignoring(NoSuchElementException.class);
		
		//parada do wait, esperar que a mensagem apareça
		wait.until(ExpectedConditions.visibilityOfElementLocated(mensagemProdutoAdicionado));
		
		return driver.findElement(mensagemProdutoAdicionado).getText();
	}
}
