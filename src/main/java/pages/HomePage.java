package pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage {

	private WebDriver driver;
	
	List<WebElement> listaProdutos = new ArrayList();
	
	private By textoProdutosNoCarrinho = By.className("cart-products-count");
	private By produtos = By.className("product-description");
	private By descricoesDosProdutos = By.cssSelector(".product-description a");
	private By precoDosProdutos = By.className("price");
	
	//usuario logado
	private By usuarioLogado = By.cssSelector("#_desktop_user_info span.hidden-sm-down");
	
	//login
	private By botaoSignIn = By.cssSelector("#_desktop_user_info span.hidden-sm-down");
	
	public HomePage(WebDriver driver) 
	{
		this.driver = driver;
	}
	
	public int contarProdutos() 
	{
		carregarListaProdutos();
		return listaProdutos.size();
	}
	
	private void carregarListaProdutos() 
	{
		listaProdutos = driver.findElements(produtos);
	}
	
	public int obterQuantidadeProdutosNoCarrinho() 
	{
		String quantidadeProdutoNoCarrinho = driver.findElement(textoProdutosNoCarrinho).getText();
		quantidadeProdutoNoCarrinho = quantidadeProdutoNoCarrinho.replace("(","");
		quantidadeProdutoNoCarrinho = quantidadeProdutoNoCarrinho.replace(")","");
		
		int qtdProdutosNoCarrinho = Integer.parseInt(quantidadeProdutoNoCarrinho);
		
		return qtdProdutosNoCarrinho;
	}
	
	public String obterNomeProduto(int indice) 
	{
		//pegar a lista de elements pelo indice fornecido
		return driver.findElements(descricoesDosProdutos).get(indice).getText();
	}
	
	public String obterPrecoProduto(int indice) 
	{
		return driver.findElements(precoDosProdutos).get(indice).getText();
	}
	
	public ProdutoPage clicarProduto(int indice) 
	{
		driver.findElements(descricoesDosProdutos).get(indice).click();
		
		//retornar uma nova classe, passando o driver de conex�o
		return new ProdutoPage(driver);
	}
	
	public LoginPage clicarBotaoSignIn() 
	{
		driver.findElement(botaoSignIn).click();
		
		return new LoginPage(driver);
	}
	
	public Boolean estaLogado(String texto) 
	{
		//compara o login com o nome passado por parametro
		return texto.contentEquals(driver.findElement(usuarioLogado).getText());
	}
}