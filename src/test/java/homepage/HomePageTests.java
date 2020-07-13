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
	
	String nomeProduto_ProdutoPage;
	
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
		
		//nessa nova página, o produto é único e não uma coleção de valores
		nomeProduto_ProdutoPage= produtoPage.obterNomeProduto();
		String precoProduto_ProdutoPage  = produtoPage.obterPrecoProduto();
		
		assertThat(nomeProduto_HomePage.toUpperCase(), is(nomeProduto_ProdutoPage.toUpperCase()));
		assertThat(precoProduto_HomePage, is(precoProduto_ProdutoPage));
	}
	
	@Test
	public void testLoginComSucesso_UsuarioLogado() 
	{
		//Clicar no botão sign In na home page 
		loginPage = homePage.clicarBotaoSignIn();
		
		//preencher login
		loginPage.preencherEmail("marilia@alves.com");
		loginPage.preencherPassword("123456");
		
		//logar
		loginPage.clicarBotaoSignIn();
		
		//validar o usuario logado
		assertThat(homePage.estaLogado("Marilia Alves"), is(true));
		
		//retornar para a página inicial
		carregarPaginaInicial();
	}
	
	@Test
	public void incluirProdutoNoCarrinho_ProdutoIncluidoComSucesso() 
	{
		String tamanhoProduto = "M";
		String corProduto = "Black";
		String precoProdutoString, subtotalProdutoString;
		Double precoProduto, subtotalProduto, subTotalCalculado;
		
		int quantidadeProduto = 2;
		
		//Pré-condição
		//caso não esteja logado, fará o login
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
		
		//Clicar no botão Add Cart
		ModalProdutoPage modalProdutoPage = produtoPage.clicarBotaoAddToCart();
		
		/*assertThat(modalProdutoPage.obterMensagemProdutoAdicionado(),
				is("Product successfully added to your shopping cart"));*/
		
		//Validações
		assertTrue(modalProdutoPage.obterMensagemProdutoAdicionado().endsWith("Product successfully added to your shopping cart"));
		
		assertThat(modalProdutoPage.obterDescricaoProduto().toUpperCase(), is(nomeProduto_ProdutoPage.toUpperCase()));
		
		//converter o valor unitário do produto
		precoProdutoString = modalProdutoPage.obterPrecoProduto().replace("$", "");
		precoProduto = Double.parseDouble(precoProdutoString);
		
		subtotalProdutoString = modalProdutoPage.obterSubTotal().replace("$", "");
		subtotalProduto = Double.parseDouble(subtotalProdutoString);
		
		//Calcular subtotal
		subTotalCalculado = precoProduto * quantidadeProduto;
		
		assertThat(modalProdutoPage.obterTamanhoProduto(), is(tamanhoProduto));
		assertThat(Integer.parseInt(modalProdutoPage.obterQuantidadeProduto()), is(quantidadeProduto));
		assertThat(modalProdutoPage.obterCorProduto(), is(corProduto));
		assertThat(subtotalProduto, is(subTotalCalculado));
	}

}
