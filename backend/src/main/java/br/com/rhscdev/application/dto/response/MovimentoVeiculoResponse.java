package br.com.rhscdev.application.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.rhscdev.domain.entity.BaseIdentificavel;

public class MovimentoVeiculoResponse implements BaseIdentificavel, Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer idMovimento;
	private Integer idVeiculo;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Integer idRegra;
	private String placa;
	private Integer tipoMovimento;
	private LocalDateTime dtHrEntrada;
	private LocalDateTime dtHrSaida;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private MovimentoFinanceiroResponse movimentoFinanceiro;
	private Integer situacao;
	private LocalDateTime versao;
    
    public MovimentoVeiculoResponse() {
    	
    }

	public MovimentoVeiculoResponse(Integer id, Integer idVeiculo, Integer idRegra, String placa, Integer tipoMovimento, LocalDateTime dtHrEntrada, 
			LocalDateTime dtHrSaida, MovimentoFinanceiroResponse movimentoFinanceiro, Integer situacao, LocalDateTime versao) {
		this.idMovimento = id;
		this.idVeiculo = idVeiculo;
		this.idRegra = idRegra;
		this.placa = placa;
		this.tipoMovimento = tipoMovimento;
		this.dtHrEntrada = dtHrEntrada;
		this.dtHrSaida = dtHrSaida;
		this.movimentoFinanceiro = movimentoFinanceiro;
		this.situacao = situacao;
		this.versao = versao;
	}

	public Integer getIdMovimento() {
		return idMovimento;
	}

	public void setIdMovimento(Integer id) {
		this.idMovimento = id;
	}

	public Integer getIdVeiculo() {
		return idVeiculo;
	}

	public void setIdVeiculo(Integer idVeiculo) {
		this.idVeiculo = idVeiculo;
	}

	public Integer getIdRegra() {
		return idRegra;
	}

	public void setIdRegra(Integer idRegra) {
		this.idRegra = idRegra;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public Integer getTipoMovimento() {
		return tipoMovimento;
	}

	public void setTipoMovimento(Integer tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}

	public LocalDateTime getDtHrEntrada() {
		return dtHrEntrada;
	}

	public void setDtHrEntrada(LocalDateTime dtHrEntrada) {
		this.dtHrEntrada = dtHrEntrada;
	}

	public LocalDateTime getDtHrSaida() {
		return dtHrSaida;
	}

	public void setDtHrSaida(LocalDateTime dtHrSaida) {
		this.dtHrSaida = dtHrSaida;
	}
    
	public MovimentoFinanceiroResponse getMovimentoFinanceiro() {
		return movimentoFinanceiro;
	}

	public void setMovimentoFinanceiro(MovimentoFinanceiroResponse movimentoFinanceiro) {
		this.movimentoFinanceiro = movimentoFinanceiro;
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
