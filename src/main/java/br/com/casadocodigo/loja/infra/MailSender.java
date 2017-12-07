package br.com.casadocodigo.loja.infra;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@ApplicationScoped // queremos injeta-lo no PagamentoService: ele irá durar todo o escopo da
					// aplicação. O CDI vai criar o objeto e o manterá vivo durante todo o contexto
					// da aplicação.
public class MailSender {

	@Resource(mappedName = "java:/jboss/mail/gmail") // , session deve ser carregado por um recurso da aplicação. nome
														// da JNDI
	// que será associada ao recurso. Toda vez que se trata de Java EE,
	// algumas configurações devem ser feitas. Na aba "Servers" selecionamos
	// standalone-full.xml
	private Session session;

	public void send(String from, String to, String subject, String body) {

		// MimeMessage já é provido pelo JEE através da especificação de JavaMail. Este
		// nos fornece o objeto session, o qual deve ser carregado por um recurso da
		// aplicação:
		MimeMessage message = new MimeMessage(session);

		try {
			message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(to));

			message.setFrom(new InternetAddress(from));
			message.setSubject(subject);
			message.setContent(body, "text/html");

			Transport.send(message);// envio propriamente dito

		} catch (MessagingException e) {
			throw new RuntimeException(e);// Dessa forma temos certeza que a Exception será vista por alguém
		}
	}

}
