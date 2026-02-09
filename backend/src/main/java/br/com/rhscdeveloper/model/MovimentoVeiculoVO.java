package br.com.rhscdeveloper.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.DynamicUpdate;

import br.com.rhscdeveloper.enumerator.Enums.SituacaoMovimento;
import br.com.rhscdeveloper.enumerator.Enums.TipoMovimento;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@DynamicUpdate
@Table(name = "tb_movimento_veiculo")
public class MovimentoVeiculoVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mvv_id")
	private Integer id;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "mvv_veiculo", nullable = false, updatable = false, referencedColumnName = "vei_id", 
			foreignKey = @ForeignKey(name = "fk_movimentoveiculo_veiculo_01"))
	private VeiculoVO veiculoVO;
	
	@Column(name = "mvv_tipo_movimento", nullable = false, updatable = false)
	private Integer tipoMovimento;
	
	@Column(name = "mvv_dt_hr_entrada", nullable = false, updatable = false)
	private LocalDateTime dtHrEntrada;

	@Column(name = "mvv_dt_hr_saida", nullable = true)
	private LocalDateTime dtHrSaida;
	
	@Column(name = "mvv_situacao", nullable = false)
	private Integer situacao;

	@Version
	@Column(name = "mvv_versao", nullable = false)
	private LocalDateTime versao;
	
	@OneToMany(mappedBy = "idMovimento", fetch = FetchType.LAZY)
	private List<MovimentoFinanceiroVO> movFinanceiro = new ArrayList<MovimentoFinanceiroVO>();

	public MovimentoVeiculoVO() {
		
	}
	
	public MovimentoVeiculoVO(VeiculoVO veiculoVO, Integer tipoMovimento, LocalDateTime dtHrEntrada, LocalDateTime dtHrSaida,
			Integer situacao, LocalDateTime versao) {
		this.veiculoVO = veiculoVO;
		this.tipoMovimento = tipoMovimento;
		this.dtHrEntrada = dtHrEntrada;
		this.dtHrSaida = dtHrSaida;
		this.situacao = situacao;
		this.versao = versao;
	}

	public MovimentoVeiculoVO(Builder b) {
		this.id = b.id;
		this.veiculoVO = b.veiculoVO;
		this.tipoMovimento = b.tipoMovimento;
		this.dtHrEntrada = b.dtHrEntrada;
		this.dtHrSaida = b.dtHrSaida;
		this.situacao = b.situacao;
		this.versao = b.versao;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public VeiculoVO getVeiculo() {
		return veiculoVO;
	}

	public void setVeiculo(VeiculoVO veiculoVO) {
		this.veiculoVO = veiculoVO;
	}

	public Integer getTipoMovimento() {
		return tipoMovimento;
	}

	public void setTipoMovimento(TipoMovimento tipoMovimento) {
		this.tipoMovimento = tipoMovimento.getId();
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

	public void setSituacao(SituacaoMovimento situacao) {
		this.situacao = situacao.getId();
	}

	public LocalDateTime getVersao() {
		return versao;
	}

	public void setVersao(LocalDateTime versao) {
		this.versao = versao;
	}
	
	public MovimentoFinanceiroVO getMovFinanceiro() {
		
		return this.movFinanceiro.isEmpty() ? null : movFinanceiro.get(0);
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
		return "MovimentoVeiculoVO [id=" + id + ", veiculoVO=" + veiculoVO + ", tipoMovimento=" + tipoMovimento
				+ ", dtHrEntrada=" + dtHrEntrada + ", dtHrSaida=" + dtHrSaida + ", situacao=" + situacao + ", versao="
				+ versao + ", movFinanceiro=" + movFinanceiro + "]";
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
	
//	public MovimentoFinanceiroVO getMovFinanceiro() {
//		MovimentoFinanceiroVO mf = MovimentoFinanceiroVO.find("idMovimento = ?1", this).firstResult();
//		this.movFinanceiro = mf;
//		return this.movFinanceiro;
//	}
//	
//	public static List<MovimentoVeiculoVO> findAll(MovimentoVeiculoDTO dto){
//		
//		Map<String, Object> params = new HashMap<String, Object>();
//		StringBuilder sb = new StringBuilder(" from MovimentoVeiculoVO mv where true");
//
//		if(dto.getDtHrEntrada() != null) {
//			sb.append(" and dtHrEntrada =: dtHrEntrada");
//			params.put("dtHrEntrada", dto.getDtHrEntrada());
//		}
//		
//		if(dto.getSituacao() != null && dto.getSituacao() != 0) {
//			sb.append(" and situacao =: situacao");
//			params.put("situacao", dto.getSituacao());
//		}
//		
//		if(dto.getPlaca() != null && !dto.getPlaca().equals("")) {
//			sb.append(" and mv.veiculo.placa =: placa");
//			params.put("placa", dto.getPlaca());
//		}
//		
//		PanacheQuery<MovimentoVeiculoVO> query = find(sb.toString(), params);
//		
//		return query.list();
//	}
}
