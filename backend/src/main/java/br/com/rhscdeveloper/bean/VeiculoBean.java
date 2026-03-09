package br.com.rhscdeveloper.bean;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class VeiculoBean extends BaseBean {
	
	private Integer id;
	private String modelo;
	private String montadora;
	private LocalDate dtRegistro;
	private String placa;
	
	public VeiculoBean() {
		
	}
	
	public VeiculoBean(Integer id, String modelo, String montadora, LocalDate dtRegistro, String placa) {
		this.id = id;
		this.modelo = modelo;
		this.montadora = montadora;
		this.dtRegistro = dtRegistro;
		this.placa = placa;
	}
	
	public VeiculoBean(String modelo, String montadora, LocalDate dtRegistro, String placa) {
		this.modelo = modelo;
		this.montadora = montadora;
		this.dtRegistro = dtRegistro;
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
}
