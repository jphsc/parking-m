package br.com.rhscdeveloper.bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MovimentoFinanceiroBean extends BaseBean {

	private Integer idRegra;

    private Integer idMovimento;
    private RegraFinanceiraBean regra;
    private MovimentoVeiculoBean movimento;
	private Double valor;
	private Integer situacao;
	
	public MovimentoFinanceiroBean() {
		
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

	public RegraFinanceiraBean getRegra() {
		return regra;
	}

	public void setRegra(RegraFinanceiraBean regra) {
		this.regra = regra;
	}

	public MovimentoVeiculoBean getMovimento() {
		return movimento;
	}

	public void setMovimento(MovimentoVeiculoBean movimento) {
		this.movimento = movimento;
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
}
