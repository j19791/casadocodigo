package br.com.casadocodigo.servlets;

import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.casadocodigo.loja.FileSaver;

@WebServlet("/file/*")
public class FileServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		// conseguimos pegar o que vier pelo *. O método retorna toda a URL, mas só nos
		// interessa o que vier depois de /file, então faremos um split("/file") e do
		// Split teremos dois lados, valor [0] será o que vem antes do /file e [1] o que
		// vem depois.
		String path = req.getRequestURI().split("/file")[1];

		System.out.println(path);

		// P/ q o arquivo seja enviado no response do Servlet, precisaremos informar
		// o contentType do arquivo. Usaremos a API para pegar o contentType direto do
		// arquivo
		// Paths.get("caminho completo do arquivo"). No entanto, temos que acessar de
		// fato o arquivo. Podemos juntar o path que temos com o caminho fixo do nosso
		// FileSaver p/
		// conseguirmos o caminho completo do arquivo. Passaremos as duas informações
		// para o Paths e assim obtemos um source como sendo a fonte do nosso arquivo.

		Path source = Paths.get(FileSaver.SERVER_PATH + "/" + path);

		FileNameMap fileNameMap = URLConnection.getFileNameMap();

		// O source servirá como fonte para o FileNameMap conseguir chegar no arquivo e
		// obter o contentType
		String contentType = fileNameMap.getContentTypeFor("file:" + source);

		res.reset();// Usando JSF ainda temos que limpar o response antes de setar qualquer
					// cabeçalho

		// é muito importante dizer ao navegador qual o tipo de conteúdo que estamos
		// enviando para ele, e ele se ajustará a esse tipo de conteúdo. Todo navegador
		// verifica sempre o Header da resposta do servidor para saber o que ele deve
		// fazer.
		res.setContentType(contentType);// setar o contentType no nosso response

		// importante informar p/ o navegador, é o tamanho do arquivo
		res.setHeader("Content-Length", String.valueOf(Files.size(source)));

		// coloca o nome correto do arquivo que estamos baixando
		res.setHeader("Content-Disposition", "filename=\"" + source.getFileName().toString() + "\"");

		FileSaver.transfer(source, res.getOutputStream());//// metodo p/ transferir o arquivo do servidor para o
															//// response
	}

}
