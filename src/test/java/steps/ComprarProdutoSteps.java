package steps;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.google.common.io.Files;

import static org.hamcrest.Matchers.*;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.hamcrest.MatcherAssert.assertThat;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import pages.HomePage;
import pages.LoginPage;
import pages.ModalProdutoPage;
import pages.ProdutoPage;
import util.Funcoes;

public class ComprarProdutoSteps {

	private static WebDriver driver;
	private HomePage homePage = new HomePage(driver);

	LoginPage loginPage;
	ProdutoPage produtoPage;
	ModalProdutoPage modalProdutoPage;

	String nomeProduto_HomePage;
	String precoProduto_HomePage;
	String nomeProduto_ProdutoPage;
	String precoProduto_ProdutoPage;

	// classe com os passos do cucumber

	@Before
	public static void inicializar() {
		System.setProperty("webdriver.chrome.driver", "C:\\driversExternos\\chromedriver\\83\\chromedriver.exe");

		driver = new ChromeDriver();
	}

	@Dado("que estou na pagina inicial")
	public void que_estou_na_pagina_inicial() {
		homePage.carregarPaginaInicial();
		assertThat(homePage.obterTituloPagina(), is("Loja de Teste"));
	}

	@Quando("nao estou logado")
	public void nao_estou_logado() {
		assertThat(homePage.estaLogado(), is(false));
	}

	@Entao("visualizo {int} produtos disponiveis")
	public void visualizo_produtos_disponiveis(Integer int1) {
		assertThat(homePage.contarProdutos(), is(int1));
	}

	@Entao("carrinho esta zerado")
	public void carrinho_esta_zerado() {
		assertThat(homePage.obterQuantidadeProdutosNoCarrinho(), is(0));
	}

	@Quando("estou logado")
	public void estou_logado() {

		// Clicar no bot�o sign In na home page
		loginPage = homePage.clicarBotaoSignIn();

		// preencher login
		loginPage.preencherEmail("marilia@alves.com");
		loginPage.preencherPassword("123456");

		// logar
		loginPage.clicarBotaoSignIn();

		// validar o usuario logado
		assertThat(homePage.estaLogado("Marilia Alves"), is(true));

		// retornar para a p�gina inicial
		homePage.carregarPaginaInicial();
	}

	@Quando("seleciono um produto na posição {int}")
	public void seleciono_um_produto_na_posição(Integer indice) {
		nomeProduto_HomePage = homePage.obterNomeProduto(indice);
		precoProduto_HomePage = homePage.obterPrecoProduto(indice);

		produtoPage = homePage.clicarProduto(indice);

		// nessa nova p�gina, o produto � �nico e n�o uma cole��o de valores
		nomeProduto_ProdutoPage = produtoPage.obterNomeProduto();
		precoProduto_ProdutoPage = produtoPage.obterPrecoProduto();
	}

	@Quando("nome do produto na tela principal e na tela produto eh {string}")
	public void nome_do_produto_na_tela_principal_eh(String nomeProduto) {
		assertThat(nomeProduto_HomePage.toUpperCase(), is(nomeProduto.toUpperCase()));

		assertThat(nomeProduto_ProdutoPage.toUpperCase(), is(nomeProduto.toUpperCase()));
	}

	@Quando("preco do produto na tela principal e na tela produto eh {string}")
	public void preco_do_produto_na_tela_principal_eh(String precoProduto) {
		assertThat(precoProduto_HomePage, is(precoProduto.toUpperCase()));
		assertThat(precoProduto_ProdutoPage, is(precoProduto.toUpperCase()));
	}

	@Quando("adiciono o produto no carrinho com tamanho {string} cor {string} e quantidade {int}")
	public void adiciono_o_produto_no_carrinho_com_tamanho_cor_e_quantidade(String tamanhoProduto, String corProduto,
			Integer quantidadeProduto) {
		// Saber o que tem selecionado
		List<String> listaOpcoes = produtoPage.obterOpcoesSelecionadas();

		// Selecionar tamanho, cor e quantidade
		produtoPage.selecionarOpcaoDropDown(tamanhoProduto);

		if (!corProduto.equals("N/A"))
			produtoPage.selecionarCorPreta();

		produtoPage.alterarQuantidade(quantidadeProduto);

		// Clicar no bot�o Add Cart
		modalProdutoPage = produtoPage.clicarBotaoAddToCart();

		// Valida��es
		assertTrue(modalProdutoPage.obterMensagemProdutoAdicionado()
				.endsWith("Product successfully added to your shopping cart"));
	}

	@Entao("o produto aparece na confirmacao com nome {string} preco {string} tamanho {string} cor {string} e quantidade {int}")
	public void o_produto_aparece_na_confirmacao_com_nome_preco_tamanho_cor_e_quantidade(String nomeProduto,
			String precoProduto, String tamanhoProduto, String corProduto, Integer quantidadeProduto) {

		assertThat(modalProdutoPage.obterDescricaoProduto().toUpperCase(), is(nomeProduto_ProdutoPage.toUpperCase()));

		Double precoProdutoDoubleEncontrado = Funcoes.removeCifraoDevolveDouble(modalProdutoPage.obterPrecoProduto());
		Double precoProdutoDoubleEsperado = Funcoes.removeCifraoDevolveDouble(precoProduto);

		String subtotalString = modalProdutoPage.obterSubTotal().replace("$", "");
		Double subtotalEncontrado = Double.parseDouble(subtotalString);

		// Calcular subtotal
		Double subTotalCalculadoEsperado = precoProdutoDoubleEsperado * quantidadeProduto;

		assertThat(modalProdutoPage.obterTamanhoProduto(), is(tamanhoProduto));
		assertThat(Integer.parseInt(modalProdutoPage.obterQuantidadeProduto()), is(quantidadeProduto));

		if (!corProduto.equals("N/A"))
			assertThat(modalProdutoPage.obterCorProduto(), is(corProduto));

		assertThat(subtotalEncontrado, is(subTotalCalculadoEsperado));
	}

	// ordem com valores mais altos, executam primeiro
	@After(order = 1)
	public void capturarTela(Scenario scenario) {
		// Scenario é uma classe do cucumber capaz de identificar o caso de teste

		System.out.println("Captura de tela");
		var camera = (TakesScreenshot) driver;

		File capturadeTela = camera.getScreenshotAs(OutputType.FILE);

		String scenarioId = scenario.getId().substring(scenario.getId().lastIndexOf(".feature:") + 9);
		try {

			// O nome do arquivo é composto pelo nome do cenário executado, ID e Status da
			// execução
			Files.move(capturadeTela, new File("resources/screenshots/" + scenario.getName() // Nome do caso de teste
					+ "_" + scenarioId + "_" + scenario.getStatus()// Status do teste
					+ ".png"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Números mais altos rodam primeiro
	@After(order = 0)
	public static void finalizar() {
		driver.quit();
	}
}