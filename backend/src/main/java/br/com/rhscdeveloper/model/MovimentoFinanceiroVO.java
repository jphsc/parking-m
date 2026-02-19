package br.com.rhscdeveloper.model;

import java.io.Serializable;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@DynamicUpdate
@Table(name = "tb_movimento_financeiro")
@IdClass(MovimentoFinanceiroVOId.class)
@AttributeOverride(name = "versao", column = @Column(name = "mvf_versao", nullable = false))
public class MovimentoFinanceiroVO extends BaseVO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "mvf_regra")
    private Integer idRegra;

	@Id
    @Column(name = "mvf_movimento")
    private Integer idMovimento;

	@ManyToOne
	@MapsId("idRegra")
    @JoinColumn(name = "mvf_regra", foreignKey = @ForeignKey(name = "fk_movfinanceiro_regrafin_01"))
    private RegraFinanceiraVO regra;

	@ManyToOne
	@MapsId("idMovimento")
    @JoinColumn(name = "mvf_movimento", foreignKey = @ForeignKey(name = "fk_movfinanceiro_movveiculo_01"))
    private MovimentoVeiculoVO movimento;
	
	@Column(name = "mvf_valor", nullable = true, precision = 2)
	private Double valor;
	
	@Column(name = "mvf_situacao", nullable = false)
	private Integer situacao;

	public MovimentoFinanceiroVO() {
		
	}

	public MovimentoFinanceiroVO(RegraFinanceiraVO regra, MovimentoVeiculoVO movimento, Double valor, Integer situacao) {
		this.setRegra(regra);
		this.setMovimento(movimento);
		this.valor = valor;
		this.situacao = situacao;
	}
	
	public Integer getIdRegra() {
		return this.idRegra;
	}
	
	public RegraFinanceiraVO getRegra() {
		return regra;
	}

	public void setRegra(RegraFinanceiraVO regra) {
        this.regra = regra;
        this.idRegra = (regra != null) ? regra.getId() : null;
    }
	
	public Integer getIdMovimento() {
		return this.idMovimento;
	}
	
	public MovimentoVeiculoVO getMovimento() {
		return movimento;
	}

	public void setMovimento(MovimentoVeiculoVO movimento) {
		if(this.movimento == movimento) return;
		
        this.movimento = movimento;
        this.idMovimento = (movimento != null) ? movimento.getId() : null;
        
        if(movimento != null) {
        	movimento.vincularFinanceiro(this);
        }
    }

	public Double getValor() {
		return valor;
	}

	public void setSituacao(Integer situacao) {
		this.situacao = situacao;
	}

	public Integer getSituacao() {
		return situacao;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "MovimentoFinanceiroVO [idRegra=" + idRegra + ", idMovimento=" + idMovimento + ", valor=" + valor
				+ ", situacao=" + situacao + ", versao=" + versao + "]";
	}
	
	@Override
    public MovimentoFinanceiroVOId getId() {
        if (idRegra == null || idMovimento == null) {
            return null;
        }
        return new MovimentoFinanceiroVOId(idRegra, idMovimento);
    }
	
}
