package br.com.casadocodigo.loja.services;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
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

	@Inject // O JMS é uma espec do JEE p/ enviar uma mensagem assíncrona.
	private JMSContext jmsContext;// objeto que tem toda a comunicação com o servidor. Podemos criar a mensagem.

	@Resource(name = "java:/jms/topics/CarrinhoComprasTopico") // O destination é um recurso JMS encontrado através do
																// JNDI
	// tipos de mensagens assíncronas:
	// Tópico (topic): lista de discussão onde todos recebem a mesma mensagem.
	// Fila (queue): envia um e-mail para o primeiro da fila, o segundo poderá
	// receber outra mensagem.
	private Destination destination;// O destination é criado e configurado pelo servidor em tempo de execução

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
		// AsyncResponse: deixar a aplicação + rapida. AsyncResponse está abrindo uma
		// thread q roda por traz da aplicação de modo assíncrono no qual novos usuários
		// ficarão executando uma thread nova. É um assíncrono no servidor para novos
		// usuários.

		Compra compra = compraDao.buscaPorUuid(uuid);

		String contextPath = context.getContextPath();

		JMSProducer producer = jmsContext.createProducer();// precisaremos de um produtor e de um "ouvidor" (listener)
															// dessa mensagem.

		executor.submit(() -> {

			try {
				String resposta = pagamentoGateway.pagar(compra.getTotal());
				System.out.println(resposta);

				// assíncrono mais aparente para o usuário. Depois de termos a aprovação da
				// resposta enviaremos o e-mail e liberamos o usuário

				// construir a Response: mandar o usuário, da tela de pagamento para a tela
				// inicial, com uma mensagem
				URI responseUri = UriBuilder.fromPath("http://localhost:8080" + contextPath + "/index.xhtml")
						.queryParam("msg", "Compra Realizada com Sucesso").build();
				Response response = Response.seeOther(responseUri).build();

				producer.send(destination, compra.getUuid());

				// isolando o envio de email p/ ser executado em 2o. plano enquanto o pagto é
				// finalizado
				/*
				 * String messageBody =
				 * "Sua compra foi realizada com sucesso, com o número de pedido " +
				 * compra.getUuid();
				 * 
				 * mailSender.send("compras@casacodigo.com.br", // quem manda
				 * compra.getUsuario().getEmail(), // quem recebe "Nova Compra na CDC", //
				 * assunto messageBody);// corpo
				 **/
				ar.resume(response); // precisamos notificar o servidor de que a requisição
				// acabou: Depois de ser feita toda a requisição é chamado a resposta de sucesso

			} catch (Exception e) {
				ar.resume(new WebApplicationException(e)); // resposta de erro
			}

		});
	}

}
