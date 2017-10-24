package br.com.casadocodigo.loja.beans;

import java.io.IOException;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import javax.transaction.Transactional;

import br.com.casadocodigo.loja.FileSaver;
import br.com.casadocodigo.loja.daos.AutorDao;
import br.com.casadocodigo.loja.daos.LivroDao;
import br.com.casadocodigo.loja.models.Autor;
import br.com.casadocodigo.loja.models.Livro;

//backbean
@Named // Para que o JSF (form.xhmtl) veja nosso Bean, temos que utilizar o CDI.
@RequestScoped // ao preencher os dados do form e clicar em Cadastrar, dessa vez os dados
				// sumiram. Bean agora está sendo gerenciado pelo CDI, e o CDI por default
				// possui um tempo de vida muito curto, no caso, sempre que o JSF precisa do
				// Bean, ele cria uma nova instância e entrega ao JSF. Porém, para que os dados
				// fiquem vivos durante toda a requisição do usuário, precisamos que o CDI deixe
				// ele vivo durante todo o Request,
public class AdminLivrosBean {
	private Livro livro = new Livro(); // para contornar o problema identifier 'adminLivrosBean' resolved to null

	@Inject
	private LivroDao dao;

	// nao é mais necessário autoresId. É recebido os autores diretamente
	/* private List<Integer> autoresId = new ArrayList<>(); */// lista para armazenar os id q serão enviados do
																// formulário.

	@Inject
	private FacesContext context; // o FacesContext é injetado indiretamente, utilizando um producer
	// injeção de dependência. Não precisamos mais fazer atribuições e também não
	// precisaremos de construtores.

	/*
	 * public List<Integer> getAutoresId() { return autoresId; }
	 */

	/*
	 * public void setAutoresId(List<Integer> autoresId) { this.autoresId =
	 * autoresId; }
	 */

	@Inject
	private AutorDao autorDao;
	// fazemos new para evitar NullPointerException

	private Part capaLivro;// Part: JEE7 - objeto que já tem a capacidade de salvar um arquivo dentro dele:

	public Livro getLivro() {
		return livro;
	}

	public void setLivro(Livro livro) {
		this.livro = livro;
	}

	@Transactional // tudo que altera o estado do banco, o servidor espera uma transação (begin,
					// commit). Precisamos pedir essa transação para o JTA
	public String salvar() throws IOException {
		/*
		 * for (Integer autorId : autoresId) { livro.getAutores().add(new
		 * Autor(autorId)); }
		 */
		// utilizando o converter AutorConverter - vem do form os objetos autores

		dao.salvar(livro);

		FileSaver fileSaver = new FileSaver();
		livro.setCapaPath(fileSaver.write(capaLivro, "livros"));

		// tranferido para FileSaver
		// getCapaLivro().write("/casadocodigo/livros/" +
		// getCapaLivro().getSubmittedFileName());// escreve o arquivo no
		// caminho
		// passado com o nome do arquivo.

		System.out.println("Livro salvo com Sucesso!: " + this.livro);

		// toda mensagem do JSF é adicionada ao Request atual. Como estamos fazendo um
		// faces-redirect=true, um novo Request é gerado e com isso perdemos a mensagem.
		// precisaremos mudar o tipo de mensagem, fazer c/ q ela dure mais de
		// um request e com isso, conseguiremos passar a mensagem da tela form.xhtml
		// O Flash Scope começa em um request e termina no request seguinte.
		// FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		// mensagem enviada para o xhtml
		// FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Livro
		// cadastrado com sucesso!"));

		// refatorando o código - utilizado CDI com context

		context.getExternalContext().getFlash().setKeepMessages(true);
		context.addMessage(null, new FacesMessage("Livro cadastrado com sucesso!"));

		// limpar os dados do formulario depois de salvar
		this.livro = new Livro();
		// this.autoresId = new ArrayList<>();

		// return "/livros/lista"; //redirecione o usuário para lista de livros.
		// Atualiza lista e o navegador nos questiona se queremos resubmeter o
		// formulário, duplicando os cadastros
		return "/livros/lista?faces-redirect=true"; // O redirect passa um http status (302) para o navegador carregar
													// uma outra página e esquecer dos dados da requisição anterior.
	}

	public List<Autor> getAutores() {
		// return Arrays.asList(new Autor(1, "Paulo Silveira"), new Autor(2, "Sérgio
		// Lopes"));
		return autorDao.listar();
	}

	@Override
	public String toString() {
		return "AdminLivrosBean [livro=" + livro + ", dao=" + dao + ", autoresId=" + "]";
	}

	public Part getCapaLivro() {
		return capaLivro;
	}

	public void setCapaLivro(Part capaLivro) {
		this.capaLivro = capaLivro;
	}

}
