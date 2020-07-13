package homepage;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import base.BaseTests;
import pages.LoginPage;
import pages.ModalProdutoPage;
import pages.ProdutoPage;

public class HomePageTests extends BaseTests{
	
	LoginPage loginPage;
	ProdutoPage produtoPage;
	
	@Test
	public void testContarProdutos_oitoProdutosDiferentes() 
	{
		carregarPaginaInicial();
		assertThat(homePage.contarProdutos(), is(8));
	}
	
	@Test
	public void testValidarCarrinhoZerado_ZeroItensNoCarrinho() 
	{
		int produtosNoCarrinho = homePage.obterQuantidadeProdutosNoCarrinho();
		
		assertThat(produtosNoCarrinho, is(0));
	}
	
	@Test
	public void testValidarDetalhesDoProduto_DescricaoEValorIguais() 
	{
		int indice = 0;
		
		String nomeProduto_HomePage = homePage.obterNomeProduto(indice);
		String precoProduto_HomePage = homePage.obterPrecoProduto(indice);
		
		produtoPage = homePage.clicarProduto(indice);
		
		//nessa nova p�gina, o produto � �nico e n�o uma cole��o de valores
		String nomeProduto_ProdutoPage= produtoPage.obterNomeProduto();
		String precoProduto_ProdutoPage  = produtoPage.obterPrecoProduto();
		
		assertThat(nomeProduto_HomePage.toUpperCase(), is(nomeProduto_ProdutoPage.toUpperCase()));
		assertThat(precoProduto_HomePage, is(precoProduto_ProdutoPage));
	}
	
	@Test
	public void testLoginComSucesso_UsuarioLogado() 
	{
		//Clicar no bot�o sign In na home page 
		loginPage = homePage.clicarBotaoSignIn();
		
		//preencher login
		loginPage.preencherEmail("marilia@alves.com");
		loginPage.preencherPassword("123456");
		
		//logar
		loginPage.clicarBotaoSignIn();
		
		//validar o usuario logado
		assertThat(homePage.estaLogado("Marilia Alves"), is(true));
		
		//retornar para a p�gina inicial
		carregarPaginaInicial();
	}
	
	@Test
	public void incluirProdutoNoCarrinho_ProdutoIncluidoComSucesso() 
	{
		String tamanhoProduto = "M";
		String corProduto = "Black";
		
		int quantidadeProduto = 2;
		
		//Pr�-condi��o
		//caso n�o esteja logado, far� o login
		if(!homePage.estaLogado("Marilia Alves")) 
		{
			testLoginComSucesso_UsuarioLogado();
		}
		
		//Selecionando produto
		testValidarDetalhesDoProduto_DescricaoEValorIguais();
				
		//Saber o que tem selecionado
		List<String> listaOpcoes = produtoPage.obterOpcoesSelecionadas();
		
		//Selecionar tamanho, cor e quantidade
		produtoPage.selecionarOpcaoDropDown(tamanhoProduto);
		produtoPage.selecionarCorPreta();
		produtoPage.alterarQuantidade(quantidadeProduto);
		
		//Clicar no bot�o Add Cart
		ModalProdutoPage modalProdutoPage = produtoPage.clicarBotaoAddToCart();
		
		/*assertThat(modalProdutoPage.obterMensagemProdutoAdicionado(),
				is("Product successfully added to your shopping cart"));*/
		
		//Valida��es
		assertTrue(modalProdutoPage.obterMensagemProdutoAdicionado().endsWith("Product successfully added to your shopping cart"));
		
		
	}

}
