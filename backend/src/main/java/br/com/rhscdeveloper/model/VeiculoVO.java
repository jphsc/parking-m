package br.com.rhscdeveloper.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.DynamicUpdate;

import br.com.rhscdeveloper.dto.VeiculoDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;

@Entity
@DynamicUpdate
@Table(name = "tb_veiculo", indexes = {@Index(name="ix_veiculo_01", columnList = "vei_placa")})
public class VeiculoVO implements Serializable, Comparable<VeiculoVO> {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "vei_id")
	private Integer id;
	
	@Column(name = "vei_modelo", nullable = false)
	private String modelo;
	
	@Column(name = "vei_montadora", nullable = false)
	private String montadora;
	
	@Column(name = "vei_dt_registro", nullable = false)
	private LocalDateTime dtRegistro;
	
	@Column(name = "vei_placa", nullable = false, unique = true, length = 7)
	private String placa;
	
	@Version
	@Column(name = "vei_versao", nullable = false)
	private LocalDateTime versao;
	
	@Transient
	private List<MovimentoVeiculoVO> movimentos = new ArrayList<>();
	
	public VeiculoVO() {
		
	}

	public VeiculoVO(String modelo, String montadora, LocalDateTime dtRegistro, String placa, LocalDateTime versao) {
		this.modelo = modelo;
		this.montadora = montadora;
		this.dtRegistro = dtRegistro;
		this.placa = placa.toUpperCase();
		this.versao = versao;
	}

	public VeiculoVO(Integer id, String modelo, String montadora, LocalDateTime dtRegistro, String placa, LocalDateTime versao) {
		this.id = id;
		this.modelo = modelo;
		this.montadora = montadora;
		this.dtRegistro = dtRegistro;
		this.placa = placa.toUpperCase();
		this.versao = versao;
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

	public LocalDateTime getDtRegistro() {
		return dtRegistro;
	}

	public void setDtRegistro(LocalDateTime dtRegistro) {
		this.dtRegistro = dtRegistro;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa.toUpperCase();
	}

	public LocalDateTime getVersao() {
		return versao;
	}

	public void setVersao(LocalDateTime versao) {
		this.versao = versao;
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
	
	public void setMovimentos(List<MovimentoVeiculoVO> movimentos){
		if(!movimentos.isEmpty()) {
			movimentos.forEach(this.movimentos::add);
		}
	}

	public static VeiculoVO dtoToVo(VeiculoVO voPersistente, VeiculoDTO dto) {
		voPersistente.id = dto.getId();
		voPersistente.modelo = dto.getModelo();
		voPersistente.montadora = dto.getMontadora();
		voPersistente.dtRegistro = dto.getDtRegistro();
		voPersistente.placa = dto.getPlaca().toUpperCase();
		voPersistente.versao = Objects.isNull(dto.getVersao()) ? LocalDateTime.now() : dto.getVersao();
		
		return voPersistente;
	}

}
