package br.com.rhscdeveloper.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MovimentoVeiculoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer idMovimento;
	private Integer idVeiculo;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Integer idRegra;
	private String placa;
	private Integer tipoMovimento;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm[:ss][.SSS]")
	private LocalDateTime dtHrEntrada;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm[:ss][.SSS]")
	private LocalDateTime dtHrSaida;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private MovimentoFinanceiroDTO movimentoFinanceiro;
	private Integer situacao;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private LocalDateTime versao;
    
    public MovimentoVeiculoDTO() {
    	
    }

	public MovimentoVeiculoDTO(Integer id, Integer idVeiculo, Integer idRegra, String placa, Integer tipoMovimento, LocalDateTime dtHrEntrada, 
			LocalDateTime dtHrSaida, MovimentoFinanceiroDTO movimentoFinanceiro, Integer situacao, LocalDateTime versao) {
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
	
	public MovimentoVeiculoDTO(Builder b) {
		this.idMovimento = b.id;
		this.idVeiculo = b.idVeiculo;
		this.idRegra = b.idRegra;
		this.placa = b.placa;
		this.tipoMovimento = b.tipoMovimento;
		this.dtHrEntrada = b.dtHrEntrada;
		this.dtHrSaida = b.dtHrSaida;
		this.movimentoFinanceiro = b.movimentoFinanceiro;
		this.situacao = b.situacao;
		this.versao = b.versao;
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
    
	public MovimentoFinanceiroDTO getMovimentoFinanceiro() {
		return movimentoFinanceiro;
	}

	public void setMovimentoFinanceiro(MovimentoFinanceiroDTO movimentoFinanceiro) {
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

	public static class Builder {
		
		private Integer id;
		private Integer idVeiculo;
		private Integer idRegra;
		private String placa;
		private Integer tipoMovimento;
		private LocalDateTime dtHrEntrada;
		private LocalDateTime dtHrSaida;
		private MovimentoFinanceiroDTO movimentoFinanceiro;
	    private Integer situacao;
	    private LocalDateTime versao;
		
		public MovimentoVeiculoDTO build() {
			return new MovimentoVeiculoDTO(this);
		}

		public Builder setId(Integer id) {
			this.id = id;
			return this;
		}

		public Builder setIdVeiculo(Integer idVeiculo) {
			this.idVeiculo = idVeiculo;
			return this;
		}

		public Builder setIdRegra(Integer idRegra) {
			this.idRegra = idRegra;
			return this;
		}

		public Builder setPlaca(String placa) {
			this.placa = placa;
			return this;
		}

		public Builder setTipoMovimento(Integer tipoMovimento) {
			this.tipoMovimento = tipoMovimento;
			return this;
		}

		public Builder setDtHrEntrada(LocalDateTime dtHrEntrada) {
			this.dtHrEntrada = dtHrEntrada;
			return this;
		}

		public Builder setDtHrSaida(LocalDateTime dtHrSaida) {
			this.dtHrSaida = dtHrSaida;
			return this;
		}

		public Builder setMovimentoFinanceiro(MovimentoFinanceiroDTO movimento) {
			this.movimentoFinanceiro = movimento;
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
