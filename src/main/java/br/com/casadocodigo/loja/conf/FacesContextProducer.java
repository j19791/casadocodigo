package br.com.casadocodigo.loja.conf;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;

//o CDI e o JSF ainda não estão totalmente integrados e facesContext não injeta diretamente. Para que funcione, indicaremos para o CDI como criar o objeto que será injetado em nosso código. 
public class FacesContextProducer {

	@Produces
	@RequestScoped
	public FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}
}
