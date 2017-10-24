package br.com.casadocodigo.loja.services;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

import br.com.casadocodigo.loja.models.Pagamento;

public class PagamentoGateway implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String pagar(BigDecimal total) {
		Client client = ClientBuilder.newClient(); // cliente que faz requisição REST (especif. JAX-RS JEE)
		Pagamento pagamento = new Pagamento(total); // a requisição é de um pagamento do valor total do carrinho
		String target = "http://book-payment.herokuapp.com/payment"; // uri do serviço rest
		Entity<Pagamento> json = Entity.json(pagamento);// Entity do JAX-RS é quem faz a transformação de um determinado
														// objeto para Json para assim ser enviado
		/*
		 * WebTarget webTarget = client.target(target); Builder request =
		 * webTarget.request(); // cria uma requisicao String response =
		 * request.post(json, String.class);
		 */// posta requisicao json, que retorna um String (json)

		// codigo menos verboso - fluente c/ encadeamento de métodos
		String response = client.target(target).request().post(Entity.json(pagamento), String.class);
		return response;
	}
}
