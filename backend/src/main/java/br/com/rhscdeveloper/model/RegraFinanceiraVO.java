package br.com.rhscdeveloper.model;

import java.time.LocalDate;
import java.util.Objects;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@DynamicUpdate
@Table(name = "tb_regra_financeira")
@AttributeOverride(name = "versao", column = @Column(name = "ref_versao", nullable = false))
public class RegraFinanceiraVO extends BaseVO implements Comparable<RegraFinanceiraVO> {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ref_id")
	private Integer id;
	
	@Column(name = "ref_descricao", nullable = false)
	private String descricao;
	
	@Column(name = "ref_valor", nullable = false, precision = 2)
	private Double valor;
	
	@Column(name = "ref_metodo_pag", nullable = false)
	private Integer tipoCobranca;
	
	@Column(name = "ref_tipo_movimento", nullable = false)
	private Integer tipoMovimento;
	
	@Column(name = "ref_ini_validade", nullable = false)
	private LocalDate dtInicioValidade;
	
	@Column(name = "ref_fim_validade", nullable = true)
	private LocalDate dtFimValidade;
	
	@Column(name = "ref_situacao", nullable = false)
	private Integer situacao;
	
	public RegraFinanceiraVO() {
		
	}

	protected RegraFinanceiraVO(Integer id, String descricao, Double valor, Integer tipoCobranca, Integer tipoMovimento,
			LocalDate dtInicioValidade, LocalDate dtFimValidade, Integer situacao) {
		this.id = id;
		this.descricao = descricao;
		this.valor = valor;
		this.tipoCobranca = tipoCobranca;
		this.tipoMovimento = tipoMovimento;
		this.dtInicioValidade = dtInicioValidade;
		this.dtFimValidade = dtFimValidade;
		this.situacao = situacao;
	}

	protected RegraFinanceiraVO(String descricao, Double valor, Integer tipoCobranca, Integer tipoMovimento,
			LocalDate dtInicioValidade, LocalDate dtFimValidade, Integer situacao) {
		this.descricao = descricao;
		this.valor = valor;
		this.tipoCobranca = tipoCobranca;
		this.tipoMovimento = tipoMovimento;
		this.dtInicioValidade = dtInicioValidade;
		this.dtFimValidade = dtFimValidade;
		this.situacao = situacao;
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

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegraFinanceiraVO other = (RegraFinanceiraVO) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "RegraFinanceiraVO [id=" + id + ", descricao=" + descricao + ", valor=" + valor + ", tipoCobranca="
				+ tipoCobranca + ", tipoMovimento=" + tipoMovimento + ", dtInicioValidade=" + dtInicioValidade
				+ ", dtFimValidade=" + dtFimValidade + ", situacao=" + situacao + ", versao=" + versao + "]";
	}

	@Override
	public int compareTo(RegraFinanceiraVO o) {
		return this.getId().compareTo(o.getId());
	}

	public static RegraFinanceiraVO criar(String descricao, Double valor, Integer tipoCobranca, Integer tipoMovimento,
			LocalDate dtInicioValidade, LocalDate dtFimValidade, Integer situacao) {
		
		return new RegraFinanceiraVO(descricao, valor, tipoCobranca, tipoMovimento, dtInicioValidade, dtFimValidade, situacao);
	}
	
	public void atualizarRegraFinanceira(Integer id, String descricao, Double valor, Integer tipoCobranca, Integer tipoMovimento,
			LocalDate dtInicioValidade, LocalDate dtFimValidade, Integer situacao) {
		if(id != null) this.id = id;
		if(descricao != null) this.descricao = descricao;
		if(valor != null) this.valor = valor;
		if(tipoCobranca != null) this.tipoCobranca = tipoCobranca;
		if(tipoMovimento != null) this.tipoMovimento = tipoMovimento;
		if(dtInicioValidade != null) this.dtInicioValidade = dtInicioValidade;
		if(dtFimValidade != null) this.dtFimValidade = dtFimValidade;
		if(situacao != null) this.situacao = situacao;
	}
}
