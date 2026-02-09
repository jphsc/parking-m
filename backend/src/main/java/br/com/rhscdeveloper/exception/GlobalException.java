package br.com.rhscdeveloper.exception;

public class GlobalException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final Integer codErro;
	private final String mensagem;
	
	public GlobalException(String mensagem) {
		super(mensagem);
		this.codErro = 999;
		this.mensagem = mensagem;
	}

	public GlobalException(Integer codErro, String mensagem) {
		super(mensagem);
		this.codErro = codErro;
		this.mensagem = mensagem;
	}


    public Integer getCodigoErro() {
        return codErro;
    }

    public String getMensagem() {
        return mensagem;
    }
}