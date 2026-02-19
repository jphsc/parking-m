package br.com.rhscdeveloper.model;

import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@DynamicUpdate
@Table(name = "tb_movimento_veiculo")
@AttributeOverride(name = "versao", column = @Column(name = "mvv_versao", nullable = false))
public class MovimentoVeiculoVO extends BaseVO {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mvv_id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mvv_veiculo", nullable = false, updatable = false, referencedColumnName = "vei_id", 
			foreignKey = @ForeignKey(name = "fk_movimentoveiculo_veiculo_01"))
	private VeiculoVO veiculo;
	
	@Column(name = "mvv_tipo_movimento", nullable = false, updatable = false)
	private Integer tipoMovimento;
	
	@Column(name = "mvv_dt_hr_entrada", nullable = false, updatable = false)
	private LocalDateTime dtHrEntrada;

	@Column(name = "mvv_dt_hr_saida", nullable = true)
	private LocalDateTime dtHrSaida;
	
	@Column(name = "mvv_situacao", nullable = false)
	private Integer situacao;

	@OneToOne(mappedBy = "movimento", fetch = FetchType.LAZY, optional = true, cascade = CascadeType.ALL)
	private MovimentoFinanceiroVO movFinanceiro;

	public MovimentoVeiculoVO() {
		
	}
	
	public MovimentoVeiculoVO(VeiculoVO veiculoVO, Integer tipoMovimento, LocalDateTime dtHrEntrada, LocalDateTime dtHrSaida,
			Integer situacao) {
		this.veiculo = veiculoVO;
		this.tipoMovimento = tipoMovimento;
		this.dtHrEntrada = dtHrEntrada;
		this.dtHrSaida = dtHrSaida;
		this.situacao = situacao;
	}

	public MovimentoVeiculoVO(Builder b) {
		this.id = b.id;
		this.veiculo = b.veiculoVO;
		this.tipoMovimento = b.tipoMovimento;
		this.dtHrEntrada = b.dtHrEntrada;
		this.dtHrSaida = b.dtHrSaida;
		this.situacao = b.situacao;
		this.versao = b.versao;
	}
	
	public MovimentoVeiculoVO(MovimentoVeiculoVO mv) {
		this.veiculo = mv.veiculo;
		this.tipoMovimento = mv.tipoMovimento;
		this.dtHrEntrada = mv.dtHrEntrada;
		this.dtHrSaida = mv.dtHrSaida;
		this.situacao = mv.situacao;
		this.versao = mv.versao;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public VeiculoVO getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(VeiculoVO veiculoVO) {
		this.veiculo = veiculoVO;
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
	
	public MovimentoFinanceiroVO getMovFinanceiro() {
		return movFinanceiro;
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
		MovimentoVeiculoVO other = (MovimentoVeiculoVO) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "MovimentoVeiculoVO [id=" + id + ", veiculo=" + veiculo + ", tipoMovimento=" + tipoMovimento
				+ ", dtHrEntrada=" + dtHrEntrada + ", dtHrSaida=" + dtHrSaida + ", situacao=" + situacao + ", versao="
				+ versao + ", movFinanceiro=" + movFinanceiro + "]";
	}
	
	public void vincularFinanceiro(MovimentoFinanceiroVO financeiro) {
		if(this.movFinanceiro == financeiro) return;
		
	    this.movFinanceiro = financeiro;
	    
	    if (financeiro != null) {
	        financeiro.setMovimento(this);
	    }
	}

	public static class Builder {
		private Integer id;
		private VeiculoVO veiculoVO;
		private Integer tipoMovimento;
		private LocalDateTime dtHrEntrada;
		private LocalDateTime dtHrSaida;
		private Integer situacao;
		private LocalDateTime versao;
		
		public MovimentoVeiculoVO build() {
			return new MovimentoVeiculoVO(this);
		}
		
		public Builder setId(Integer id) {
			this.id = id;
			return this;
		}
		
		public Builder setVeiculoVO(VeiculoVO veiculoVO) {
			this.veiculoVO = veiculoVO;
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
