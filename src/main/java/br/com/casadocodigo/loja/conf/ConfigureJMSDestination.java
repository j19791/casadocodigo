package br.com.casadocodigo.loja.conf;

import javax.jms.JMSDestinationDefinition;

//para que nosso servidor entenda que o destino é um tópico e para onde ele será enviado para todo mundo que se registra nele. 
@JMSDestinationDefinition(name = "java:/jms/topics/CarrinhoComprasTopico", interfaceName = "javax.jms.Topic" // avisamos
																												// que é
																												// um
																												// tópico
)
// @Singleton
public class ConfigureJMSDestination {

}
