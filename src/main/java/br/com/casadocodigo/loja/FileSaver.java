package br.com.casadocodigo.loja;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;

import javax.servlet.http.Part;

public class FileSaver {
	public static final String SERVER_PATH = "C:/casadocodigo";

	public String write(Part arquivo, String path) {
		String relativePath = path + "/" + arquivo.getSubmittedFileName();
		try {
			arquivo.write(SERVER_PATH + "/" + relativePath);

			return relativePath;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// metodo p/ transferir o arquivo do servidor para o response
	public static void transfer(Path source, OutputStream outputStream) {
		try {
			// realizar a entrada do arquivo, do servidor para o sistema.
			FileInputStream input = new FileInputStream(source.toFile());

			try (// try-with-resources: automaticamente já fecha um recurso (no caso,
					// ReadableByteChannel e WritableByteChannel) após o fim do try

					// abre um canal direto com o arquivo
					ReadableByteChannel inputChannel = Channels.newChannel(input);

					// precisaremos de um canal de saída.
					WritableByteChannel outputChannel = Channels.newChannel(outputStream)) {

				// a transferência de um lado para o outro deve ser feita sempre usando um
				// Buffer (transferidos em pedaços). No caso, vamos transferir 10kb por vez
				ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 10);

				// ler do nosso canal de entrada e transferir para o buffer. Faremos isto,
				// enquanto existirem bytes para serem lidos. -1 ñ há mais bytes p/ serem lidos
				while (inputChannel.read(buffer) != -1) {
					buffer.flip();// Todo buffer possui um ponteiro que após temos escrito no buffer pode ter
									// ficado no final do mesmo, e para garantir que escreveremos no outputStream
									// tudo que está no buffer, precisamos chamar o método buffer.flip que colocará
									// o ponteiro na posição zero novamente.
					outputChannel.write(buffer);// o canal escreve na saída o conteúdo do buffer
					buffer.clear();// o buffer pode voltar a ler mais informações do arquivo.
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
