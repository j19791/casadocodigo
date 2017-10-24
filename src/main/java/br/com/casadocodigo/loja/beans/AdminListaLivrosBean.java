package br.com.casadocodigo.loja.beans;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import br.com.casadocodigo.loja.daos.LivroDao;
import br.com.casadocodigo.loja.models.Livro;

//@Named: Como ele é um bean que pertence ao JSF
//@RequestScoped: para que os dados permaneçam na tela durante o request
@Model // CDI unifica estas duas anotações em uma só
public class AdminListaLivrosBean {

	private List<Livro> livros = new ArrayList<>();

	@Inject
	private LivroDao dao;

	public List<Livro> getLivros() {

		this.livros = dao.listar();

		return livros;
	}

	public void setLivros(List<Livro> livros) {
		this.livros = livros;
	}

}
