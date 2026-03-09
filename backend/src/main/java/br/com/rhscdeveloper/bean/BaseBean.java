package br.com.rhscdeveloper.bean;

import java.time.LocalDateTime;

public abstract class BaseBean {

	private LocalDateTime versao;

	public LocalDateTime getVersao() {
		return versao;
	}

	public void setVersao(LocalDateTime versao) {
		this.versao = versao;
	}
}
