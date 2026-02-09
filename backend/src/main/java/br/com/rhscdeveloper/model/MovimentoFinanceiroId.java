package br.com.rhscdeveloper.model;

import java.io.Serializable;
import java.util.Objects;

public class MovimentoFinanceiroId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private RegraFinanceiraVO idRegra;
	private MovimentoVeiculoVO idMovimento;

	public MovimentoFinanceiroId() {
		
	}
	
	public MovimentoFinanceiroId(RegraFinanceiraVO idRegra, MovimentoVeiculoVO idMovimento) {
		this.idRegra = idRegra;
		this.idMovimento = idMovimento;
	}

	public RegraFinanceiraVO getIdRegra() {
		return idRegra;
	}

	public void setIdRegra(RegraFinanceiraVO idRegra) {
		this.idRegra = idRegra;
	}

	public MovimentoVeiculoVO getIdMovimento() {
		return idMovimento;
	}

	public void setIdMovimento(MovimentoVeiculoVO idMovimento) {
		this.idMovimento = idMovimento;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idMovimento, idRegra);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MovimentoFinanceiroId other = (MovimentoFinanceiroId) obj;
		return Objects.equals(idMovimento, other.idMovimento) && Objects.equals(idRegra, other.idRegra);
	}
}
