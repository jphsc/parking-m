package br.com.rhscdev.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicUpdate;

import br.com.rhscdev.domain.enumerator.Enums;
import br.com.rhscdev.domain.enumerator.Enums.TipoMovimento;
import br.com.rhscdev.domain.exception.DomainException;
import br.com.rhscdev.infrastructure.config.Constantes;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@MapsId("idRegra")
    @JoinColumn(name = "mvf_regra", nullable = false, updatable = false)
    private RegraFinanceiraVO regra;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@MapsId("idMovimento")
    @JoinColumn(name = "mvf_movimento", nullable = false, updatable = false)
    private MovimentoVeiculoVO movimento;
	
	@Column(name = "mvf_valor", nullable = false)
	private BigDecimal valor;
	
	@Column(name = "mvf_situacao", nullable = false)
	private Integer situacao;

	private MovimentoFinanceiroVO() {
		
	}

	private MovimentoFinanceiroVO(RegraFinanceiraVO regra, MovimentoVeiculoVO movimento, Integer situacao) {
		this.setRegra(regra);
		this.setMovimento(movimento);
		this.valor = this.gerarValorMovimento(regra.getValor(), movimento.getTipoMovimento(), movimento.getDtHrEntrada());
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
		if (this.movimento == movimento) {
	        return;
	    }

	    this.movimento = movimento;
	    this.idMovimento = (movimento != null) ? movimento.getId() : null;

	    if (movimento != null && movimento.getMovFinanceiro() != this) {
	        movimento.vincularFinanceiro(this);
	    }
    }

	public BigDecimal getValor() {
		return valor;
	}

	public void setSituacao(Integer situacao) {
		this.situacao = situacao;
	}

	public Integer getSituacao() {
		return situacao;
	}

	public void setValor(BigDecimal valor) {
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
	
	public static MovimentoFinanceiroVO criar(RegraFinanceiraVO regra, MovimentoVeiculoVO movimento, Integer situacao) {
		if (regra == null) {
            throw new DomainException(Constantes.COD_VALIDACAO_REGISTRO, Constantes.MSG_MOV_FIN_REGRA_OBRIGATORIO);
        }
        
        if (movimento == null) {
            throw new DomainException(Constantes.COD_VALIDACAO_REGISTRO, Constantes.MSG_MOV_FIN_MOV_FIN_OBRIGATORIO);
        }
        
        if (situacao == null) {
            throw new DomainException(Constantes.COD_VALIDACAO_REGISTRO, Constantes.MSG_MOV_FIN_SITUACAO_OBRIGATORIO);
        }
        
        return new MovimentoFinanceiroVO(regra, movimento, situacao);
	}
	
	/**
	 * Calcula o valor do movimento financeiro
	 * 
	 * <p>Dependendo do tipo de movimento (mensalista, final de semana, etc.), aplica regras diferentes:
	 * - Para mensalistas ou finais de semana, retorna o valor fixo da regra.
	 * - Para demais casos, calcula o valor com base no número de horas e valor da regra.
	 * 
	 * @param movimento - movimento do veículo do tipo MovimentoVeiculoVO
	 * @param regraFinanceira - regra financeira aplicável do tipo RegraFinanceiraVO
	 * @param dataHoraEntrada - Data e hora de entrada do veículo
	 * @return valor calculado do movimento do veiculo, considerando sua regra e entrada
	 *  */
	private BigDecimal gerarValorMovimento(BigDecimal valorRegra, Integer idTipoMovimento, LocalDateTime entrada) {
		
		TipoMovimento tipoMovimento = Enums.getEnum(TipoMovimento.class, idTipoMovimento, Constantes.DESC_ENUM_TIPO_MOVIMENTO);
		
		Duration duracao = Duration.between(entrada, LocalDateTime.now());
		long dias = duracao.toDays();
		long minutos = duracao.toMinutes();
		int horas = (int) (minutos%60 > 10 ? (minutos/60 + 1) : (minutos/60));
		BigDecimal valor = BigDecimal.ZERO;
		

		switch(tipoMovimento) {
			case HORA:
				valor = valorRegra.multiply(BigDecimal.valueOf(horas)); //valorRegra * horas;
				break;
			case DIA:
				valor = valorRegra.multiply(BigDecimal.valueOf(dias)); //valorRegra * dias;
				break;
			default:
				valor = valorRegra;
		}

		return valor.setScale(2, RoundingMode.HALF_UP);
	}
}
