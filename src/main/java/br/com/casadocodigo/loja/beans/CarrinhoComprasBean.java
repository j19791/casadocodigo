package br.com.casadocodigo.loja.beans;

import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.casadocodigo.loja.daos.LivroDao;
import br.com.casadocodigo.loja.models.CarrinhoCompras;
import br.com.casadocodigo.loja.models.CarrinhoItem;
import br.com.casadocodigo.loja.models.Livro;

@Named
@Model
public class CarrinhoComprasBean {

	@Inject
	private LivroDao livroDao;

	@Inject
	private CarrinhoCompras carrinho;

	public String add(Integer id) {

		Livro livro = livroDao.buscarPorId(id); // pegar o livro através do id passado
		CarrinhoItem item = new CarrinhoItem(livro); // cria o item com o livro passado
		carrinho.add(item); // adiciona no carrinho o item

		return "carrinho?faces-redirect=true";// enviar o usuário para a tela de carrinho tratado o problema de reenvio
												// indesejado do form

	}

	// lista que ira preencher o carrinho de comparas
	public List<CarrinhoItem> getItens() {//// deverá retornar list p/ ser usado no ui;repeat q só trabalha com list
		return carrinho.getItens();
	}

	public void remover(CarrinhoItem item) {
		carrinho.remover(item);
	}

}
