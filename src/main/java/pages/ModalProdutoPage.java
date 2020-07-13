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
	private By descricaoProduto = By.className("product-name");
	private By precoProduto = By.cssSelector("div.modal-body p.product-price");
	private By listaValoresInformados = By.cssSelector("div.divide-right .col-md-6:nth-child(2) span strong");
	private By subTotal = By.cssSelector(".cart-content p:nth-child(2) span.value");
	
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
	
	public String obterDescricaoProduto()
	{
		return driver.findElement(descricaoProduto).getText();
	}
	
	public String obterPrecoProduto() 
	{
		return driver.findElement(precoProduto).getText();
	}
	
	public String obterTamanhoProduto() 
	{
		return driver.findElements(listaValoresInformados).get(0).getText();
	}
	
	public String obterCorProduto() 
	{
		return driver.findElements(listaValoresInformados).get(1).getText();
	}
	
	public String obterQuantidadeProduto() 
	{
		return driver.findElements(listaValoresInformados).get(2).getText();
	}
	
	public String obterSubTotal() 
	{
		return driver.findElement(subTotal).getText();
	}
}
