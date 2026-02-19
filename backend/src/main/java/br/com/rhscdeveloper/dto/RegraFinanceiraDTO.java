package br.com.rhscdeveloper.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class RegraFinanceiraDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String descricao;
	private Double valor;
	private Integer tipoCobranca;
	private Integer tipoMovimento;
	private LocalDate dtInicioValidade;
	private LocalDate dtFimValidade;
	private Integer situacao;
	private LocalDateTime versao;
	
	public RegraFinanceiraDTO() {
		
	}

	public RegraFinanceiraDTO(Integer id, String descricao, Double valor, Integer tipoCobranca, Integer tipoMovimento,
			LocalDate dtInicioValidade, LocalDate dtFimValidade, Integer situacao, LocalDateTime versao) {
		this.id = id;
		this.descricao = descricao;
		this.valor = valor;
		this.tipoCobranca = tipoCobranca;
		this.tipoMovimento = tipoMovimento;
		this.dtInicioValidade = dtInicioValidade;
		this.dtFimValidade = dtFimValidade;
		this.situacao = situacao;
		this.versao = versao;
	}
	
	public RegraFinanceiraDTO(Builder build) {
		this.id = build.id;
		this.descricao = build.descricao;
		this.valor = build.valor;
		this.tipoCobranca = build.tipoCobranca;
		this.tipoMovimento = build.tipoMovimento;
		this.dtInicioValidade = build.dtInicioValidade;
		this.dtFimValidade = build.dtFimValidade;
		this.situacao = build.situacao;
		this.versao = build.versao;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Integer getTipoCobranca() {
		return tipoCobranca;
	}

	public void setTipoCobranca(Integer tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
	}

	public Integer getTipoMovimento() {
		return tipoMovimento;
	}

	public void setTipoMovimento(Integer tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}

	public LocalDate getDtInicioValidade() {
		return dtInicioValidade;
	}

	public void setDtInicioValidade(LocalDate dtInicioValidade) {
		this.dtInicioValidade = dtInicioValidade;
	}

	public LocalDate getDtFimValidade() {
		return dtFimValidade;
	}

	public void setDtFimValidade(LocalDate dtFimValidade) {
		this.dtFimValidade = dtFimValidade;
	}

	public Integer getSituacao() {
		return situacao;
	}

	public void setSituacao(Integer situacao) {
		this.situacao = situacao;
	}

	public LocalDateTime getVersao() {
		return versao;
	}

	public void setVersao(LocalDateTime versao) {
		this.versao = versao;
	}
	
	public static class Builder {

		private Integer id;
		private String descricao;
		private Double valor;
		private Integer tipoCobranca;
		private Integer tipoMovimento;
		private LocalDate dtInicioValidade;
		private LocalDate dtFimValidade;
		private Integer situacao;
		private LocalDateTime versao;
		
		public RegraFinanceiraDTO build() {
			return new RegraFinanceiraDTO(this);
		}
		
		public Builder setId(Integer id) {
			this.id = id;
			return this;
		}
		
		public Builder setDescricao(String descricao) {
			this.descricao = descricao;
			return this;
		}
		
		public Builder setValor(Double valor) {
			this.valor = valor;
			return this;
		}
		
		public Builder setTipoCobranca(Integer tipoCobranca) {
			this.tipoCobranca = tipoCobranca;
			return this;
		}
		
		public Builder setTipoMovimento(Integer tipoMovimento) {
			this.tipoMovimento = tipoMovimento;
			return this;
		}
		
		public Builder setDtInicioValidade(LocalDate dtInicioValidade) {
			this.dtInicioValidade = dtInicioValidade;
			return this;
		}
		
		public Builder setDtFimValidade(LocalDate dtFimValidade) {
			this.dtFimValidade = dtFimValidade;
			return this;
		}
		
		public Builder setSituacao(Integer situacao) {
			this.situacao = situacao;
			return this;
		}
		
		public Builder setVersao(LocalDateTime versao) {
			this.versao = versao;
			return this;
		}
	}
}
