package br.com.rhscdev.application.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MovimentoFinanceiroResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer idRegra;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer idMovimento;
	private Double valor;
	private Integer situacao;
	private LocalDateTime versao;
	
	public MovimentoFinanceiroResponse() {
		
	}

	public MovimentoFinanceiroResponse(Integer idRegra, Integer idMovimento, Double valor, Integer situacao, LocalDateTime versao) {
		this.idRegra = idRegra;
		this.idMovimento = idMovimento;
		this.valor = valor;
		this.situacao = situacao;
		this.versao = versao;
	}

	public Integer getIdRegra() {
		return idRegra;
	}

	public void setIdRegra(Integer idRegra) {
		this.idRegra = idRegra;
	}

	public Integer getIdMovimento() {
		return idMovimento;
	}

	public void setIdMovimento(Integer idMovimento) {
		this.idMovimento = idMovimento;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
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
}
