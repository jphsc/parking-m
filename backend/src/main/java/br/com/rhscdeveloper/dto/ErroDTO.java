package br.com.rhscdeveloper.dto;

public class ErroDTO {

	private Integer codigo;
	private String mensagem;
	
	public ErroDTO() {
		
	}

	public ErroDTO(Integer codigoErrro, String mensagem) {
		this.codigo = codigoErrro;
		this.mensagem = mensagem;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigoErrro) {
		this.codigo = codigoErrro;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
}
