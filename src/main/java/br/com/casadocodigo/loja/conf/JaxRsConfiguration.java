package br.com.casadocodigo.loja.conf;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/service") // indica o caminho que será atendido
public class JaxRsConfiguration extends Application {

}
