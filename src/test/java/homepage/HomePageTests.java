package homepage;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import base.BaseTests;
import pages.CarrinhoPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ModalProdutoPage;
import pages.PedidoPage;
import pages.ProdutoPage;
import util.Funcoes;

public class HomePageTests extends BaseTests {

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
	public void testContarProdutos_oitoProdutosDiferentes() {
		carregarPaginaInicial();
		assertThat(homePage.contarProdutos(), is(8));
	}

	@Test
	public void testValidarCarrinhoZerado_ZeroItensNoCarrinho() {
		int produtosNoCarrinho = homePage.obterQuantidadeProdutosNoCarrinho();

		assertThat(produtosNoCarrinho, is(0));
	}

	@Test
	public void testValidarDetalhesDoProduto_DescricaoEValorIguais() {
		int indice = 0;

		String nomeProduto_HomePage = homePage.obterNomeProduto(indice);
		String precoProduto_HomePage = homePage.obterPrecoProduto(indice);

		produtoPage = homePage.clicarProduto(indice);

		// nessa nova pagina, o produto e unico e nao uma colecao de valores
		nomeProduto_ProdutoPage = produtoPage.obterNomeProduto();
		String precoProduto_ProdutoPage = produtoPage.obterPrecoProduto();

		assertThat(nomeProduto_HomePage.toUpperCase(), is(nomeProduto_ProdutoPage.toUpperCase()));
		assertThat(precoProduto_HomePage, is(precoProduto_ProdutoPage));
	}

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

	//usando arquivo csv contendo massa de testes
	@ParameterizedTest
	@CsvFileSource(resources = "/massaTeste_Login.csv", numLinesToSkip = 1, delimiter = ';')
	public void testLogin_UsuarioLogadoComDadosValidos(String nomeTeste, String email, String password,
			String nomeUsuario, String resultado) {
		// Clicar no bot�o sign In na home page
		loginPage = homePage.clicarBotaoSignIn();

		// preencher login com dados da planilha/dados do parametro
		loginPage.preencherEmail(email);
		loginPage.preencherPassword(password);

		// logar
		loginPage.clicarBotaoSignIn();
		
		boolean esperado_loginOk;//se o resultado for positivo, altera o valor dessa variavel para verdadeiro
		if(resultado.equals("positivo"))
			esperado_loginOk = true;
		else
			esperado_loginOk = false;
		
		// validar o usuario logado com a variavel esperado_loginOk
		assertThat(homePage.estaLogado(nomeUsuario), is(esperado_loginOk));

		capturarTela(nomeTeste, resultado);
		
		//deslogar quando o resultado esperado for positivo
		if(esperado_loginOk)
			homePage.clicarBotaoSignOut();
		
		// retornar para a p�gina inicial
		carregarPaginaInicial();
	}

	@Test
	public void testIncluirProdutoNoCarrinho_ProdutoIncluidoComSucesso() {
		String tamanhoProduto = "M";
		String corProduto = "Black";
		String precoProdutoString, subtotalProdutoString;
		Double precoProduto, subtotalProduto, subTotalCalculado;

		int quantidadeProduto = 2;

		// caso nao esteja logado, far� o login
		if (!homePage.estaLogado("Marilia Alves")) {
			testLoginComSucesso_UsuarioLogado();
		}

		// Selecionando produto
		testValidarDetalhesDoProduto_DescricaoEValorIguais();

		// Saber o que tem selecionado
		List<String> listaOpcoes = produtoPage.obterOpcoesSelecionadas();

		// Selecionar tamanho, cor e quantidade
		produtoPage.selecionarOpcaoDropDown(tamanhoProduto);
		produtoPage.selecionarCorPreta();
		produtoPage.alterarQuantidade(quantidadeProduto);

		// Clicar no bot�o Add Cart
		modalProdutoPage = produtoPage.clicarBotaoAddToCart();

		// Valida��es
		assertTrue(modalProdutoPage.obterMensagemProdutoAdicionado()
				.endsWith("Product successfully added to your shopping cart"));

		assertThat(modalProdutoPage.obterDescricaoProduto().toUpperCase(), is(nomeProduto_ProdutoPage.toUpperCase()));

		precoProduto = Funcoes.removeCifraoDevolveDouble(modalProdutoPage.obterPrecoProduto());

		subtotalProdutoString = modalProdutoPage.obterSubTotal().replace("$", "");
		subtotalProduto = Double.parseDouble(subtotalProdutoString);

		// Calcular subtotal
		subTotalCalculado = precoProduto * quantidadeProduto;

		assertThat(modalProdutoPage.obterTamanhoProduto(), is(tamanhoProduto));
		assertThat(Integer.parseInt(modalProdutoPage.obterQuantidadeProduto()), is(quantidadeProduto));
		assertThat(modalProdutoPage.obterCorProduto(), is(corProduto));
		assertThat(subtotalProduto, is(subTotalCalculado));
	}

	@Test
	public void testIrParaCarrinho_InformacoesPersistidas() {
		// --Pr� condi�oes
		// incluir produto no carrinho
		testIncluirProdutoNoCarrinho_ProdutoIncluidoComSucesso();

		carrinhoPage = modalProdutoPage.clicarBotaoProceedToCheckout();

		// validar elementos da tela
		assertThat(carrinhoPage.obter_nomeProduto(), is(esperado_nomeProduto));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_precoProduto()), is(esperado_precoProduto));
		assertThat(carrinhoPage.obter_tamanhoProduto(), is(esperado_tamanhoProduto));
		assertThat(carrinhoPage.obter_corProduto(), is(esperado_corProduto));
		assertThat(Integer.parseInt(carrinhoPage.obter_input_quantidadeProduto()),
				is(esperado_input_quantidadeProduto));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_subTotalProduto()),
				is(esperado_subtotalProduto));

		assertThat(Funcoes.removeTextoItemsDevolveInt(carrinhoPage.obter_numeroItensTotal()),
				is(esperado_numeroItensTotal));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_subtotalTotal()), is(esperado_subtotalTotal));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_shippingTotal()), is(esperado_shippingTotal));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_totalTaxExclTotal()),
				is(esperado_totalTaxExclTotal));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_totalTaxIncTotal()),
				is(esperado_totalTaxIncTotal));
		assertThat(Funcoes.removeCifraoDevolveDouble(carrinhoPage.obter_taxesTotal()), is(esperado_taxesTotal));
	}

	@Test
	public void testIrParaCheckout_FreteMeioPagamentoEnderecoListadosOk() {
		testIrParaCarrinho_InformacoesPersistidas();

		checkoutPage = carrinhoPage.clicarBotaoProceedToCheck();

		assertThat(Funcoes.removeCifraoDevolveDouble(checkoutPage.obter_totalTaxIncTotal()),
				is(esperado_totalTaxIncTotal));
		
		// assertThat(checkoutPage.obter_nomeCliente(), is(esperado_nomeCliente));
		assertTrue(checkoutPage.obter_nomeCliente().startsWith(esperado_nomeCliente));

		checkoutPage.clicarBotaoContinueAddress();

		String encontrado_shippingValor = checkoutPage.obter_shippingValor();
		encontrado_shippingValor = Funcoes.removeTexto(encontrado_shippingValor, " tax excl.");
		Double encontrado_shippingValor_Double = Funcoes.removeCifraoDevolveDouble(encontrado_shippingValor);

		assertThat(encontrado_shippingValor_Double, is(esperado_shippingTotal));

		// clicar no botao continue
		checkoutPage.clicarBotaoContinueShipping();

		// Selecionar op��o "pay by check"
		checkoutPage.selecionarRadioPayByCheck();

		String encontrado_amountPayByCheck = checkoutPage.obter_amontPayByCheck();
		encontrado_amountPayByCheck = Funcoes.removeTexto(encontrado_amountPayByCheck, " (tax incl.)");
		Double encontrado_amountPayByCheck_Double = Funcoes.removeCifraoDevolveDouble(encontrado_amountPayByCheck);

		// validar valor do cheque
		assertThat(encontrado_amountPayByCheck_Double, is(esperado_totalTaxIncTotal));

		// clicar na op��o "I agree"
		checkoutPage.selecionarCheckboxIAgree();

		assertTrue(checkoutPage.estaSelecionadoCheckboxIAgree());
	}

	@Test
	public void testFinalizarPedido_pedidoFinalizadoComSucesso() {
		testIrParaCheckout_FreteMeioPagamentoEnderecoListadosOk();

		// teste
		// clicar no bot�o que confirma o pedido
		pedidoPage = checkoutPage.clicarBotaoConfirmaPedido();

		// Validar valores da tela
		assertTrue(pedidoPage.obter_textoPedidoConfirmado().toUpperCase().endsWith("YOUR ORDER IS CONFIRMED"));

		assertThat(pedidoPage.obter_email(), is(email));
		assertThat(pedidoPage.obter_totalProdutos(), is(esperado_subtotalProduto));
		assertThat(pedidoPage.obter_totalTaxIncl(), is(esperado_totalTaxIncTotal));
		assertThat(pedidoPage.obter_metodoPagamento(), is("check"));
	}
}