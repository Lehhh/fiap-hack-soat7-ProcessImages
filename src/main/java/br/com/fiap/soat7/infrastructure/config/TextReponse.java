package br.com.fiap.soat7.infrastructure.config;

import lombok.Getter;

@Getter
public class TextReponse {

	public final static String SUCCESS = "Success: ";
	public final static String ERROR = "Erro : ";
	public final static String MESSAGE = "Message";
	public final static String UPLOAD_DISK_ERROR_EMPTY = ERROR + "Arquivo Vazio";
	public final static String UPLOAD_DISK_ERROR_FAIL_UPLOAD = ERROR + "Upload não realizado %s";
	public final static String SUCCESS_DISK_UPLOAD = SUCCESS + "Upload de arquivo concluído com sucesso: %s";
	public final static String UPLOAD_DISK_ERROR_FAIL_CREATE_DIRECTORY = ERROR + "Ao criar diretorio";

}
