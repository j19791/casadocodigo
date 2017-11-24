package br.com.casadocodigo.loja.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Cacheable // precisamos falar que as entidades serão cacheadas
@XmlRootElement // para disponibilizar um serviço rest que retorna xml, existe outra
				// especificação que trata da transformação para XML, a JAXB. Essa especificação
				// diz que um objeto precisa estar mapeado como XmlRootElement.
				// a implementação do JAX-RS do Wildfly default, que é a RESTEasy, então não
				// precisará fazer nada. Por si só o RESTEasey faz a transformação do livro em
				// uma lista de livros
@XmlAccessorType(XmlAccessType.FIELD) // informando que queremos o acesso através do campo (field), será acessado
										// diretamente o campo, como titulo, descricao:
public class Livro {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank // Bean validation: Valida já vazio e espeços em branco
	private String titulo;

	@Length(min = 10) // Bean validation: Número mínimo de caracteres que o campo pode ter
	@NotBlank
	@Lob // para informar que ele pode receber um grande valor de texto
	private String descricao;

	@DecimalMin("20") // Valor decimal mínimo
	private BigDecimal preco;

	@Min(50) // Valor inteiro mínimo
	private Integer numeroPaginas;

	@ManyToMany // cria uma tabela auxiliar
	@Size(min = 1) // número mínimo de elementos na lista
	@NotNull // A lista não pode ser nula
	@XmlElement(name = "autor")
	@XmlElementWrapper(name = "autores")
	private List<Autor> autores = new ArrayList<Autor>();// para evitar null

	@Temporal(TemporalType.DATE) // apenas a data sem hora
	// private Calendar dataPublicacao = Calendar.getInstance(); // tratar
	// PropertyNotFoundException com inicialização do
	// atributo - já será carregada com a data de hoje.
	private Calendar dataPublicacao; // utilizando o CalendarConverter- não há necessidade de inicialização

	private String capaPath;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Autor> getAutores() {
		return autores;
	}

	public void setAutores(List<Autor> autores) {
		this.autores = autores;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	@Override
	public String toString() {
		return "Livro [titulo=" + titulo + ", descricao=" + descricao + ", preco=" + preco + ", numeroPaginas="
				+ numeroPaginas + "]";
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public Integer getNumeroPaginas() {
		return numeroPaginas;
	}

	public void setNumeroPaginas(Integer numeroPaginas) {
		this.numeroPaginas = numeroPaginas;
	}

	public Calendar getDataPublicacao() {
		return dataPublicacao;
	}

	public void setDataPublicacao(Calendar dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	public String getCapaPath() {
		return capaPath;
	}

	public void setCapaPath(String capaPath) {
		this.capaPath = capaPath;
	}

	// precisamos criar hascode() pois o CarrinhoItem usa Livro e CarrinhoItem esta
	// sendo usado num set do CarrinhoCompra
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Livro other = (Livro) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
