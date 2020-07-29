package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

public class ModalProdutoPage {

	private WebDriver driver;

	//itens da tela
	private By mensagemProdutoAdicionado = By.id("myModalLabel");
	private By descricaoProduto = By.className("product-name");
	private By precoProduto = By.cssSelector("div.modal-body p.product-price");
	private By listaValoresInformados = By.cssSelector("div.divide-right .col-md-6:nth-child(2) span strong");
	private By subTotal = By.cssSelector(".cart-content p:nth-child(2) span.value");
	private By botaoProceedToCheckout = By.cssSelector("div.cart-content-btn a.btn-primary");

	public ModalProdutoPage(WebDriver driver) {
		this.driver = driver;
	}

	public String obterMensagemProdutoAdicionado() {
		// espera de 5 segundos, a cada 1 segundo ignora o erro NoSuchElementException
		// de objeto n�o encontrado
		FluentWait wait = new FluentWait(driver).withTimeout(Duration.ofSeconds(15)).pollingEvery(Duration.ofSeconds(1))
				.ignoring(NoSuchElementException.class);

		// parada do wait, esperar que a mensagem apare�a
		wait.until(ExpectedConditions.visibilityOfElementLocated(mensagemProdutoAdicionado));

		return driver.findElement(mensagemProdutoAdicionado).getText();
	}

	public String obterDescricaoProduto() {
		return driver.findElement(descricaoProduto).getText();
	}

	public String obterPrecoProduto() {
		return driver.findElement(precoProduto).getText();
	}

	public String obterTamanhoProduto() {
		return driver.findElements(listaValoresInformados).get(0).getText();
	}

	public String obterCorProduto() 
	{
		if(driver.findElements(listaValoresInformados).size() == 3)
			return driver.findElements(listaValoresInformados).get(1).getText();
		else 
			//quando o item não tem cores disponíveis
			return "N/A";
	}

	public String obterQuantidadeProduto() {

		if(driver.findElements(listaValoresInformados).size() == 3)
			return driver.findElements(listaValoresInformados).get(2).getText();
		
		else
			return driver.findElements(listaValoresInformados).get(1).getText();					
	}

	public String obterSubTotal() {
		return driver.findElement(subTotal).getText();
	}

	public CarrinhoPage clicarBotaoProceedToCheckout() {
		driver.findElement(botaoProceedToCheckout).click();

		return new CarrinhoPage(driver);
	}
}