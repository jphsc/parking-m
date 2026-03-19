package br.com.rhscdev.domain.entity;

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
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@DynamicUpdate
@Table(name = "tb_veiculo")
@AttributeOverride(name = "versao", column = @Column(name = "vei_versao", nullable = false))
public class VeiculoVO extends BaseVO implements Comparable<VeiculoVO> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "veiculo_seq")
	@SequenceGenerator(name = "veiculo_seq", sequenceName = "tb_veiculo_vei_id_seq", allocationSize = 1)
	@Column(name = "vei_id")
	private Integer id;
	
	@Column(name = "vei_modelo", nullable = false, length = 20)
	private String modelo;
	
	@Column(name = "vei_montadora", nullable = false, length = 20)
	private String montadora;
	
	@Column(name = "vei_dt_registro", nullable = false, updatable = false)
	private LocalDate dtRegistro;
	
	@Column(name = "vei_placa", nullable = false, unique = true, length = 7)
	private String placa;
	
	@Transient
	private List<MovimentoVeiculoVO> movimentos = new ArrayList<>();
	
	public VeiculoVO() {
		
	}
	
	protected VeiculoVO(String modelo, String montadora, String placa) {
		this.modelo = modelo;
		this.montadora = montadora;
		this.dtRegistro = LocalDate.now();
		this.placa = placa;
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
		this.modelo = modelo;
	}

	public String getMontadora() {
		return montadora;
	}

	public void setMontadora(String montadora) {
		this.montadora = montadora;
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
		this.placa = placa;
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
		return "VeiculoVO [id=" + id + ", modelo=" + modelo + ", montadora=" + montadora + ", dtRegistro=" + 
				dtRegistro + ", placa=" + placa + ", versao=" + versao + ", movimentos=" + movimentos + "]";
	}

	@Override
	public int compareTo(VeiculoVO o) {
		return this.id < o.getId() ? -1 : 1;
	}
	
	public static VeiculoVO criar(String modelo, String montadora, String placa) {
		return new VeiculoVO(modelo, montadora, placa);
	}
	
	public void atualizar(String modelo, String montadora, String placa) {
		if(modelo != null) this.modelo = modelo;
		if(montadora != null) this.montadora = montadora;
		if(placa != null) this.placa = placa;
	}
}
