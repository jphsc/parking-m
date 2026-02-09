package br.com.rhscdeveloper.exception;

import org.hibernate.exception.ConstraintViolationException;

public class MyConstraintViolationException extends ConstraintViolationException {
	
	private static final long serialVersionUID = 1L;
	
	private String descricao;
	private Integer codErro;
	
	public MyConstraintViolationException(String msg) {
		super(msg, null, null);
		this.descricao = msg;
	}

	public MyConstraintViolationException(String msg, Integer codErro) {
		super(msg, null, null);
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
