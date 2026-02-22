package br.com.rhscdeveloper.exception;

public class RegistroNaoEncontradoException extends NullPointerException {
	
	private static final long serialVersionUID = 1L;
	
	private String descricao;
	private Integer codErro;
	
	public RegistroNaoEncontradoException(String msg) {
		super(msg);
		this.descricao = msg;
	}

	public RegistroNaoEncontradoException(String msg, Integer codErro) {
		super(msg);
		this.descricao = msg;
		this.codErro = codErro;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getCodErro() {
		return codErro;
	}

	public void setCodErro(Integer codErro) {
		this.codErro = codErro;
	}	
}
