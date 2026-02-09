package br.com.rhscdeveloper.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicUpdate;

import br.com.rhscdeveloper.enumerator.Enums.SituacaoMovimento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@DynamicUpdate
@Table(name = "tb_movimento_financeiro")
@IdClass(MovimentoFinanceiroId.class)
public class MovimentoFinanceiroVO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @ManyToOne
    @JoinColumn(name = "mvf_regra", foreignKey = @ForeignKey(name = "fk_movfinanceiro_regrafin_01"))
    private RegraFinanceiraVO idRegra;

	@Id
    @ManyToOne
    @JoinColumn(name = "mvf_movimento", foreignKey = @ForeignKey(name = "fk_movfinanceiro_movveiculo_01"))
    private MovimentoVeiculoVO idMovimento;
	
	@Column(name = "mvf_valor", nullable = true, precision = 2)
	private Double valor;
	
	@Column(name = "mvf_situacao", nullable = false)
	private Integer situacao;
	
	@Version
	@Column(name = "mvf_versao", nullable = false)
	private LocalDateTime versao;
	
	public MovimentoFinanceiroVO() {
		
	}

	public MovimentoFinanceiroVO(RegraFinanceiraVO regra, MovimentoVeiculoVO movimento, Double valor, SituacaoMovimento situacao, LocalDateTime versao) {
		this.idRegra = regra;
		this.idMovimento = movimento;
		this.valor = valor;
		this.situacao = situacao.getId();
		this.versao = versao;
	}

	public RegraFinanceiraVO getRegra() {
		return idRegra;
	}

	public void setRegra(RegraFinanceiraVO idRegra) {
		this.idRegra = idRegra;
	}

	public MovimentoVeiculoVO getMovimento() {
		return idMovimento;
	}

	public void setMovimento(MovimentoVeiculoVO idMovimento) {
		this.idMovimento = idMovimento;
	}

	public Double getValor() {
		return valor;
	}

	public void setSituacao(SituacaoMovimento situacao) {
		this.situacao = situacao.getId();
	}

	public Integer getSituacao() {
		return situacao;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public LocalDateTime getVersao() {
		return versao;
	}

	public void setVersao(LocalDateTime versao) {
		this.versao = versao;
	}

	@Override
	public String toString() {
		return "MovimentoFinanceiroVO [idRegra=" + idRegra + ", idMovimento=" + idMovimento + ", valor=" + valor
				+ ", situacao=" + situacao + ", versao=" + versao + "]";
	}
	
}
