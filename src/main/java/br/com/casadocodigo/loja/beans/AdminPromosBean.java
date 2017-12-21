package br.com.casadocodigo.loja.beans;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import br.com.casadocodigo.loja.models.Promo;
import br.com.casadocodigo.loja.websockets.PromosEndPoint;

@Model
public class AdminPromosBean {

	@Inject
	private PromosEndPoint promos;

	private Promo promo = new Promo();// evita null pointer exception

	public void enviar() {
		promos.send(promo);
	}

	public Promo getPromo() {
		return promo;
	}

	public void setPromo(Promo promo) {
		this.promo = promo;
	}
}
