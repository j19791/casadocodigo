package br.com.casadocodigo.loja.services;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import br.com.casadocodigo.loja.daos.CompraDao;
import br.com.casadocodigo.loja.models.Compra;

@Path("/pagamento") // irá atender a URL /pagamento
public class PagamentoService {

	// service executando de forma assíncrona (não trava a aplicação esperando o fim
	// da execução) : Executor Service nos deixará executar por meio de um pool de
	// conexões
	private static ExecutorService executor = Executors.newFixedThreadPool(50);// 50 é a quantidade de Threads que
																				// queremos que sejam criadas pelo
																				// executor. Esse Pool de threads
																				// autogerenciável pelo Java.

	@Inject
	private CompraDao compraDao;// para pegar a compra elo próprio uuid

	@Inject
	private PagamentoGateway pagamentoGateway;

	@Context
	ServletContext context; // /casadocodigo

	@POST
	// @Suspended notifica o sevidor que toda a execução desse método deve ser feita
	// em um contexto assíncrono. precisamos notificar o servidor de que a
	// requisição acabou
	public void pagar(@Suspended final AsyncResponse ar, @QueryParam("uuid") String uuid) {

		Compra compra = compraDao.buscaPorUuid(uuid);

		String contextPath = context.getContextPath();

		executor.submit(() -> {

			try {
				String resposta = pagamentoGateway.pagar(compra.getTotal());
				System.out.println(resposta);

				// construir a Response: mandar o usuário, da tela de pagamento para a tela
				// inicial, com uma mensagem
				URI responseUri = UriBuilder.fromPath("http://localhost:8080" + contextPath + "/index.xhtml")
						.queryParam("msg", "Compra Realizada com Sucesso").build();
				Response response = Response.seeOther(responseUri).build();
				ar.resume(response); // precisamos notificar o servidor de que a requisição acabou: Depois de ser
										// feita toda a requisição é chamado a resposta de sucesso

			} catch (Exception e) {
				ar.resume(new WebApplicationException(e)); // resposta de erro
			}

		});
	}

}
