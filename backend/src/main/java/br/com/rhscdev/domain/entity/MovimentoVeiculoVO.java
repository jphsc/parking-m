package br.com.rhscdev.domain.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.DynamicUpdate;

import br.com.rhscdev.domain.enumerator.Enums.SituacaoMovimento;
import br.com.rhscdev.domain.exception.DomainException;
import br.com.rhscdev.infrastructure.config.Constantes;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@DynamicUpdate
@Table(name = "tb_movimento_veiculo")
@AttributeOverride(name = "versao", column = @Column(name = "mvv_versao", nullable = false))
public class MovimentoVeiculoVO extends BaseVO {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mov_veiculo_seq")
	@SequenceGenerator(name = "mov_veiculo_seq", sequenceName = "tb_movimento_veiculo_mvv_id_seq", allocationSize = 1)
	@Column(name = "mvv_id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mvv_veiculo", nullable = false, updatable = false, referencedColumnName = "vei_id")
	private VeiculoVO veiculo;
	
	@Column(name = "mvv_tipo_movimento", nullable = false, updatable = false)
	private Integer tipoMovimento;
	
	@Column(name = "mvv_dt_hr_entrada", nullable = false, updatable = false)
	private LocalDateTime dtHrEntrada;

	@Column(name = "mvv_dt_hr_saida", nullable = true)
	private LocalDateTime dtHrSaida;
	
	@Column(name = "mvv_situacao", nullable = false)
	private Integer situacao;

	@OneToOne(mappedBy = "movimento", fetch = FetchType.LAZY, optional = true, orphanRemoval = false)
	private MovimentoFinanceiroVO movFinanceiro;

	public MovimentoVeiculoVO() {
		
	}
	
	protected MovimentoVeiculoVO(VeiculoVO veiculoVO, Integer tipoMovimento, LocalDateTime dtHrEntrada, LocalDateTime dtHrSaida,
			Integer situacao) {
		this.veiculo = veiculoVO;
		this.tipoMovimento = tipoMovimento;
		this.dtHrEntrada = dtHrEntrada;
		this.dtHrSaida = dtHrSaida;
		this.situacao = situacao;
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

	public static MovimentoVeiculoVO criar(VeiculoVO veiculoVO, Integer tipoMovimento, LocalDateTime dtHrEntrada, 
			LocalDateTime dtHrSaida, Integer situacao) {
		return new MovimentoVeiculoVO(veiculoVO, tipoMovimento, dtHrEntrada, dtHrSaida, situacao);
	}
	
	public void vincularFinanceiro(MovimentoFinanceiroVO financeiro) {
        if (financeiro == null) {
            throw new DomainException(Constantes.COD_VALIDACAO_REGISTRO, "Não é possível vincular movimento financeiro nulo");
        }
        
        this.movFinanceiro = financeiro;
        
        if (financeiro.getMovimento() != this) {
            financeiro.setMovimento(this);
        }
    }
	
	public void encerrar(LocalDateTime dtHrSaida) {
		
		if(this.situacao == SituacaoMovimento.ENCERRADO.getId()) {
			throw new DomainException(Constantes.COD_VALIDACAO_REGISTRO, Constantes.MSG_MOV_VEI_JA_ENCERRADO);
		}
		
		this.dtHrSaida = dtHrSaida;
	}
	
	public boolean isEncerrado() {
		return this.getSituacao() == SituacaoMovimento.ENCERRADO.getId() ? true : false;
	}
}
