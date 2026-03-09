package br.com.rhscdeveloper.bean;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MovimentoVeiculoBean extends BaseBean {

	private Integer id;
	private VeiculoBean veiculo;
	private Integer tipoMovimento;
	private LocalDateTime dtHrEntrada;
	private LocalDateTime dtHrSaida;
	private Integer situacao;
	private MovimentoFinanceiroBean movFinanceiro;
	
	public MovimentoVeiculoBean() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public VeiculoBean getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(VeiculoBean veiculo) {
		this.veiculo = veiculo;
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

	public Integer getSituacao() {
		return situacao;
	}

	public void setSituacao(Integer situacao) {
		this.situacao = situacao;
	}

	public MovimentoFinanceiroBean getMovFinanceiro() {
		return movFinanceiro;
	}

	public void setMovFinanceiro(MovimentoFinanceiroBean movFinanceiro) {
		this.movFinanceiro = movFinanceiro;
	}
}
