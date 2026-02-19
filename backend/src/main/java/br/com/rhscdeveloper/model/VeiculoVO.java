package br.com.rhscdeveloper.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@DynamicUpdate
@Table(name = "tb_veiculo", indexes = {@Index(name="ix_veiculo_01", columnList = "vei_placa")})
@AttributeOverride(name = "versao", column = @Column(name = "vei_versao", nullable = false))
public class VeiculoVO extends BaseVO implements Comparable<VeiculoVO> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "vei_id")
	private Integer id;
	
	@Column(name = "vei_modelo", nullable = false)
	private String modelo;
	
	@Column(name = "vei_montadora", nullable = false)
	private String montadora;
	
	@Column(name = "vei_dt_registro", nullable = false, updatable = false)
	private LocalDate dtRegistro;
	
	@Column(name = "vei_placa", nullable = false, unique = true, length = 7)
	private String placa;
	
	@Transient
	private List<MovimentoVeiculoVO> movimentos = new ArrayList<>();
	
	public VeiculoVO() {
		
	}

	public VeiculoVO(String modelo, String montadora, LocalDate dtRegistro, String placa) {
		this.modelo = modelo.toUpperCase();
		this.montadora = montadora.toUpperCase();
		this.dtRegistro = dtRegistro;
		this.placa = placa.toUpperCase();
	}

	public VeiculoVO(Integer id, String modelo, String montadora, LocalDate dtRegistro, String placa) {
		this.id = id;
		this.modelo = modelo.toUpperCase();
		this.montadora = montadora.toUpperCase();
		this.dtRegistro = dtRegistro;
		this.placa = placa.toUpperCase();
	}

	public VeiculoVO(VeiculoVO vo) {
		this.id = vo.id;
		this.modelo = vo.modelo;
		this.montadora = vo.montadora;
		this.dtRegistro = vo.dtRegistro;
		this.placa = vo.placa;
		this.versao = vo.versao;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo.toUpperCase();
	}

	public String getMontadora() {
		return montadora;
	}

	public void setMontadora(String montadora) {
		this.montadora = montadora.toUpperCase();
	}

	public LocalDate getDtRegistro() {
		return dtRegistro;
	}

	public void setDtRegistro(LocalDate dtRegistro) {
		this.dtRegistro = dtRegistro;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa.toUpperCase();
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
		VeiculoVO other = (VeiculoVO) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "VeiculoVO [id=" + id + ", modelo=" + modelo + ", montadora=" + montadora + ", dtRegistro=" + dtRegistro
				+ ", placa=" + placa + ", versao=" + versao + ", movimentos=" + movimentos + "]";
	}

	@Override
	public int compareTo(VeiculoVO o) {
		return this.id < o.getId() ? -1 : 1;
	}
	
//	public void setMovimentos(List<MovimentoVeiculoVO> movimentos){
//		if(!movimentos.isEmpty()) {
//			movimentos.forEach(this.movimentos::add);
//		}
//	}
}
