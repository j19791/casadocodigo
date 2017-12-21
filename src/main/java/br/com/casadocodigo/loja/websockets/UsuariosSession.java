package br.com.casadocodigo.loja.websockets;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;

@ApplicationScoped // deverá sobreviver enquanto a aplicação estiver ativa
public class UsuariosSession {

	// salvar a lista com os usuários que estão na home da casa do código
	private List<Session> sessions = new ArrayList<>();

	// recebe um Session (usuario), e o adiciona na lista.
	public void add(Session session) {
		sessions.add(session);
	}

	// retorna a lista de sessões (usuarios)
	public List<Session> getUsuarios() {
		return sessions;
	}

	public void remove(Session session) {
		sessions.remove(session); // remover um usuário da lista de sessions quando ele fechar o navegador

	}
}
