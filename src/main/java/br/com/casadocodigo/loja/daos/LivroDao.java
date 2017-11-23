package br.com.casadocodigo.loja.daos;

import java.util.List;

import javax.ejb.Stateful;
import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.hibernate.jpa.QueryHints;

import br.com.casadocodigo.loja.models.Livro;

@Stateful // especificação: EXTENDED. LivroDao precisará ser um EJB do tipo Statefull
public class LivroDao {

	// @PersistenceContext // JPA possa injetá-lo no nosso Dao
	@PersistenceContext(type = PersistenceContextType.EXTENDED) // mantendo a conexão aberta para que o usuário possa
																// carregar tudo (relacionamentos @manytomany), mesmo
																// sendo Lazy
	private EntityManager manager; // objeto responsável por gerenciar nossas entidades, mantendo a ligação delas
									// com o banco de dados

	public void salvar(Livro livro) {
		manager.persist(livro);
	}

	public List<Livro> listar() {

		// Como podemos ter mais de um autor para cada livro temos que fazer um distinct
		// para distinguir o livro independente da quantidade de autores
		String jpql = "select distinct(l) from Livro l" + " join fetch l.autores";

		return manager.createQuery(jpql, Livro.class).getResultList();
	}

	public List<Livro> ultimosLancamentos() {
		String jpql = "select l from Livro l order by l.id desc";
		// queremos apenas os últimos 5 livros cadastrados.
		return manager.createQuery(jpql, Livro.class).setMaxResults(5).setHint(QueryHints.HINT_CACHEABLE, true)// fazer
																												// com
																												// que
																												// as
																												// queries
																												// vão
																												// para
																												// o
																												// cache
				.getResultList();
	}

	public List<Livro> demaisLivros() {
		String jpql = "select l from Livro l order by l.id desc";
		// informar que o primeiro resultado é do 5º em diante (0-4). (fora os lanctos)
		return manager.createQuery(jpql, Livro.class).setFirstResult(5).setHint(QueryHints.HINT_CACHEABLE, true)
				.getResultList();
	}

	public void limpaCache() {

		// abaixo, utilizando o jpa - o hibernate ainda tem suas próprias extensões de
		// cache
		Cache cache = manager.getEntityManagerFactory().getCache();// pegar as entidades que estão dentro do cache e
																	// fazer uma determinada atualização delas

		cache.evict(Livro.class);// o método evict() passando o Livro para limpar seu cache.
		// evict(Livro.class, 1l) pegaria apenas o primeiro livro da lista, por exemplo.
		// evictAll() - limpa o cache de todos, seja livro, autor, etc
	}

	public Livro buscarPorId(Integer id) {
		return manager.find(Livro.class, id);// utilizando 2o. tratamentro do LazyInitializationException: Extended
												// Entity Manager

		// melhor tratamento do LazyInitializationException: planned queries - menos
		// consultas até o banco de dados, tornando a aplicação mais performática
		// Ao realizarmos a busca pelo Livro,
		// o JPA buscou apenas pelo próprio Livro, e não pelos Autores relacionados a
		// ele (preguiçoso). Só quando precisamos dos autores na tela, o JPA tentou
		// carregá-los. Porém a conexão com o banco de dados já havia sido fechada.
		// Deve-se Buscar o livro e junto deste, trazer os autores. Fazer uso do JPQL
		// com join fetch. O
		// fetch tem a capacidade de realizar a busca dos autores
		// bem no momento em que buscamos o livro.
		// String jpql = "select l from Livro l join fetch l.autores " + "where l.id =
		// :id";
		// return manager.createQuery(jpql, Livro.class).setParameter("id",
		// id).getSingleResult();
	}
}
