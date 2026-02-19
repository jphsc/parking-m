package br.com.rhscdeveloper.model;

import java.io.Serializable;
import java.util.Objects;

public class MovimentoFinanceiroVOId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer idRegra;
	private Integer idMovimento;

	public MovimentoFinanceiroVOId() {
		
	}
	
	public MovimentoFinanceiroVOId(Integer idRegra, Integer idMovimento) {
		this.idRegra = idRegra;
		this.idMovimento = idMovimento;
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

	@Override
	public int hashCode() {
		return Objects.hash(idMovimento, idRegra);
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovimentoFinanceiroVOId that = (MovimentoFinanceiroVOId) o;
        return Objects.equals(idRegra, that.idRegra) && 
               Objects.equals(idMovimento, that.idMovimento);
	}
}
