package br.com.rhscdeveloper.model;

import static java.util.Objects.nonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.DynamicUpdate;

import br.com.rhscdeveloper.dto.RegraFinanceiraDTO;
import br.com.rhscdeveloper.enumerator.Enums.Situacao;
import br.com.rhscdeveloper.enumerator.Enums.TipoCobranca;
import br.com.rhscdeveloper.enumerator.Enums.TipoMovimento;
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
	
	@Column(name = "ref_valor", nullable = false, precision = 2, updatable = false)
	private Double valor;
	
	@Column(name = "ref_metodo_pag", nullable = false, updatable = false)
	private Integer tipoCobranca;
	
	@Column(name = "ref_tipo_movimento", nullable = false, updatable = false)
	private Integer tipoMovimento;
	
	@Column(name = "ref_ini_validade", nullable = false, updatable = false)
	private LocalDate dtInicioValidade;
	
	@Column(name = "ref_fim_validade", nullable = true)
	private LocalDate dtFimValidade;
	
	@Column(name = "ref_situacao", nullable = false)
	private Integer situacao;
	
	public RegraFinanceiraVO() {
		
	}

	public RegraFinanceiraVO(String descricao, Double valor, TipoCobranca tipoCobranca, TipoMovimento tipoMovimento,
			LocalDate dtInicioValidade, LocalDate dtFimValidade, Situacao situacao) {
		this.descricao = descricao;
		this.valor = valor;
		this.tipoCobranca = tipoCobranca.getId();
		this.tipoMovimento = tipoMovimento.getId();
		this.dtInicioValidade = dtInicioValidade;
		this.dtFimValidade = dtFimValidade;
		this.situacao = situacao.getId();
	}

	public RegraFinanceiraVO(Integer id, String descricao, Double valor, TipoCobranca tipoCobranca, TipoMovimento tipoMovimento,
			LocalDate dtInicioValidade, LocalDate dtFimValidade, Situacao situacao) {
		this.id = id;
		this.descricao = descricao;
		this.valor = valor;
		this.tipoCobranca = tipoCobranca.getId();
		this.tipoMovimento = tipoMovimento.getId();
		this.dtInicioValidade = dtInicioValidade;
		this.dtFimValidade = dtFimValidade;
		this.situacao = situacao.getId();
	}

	public RegraFinanceiraVO(Builder b) {
		this.id = b.id;
		this.descricao = b.descricao;
		this.valor = b.valor;
		this.tipoCobranca = b.tipoCobranca;
		this.tipoMovimento = b.tipoMovimento;
		this.dtInicioValidade = b.dtInicioValidade;
		this.dtFimValidade = b.dtFimValidade;
		this.situacao = b.situacao;
		this.versao = b.versao;
	}
	
	public RegraFinanceiraVO(RegraFinanceiraVO rf) {
		this.id = rf.id;
		this.descricao = rf.descricao;
		this.valor = rf.valor;
		this.tipoCobranca = rf.tipoCobranca;
		this.tipoMovimento = rf.tipoMovimento;
		this.dtInicioValidade = rf.dtInicioValidade;
		this.dtFimValidade = rf.dtFimValidade;
		this.situacao = rf.situacao;
		this.versao = rf.versao;
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
	
	public static class Builder {
		private Integer id;
		private String descricao;
		private Double valor;
		private Integer tipoCobranca;
		private Integer tipoMovimento;
		private LocalDate dtInicioValidade;
		private LocalDate dtFimValidade;
		private Integer situacao;
		private LocalDateTime versao;
		
		public RegraFinanceiraVO build() {
			return new RegraFinanceiraVO(this);
		}

		public Builder setId(Integer id) {
			this.id = id;
			return this;
		}

		public Builder setDescricao(String descricao) {
			this.descricao = descricao;
			return this;
		}

		public Builder setValor(Double valor) {
			this.valor = valor;
			return this;
		}

		public Builder setTipoCobranca(Integer tipoCobranca) {
			this.tipoCobranca = tipoCobranca;
			return this;
		}

		public Builder setTipoMovimento(Integer tipoMovimento) {
			this.tipoMovimento = tipoMovimento;
			return this;
		}

		public Builder setDtInicioValidade(LocalDate dtInicioValidade) {
			this.dtInicioValidade = dtInicioValidade;
			return this;
		}

		public Builder setDtFimValidade(LocalDate dtFimValidade) {
			this.dtFimValidade = dtFimValidade;
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

	public static RegraFinanceiraVO dtoToVo(RegraFinanceiraVO voPersistente, RegraFinanceiraDTO dto) {
		voPersistente.id = nonNull(dto.getId()) ? dto.getId() : voPersistente.getId();;
		voPersistente.descricao = nonNull(dto.getDescricao()) ? dto.getDescricao() : voPersistente.getDescricao();
		voPersistente.valor = nonNull(dto.getValor()) ? dto.getValor() : voPersistente.getValor();
		voPersistente.tipoCobranca = nonNull(dto.getTipoCobranca()) ? dto.getTipoCobranca() : voPersistente.getTipoCobranca();
		voPersistente.tipoMovimento = nonNull(dto.getTipoMovimento()) ? dto.getTipoMovimento() : voPersistente.getTipoMovimento();
		voPersistente.dtInicioValidade = nonNull(dto.getDtInicioValidade()) ? dto.getDtInicioValidade() : voPersistente.getDtInicioValidade();
		voPersistente.dtFimValidade = nonNull(dto.getDtFimValidade()) ? dto.getDtFimValidade() : voPersistente.getDtFimValidade();
		voPersistente.situacao = nonNull(dto.getSituacao()) ? dto.getSituacao() : voPersistente.getSituacao();
		
		return voPersistente;
	}
}
