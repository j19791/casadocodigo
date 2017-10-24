package br.com.casadocodigo.loja.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonArrayBuilder;

import br.com.casadocodigo.loja.daos.CompraDao;
import br.com.casadocodigo.loja.services.PagamentoGateway;

@SessionScoped // Normalmente queremos que os dados de um carrinho de compras permaneça durante
				// toda a sessão do usuário. O CarrinhoCompras deverá permanecer "vivo" por toda
				// a sessão. Todas as classes ligadas ao carrinho deverão ser serializable
@Named // através do cdi, a classe vira um bean, podendo ser utilizado no xhtml
public class CarrinhoCompras implements Serializable {

	private static final long serialVersionUID = 1L;
	// CarrinhoItem : livro não guarda qtd. CarrinhoItem = livro + qtd desse livro
	private Set<CarrinhoItem> itens = new HashSet<>(); // set <> list : set ñ permite repetição. Precisamos ainda usar o

	@Inject
	// private UsuarioDao usuarioDao; //a compra esta sendo salva junto com o
	// usuario em cascata.
	private CompraDao compraDao;
	// hashCode da classe q estamos usando (CarrinhoItem)

	@Inject
	private PagamentoGateway pagamentoGateway;

	public void add(CarrinhoItem item) {
		itens.add(item);
	}

	public List<CarrinhoItem> getItens() {// deverá retornar list p/ ser usado no ui;repeat q só trabalha com list
		return new ArrayList<CarrinhoItem>(itens); // conversao de set p/ list
	}

	public BigDecimal getTotal() {
		BigDecimal total = BigDecimal.ZERO;
		for (CarrinhoItem carrinhoItem : itens) {
			total = total
					.add(carrinhoItem.getLivro().getPreco().multiply(new BigDecimal(carrinhoItem.getQuantidade())));
		}
		return total;
	}

	public BigDecimal getTotal(CarrinhoItem item) {
		return item.getLivro().getPreco().multiply(new BigDecimal(item.getQuantidade()));
	}

	public void remover(CarrinhoItem item) {
		this.itens.remove(item);

	}

	public Integer getQuantidadeTotal() {
		return itens.stream().mapToInt(item -> item.getQuantidade()).sum();
		// pegar o total de itens em relação à quantidade, inclusive dentro de cada item
	}

	public void finalizar(Compra compra) {// Usuario usuario

		compra.setItens(this.toJson());// os itens da compra serão pegos dos itens do carrinho de compras
		// usuarioDao.salvar(usuario);//nao precisa mais pois usuario esta sendo salvo
		// em cascata
		compraDao.salvar(compra);

		String response = pagamentoGateway.pagar(getTotal()); // código refatorado - extraido método p/ a classe
																// PagamentoGateway - lógica do negócio mais simples.

		System.out.println(response);// teste
	}

	public String toJson() {
		JsonArrayBuilder builder = Json.createArrayBuilder();
		for (CarrinhoItem item : itens) {
			builder.add(Json.createObjectBuilder().add("titulo", item.getLivro().getTitulo())
					.add("preco", item.getLivro().getPreco()).add("quantidade", item.getQuantidade())
					.add("total", getTotal(item)));
		}
		return builder.build().toString();
	}

}
