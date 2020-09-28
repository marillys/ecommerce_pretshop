package homepage;

import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentXReporter;

import static org.hamcrest.MatcherAssert.assertThat;

import base.BaseTests;
import base.BaseTests2;
import pages.CarrinhoPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ModalProdutoPage;
import pages.PedidoPage;
import pages.ProdutoPage;
import util.Funcoes;

public class HomePageTests2 extends BaseTests2 {

	//novos objetos
	LoginPage loginPage;
	ProdutoPage produtoPage;
	ModalProdutoPage modalProdutoPage;
	CarrinhoPage carrinhoPage;
	CheckoutPage checkoutPage;
	PedidoPage pedidoPage;

	String nomeProduto_ProdutoPage;

	String email = "marilia@alves.com";
	String esperado_nomeCliente = "Marilia Alves";

	String esperado_nomeProduto = "Hummingbird printed t-shirt";
	Double esperado_precoProduto = 19.12;
	String esperado_tamanhoProduto = "M";
	String esperado_corProduto = "Black";
	int esperado_input_quantidadeProduto = 2;
	Double esperado_subtotalProduto = esperado_precoProduto * esperado_input_quantidadeProduto;

	int esperado_numeroItensTotal = esperado_input_quantidadeProduto;
	Double esperado_subtotalTotal = esperado_subtotalProduto;
	Double esperado_shippingTotal = 7.00;
	Double esperado_totalTaxExclTotal = esperado_subtotalTotal + esperado_shippingTotal;
	Double esperado_totalTaxIncTotal = esperado_totalTaxExclTotal;
	Double esperado_taxesTotal = 0.00;

	@Test
	public void testLoginComSucesso_UsuarioLogado() {
		// Clicar no bot�o sign In na home page
		loginPage = homePage.clicarBotaoSignIn();

		// preencher login
		loginPage.preencherEmail(email);
		loginPage.preencherPassword("123456");

		// logar
		loginPage.clicarBotaoSignIn();

		// validar o usuario logado
		assertThat(homePage.estaLogado("Marilia Alves"), is(true));

		// retornar para a p�gina inicial
		carregarPaginaInicial();
	}
}