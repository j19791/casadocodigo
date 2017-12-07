package br.com.casadocodigo.loja.services;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import br.com.casadocodigo.loja.daos.CompraDao;
import br.com.casadocodigo.loja.infra.MailSender;
import br.com.casadocodigo.loja.models.Compra;

@MessageDriven(// conseguimos escutar uma determinada mensagem,podendo ser uma fila ou um
				// tópico.DEstination: quem vai ouvir
		activationConfig = {
				@ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/topics/CarrinhoComprasTopico"), // é
																																			// que
																																			// "ouviremos"
																																			// e,
																																			// como
																																			// nesse
																																			// caso
																																			// estaremos
																																			// ouvindo
																																			// um
				@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic") // tópico
		})
public class EnviaEmailCompra implements MessageListener {

	@Inject
	private MailSender mailSender;

	@Inject
	private CompraDao compraDao;

	public void onMessage(Message message) {

		// o Message não vem como texto. Precisamos adicionar um TextMessage, que irá
		// nos dar uma mensagem específica em String:
		TextMessage textMessage = (TextMessage) message;

		Compra compra;
		try {
			compra = compraDao.buscaPorUuid(textMessage.getText());

			String messageBody = "Sua compra foi realizada com sucesso, com o número de pedido " + compra.getUuid();

			mailSender.send("compras@casacodigo.com.br", compra.getUsuario().getEmail(), "Nova Compra na CDC",
					messageBody);

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
