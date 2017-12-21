package br.com.casadocodigo.loja.security;

import java.security.Principal;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import br.com.casadocodigo.loja.daos.SecurityDao;
import br.com.casadocodigo.loja.models.SystemUser;

@Model
public class CurrentUser {

	@Inject
	private HttpServletRequest request;

	@Inject
	private SecurityDao securityDao;

	// private SystemUser principal;

	private SystemUser systemUser;

	@PostConstruct
	private void loadSystemUser() {// carregar usuario apos a instanciação da classe

		Principal principal = request.getUserPrincipal();
		if (principal != null) {
			String email = request.getUserPrincipal().getName();
			systemUser = securityDao.findByEmail(email);
		}
	}

	public SystemUser get() {
		return systemUser;
	}

	public boolean hasRole(String name) {
		return request.isUserInRole(name);
	}

	public String logout() {
		request.getSession().invalidate(); // invalida a sessão do JAAS

		return "/admin/livros/lista.xhtml?faces-redirect=true";// vai p/ login pois nao esta autenticado
	}
}
