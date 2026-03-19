package br.com.rhscdev.domain.exception;

public class DomainException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final Integer codigoErro;
	
	public DomainException(String mensagem) {
		super(mensagem);
		this.codigoErro = 999;
	}
	
	public DomainException(Integer codigoErro, String mensagem) {
		super(mensagem);
		this.codigoErro = codigoErro;
	}

	public Integer getCodigoErro() {
		return codigoErro;
	}
	
}
