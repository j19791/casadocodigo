package br.com.casadocodigo.loja.models;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

@Entity
public class Compra {

	private String uuid; // resolver problema de segurança podemos utilizar outro tipo de identificador,
							// o uuid (Universally Unique Identifier):

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(cascade = CascadeType.PERSIST) // Uma conta possui apenas um usuário, mas um usuário pode possuir mais de
												// uma conta. Cascade Persist: toda vez que quisermos salvar uma compra,
												// o usuário seja salvo juntamente.
	private Usuario usuario;

	private String itens;// <CarrinhoItem> não é uma entidade e só representa o item que está no carrinho
							// antes da compra, não é um item vendido para o usuário. caso o valor do item
							// mude, compras passadas não serão afetadas.
	// XML, JSON: Estes formatos texto são os mais comuns para transportar dados de
	// um sistema para outro!

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getItens() {
		return itens;
	}

	public void setItens(String itens) {
		this.itens = itens;
	}

	public String getUuid() {
		// TODO Auto-generated method stub
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@PrePersist // informar para o Entity Manager que, antes de persistir, é necessário chamar o
				// método
	public void createUUID() {
		this.uuid = UUID.randomUUID().toString();// será gerado o UUID de maneira aleatória
	}
}
