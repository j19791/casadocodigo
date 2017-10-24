package br.com.casadocodigo.loja.beans;

import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import br.com.casadocodigo.loja.models.CarrinhoCompras;
import br.com.casadocodigo.loja.models.Compra;
import br.com.casadocodigo.loja.models.Usuario;

@Model
public class CheckoutBean {

	private Usuario usuario = new Usuario();

	@Inject
	private FacesContext facesContext;

	@Inject
	private CarrinhoCompras carrinho;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Transactional // toda vez que nós realizamos uma operação que altera o estado do banco de
					// dados, deverá haver uma transação
	public void finalizar() {
		Compra compra = new Compra(); // Compra é iguial ao usuario e os itens q desejamos comprar
		compra.setUsuario(usuario); // o usuário é setado na compra

		carrinho.finalizar(compra); // salvar os itens do carrinho

		// Requisição Assíncrona: avisar para o JSF que queremos realizar uma
		// requisição, como se fosse um componete externo, mas dentro da própria
		// aplicação.o que queremos fazer de fato, é pegar o response, tratá-lo e
		// enviá-lo para outro local.
		String contextName = facesContext.getExternalContext().getContextName();
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.setStatus(307); // redirect temporário mantendo o método que foi invocado.
		response.setHeader("Location", "/" + contextName + "/service/pagamento?id=" + compra.getUuid());// dizer para o
																										// response qual
																										// a URL
	}
}