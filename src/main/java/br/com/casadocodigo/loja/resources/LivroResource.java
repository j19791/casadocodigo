package br.com.casadocodigo.loja.resources;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.jaxb.Wrapped;

import br.com.casadocodigo.loja.daos.LivroDao;
import br.com.casadocodigo.loja.models.Livro;

//http://localhost:8080/casadocodigo/service/livros/lancamentos
//get, Accept:application/json
//disponibilizar os serviços como recursos de nossa aplicação
@Path("livros") // informar qual o Path para atender nosso usuário, especificando o nome do
				// caminho
public class LivroResource {

	@Inject
	private LivroDao dao;

	/*
	 * // pegaremos todos os últimos lançamentos que vêm do banco de dados e //
	 * retorná-los como JSON
	 * 
	 * @Path("json") // caminho mais especifico -
	 * 
	 * @GET // os ultimos lançamentos serão chamados pelo método GET
	 * 
	 * @Produces({ MediaType.APPLICATION_JSON }) // para que eles sejam retornados
	 * em JSON, Precisaremos configurar que // queremos produzir public List<Livro>
	 * ultimosLancamentosJson() { System.out.println("teste");
	 * 
	 * return dao.ultimosLancamentos(); }
	 * 
	 * @GET
	 * 
	 * @Path("xml")
	 * 
	 * @Produces({ MediaType.APPLICATION_XML })
	 * 
	 * @Wrapped(element = "livros") //Ele usou <collection> como tag principal. Para
	 * solucionar isso teremos que utilizar uma configuração do RESTEasy public
	 * List<Livro> ultimosLancamentosXml() { return dao.ultimosLancamentos(); }
	 */

	// utilizando o content negotiation: o client definirá qual formato deseja a
	// partir do header Accept
	@GET
	@Path("lancamentos")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Wrapped(element = "livros")
	public List<Livro> ultimosLancamentos() {
		return dao.ultimosLancamentos();
	}
}
