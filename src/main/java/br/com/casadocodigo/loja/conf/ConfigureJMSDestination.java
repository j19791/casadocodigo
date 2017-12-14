package br.com.casadocodigo.loja.conf;

import javax.ejb.Singleton;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;

//para que nosso servidor entenda que o destino é um tópico e para onde ele será enviado para todo mundo que se registra nele. 
@JMSDestinationDefinitions({
		@JMSDestinationDefinition(name = "java:/jms/topics/CarrinhoComprasTopico", interfaceName = "javax.jms.Topic", destinationName = "java:/jms/topics/CarrinhoComprasTopico") })
@Singleton
public class ConfigureJMSDestination {

}
