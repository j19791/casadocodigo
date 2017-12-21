package br.com.casadocodigo.loja.websockets;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import br.com.casadocodigo.loja.models.Promo;

@ServerEndpoint(value = "/canal/promos") // indicar que nossa aplicação aceita conexões via WebSockets.
public class PromosEndPoint {

	@Inject // deverá ser injetado pois é utilizado @ApplicationScoped em UsuariosSession
	private UsuariosSession usuarios;

	@OnOpen // Quando a conexão for solicitada é executado o metodo abaixo
	public void onMessage(Session session) {
		usuarios.add(session);// recebe um Session (usuario), e o adiciona na lista.
	}

	public void send(Promo promo) {
		List<Session> sessions = usuarios.getUsuarios();

		for (Session session : sessions) {
			if (session.isOpen()) {
				try {
					session.getBasicRemote().sendText(promo.toJson());// envia a promoção para a lista de usuários
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// método chamado sempre que uma sessão de WebSocket está sendo fechada.
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		usuarios.remove(session);
		System.out.println(closeReason.getCloseCode()); // GOING_AWAY - qdo o usuario vai embora (fecha o navegador)
	}
}