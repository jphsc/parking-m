package br.com.rhscdeveloper.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import br.com.rhscdeveloper.model.BaseIdentificavel;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class VeiculoRespDTO implements BaseIdentificavel, Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String modelo;
	private String montadora;
	private LocalDate dtRegistro;
	private String placa;
	private LocalDateTime versao;
	
	public VeiculoRespDTO() {
		
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

	public LocalDateTime getVersao() {
		return versao;
	}

	public void setVersao(LocalDateTime versao) {
		this.versao = versao;
	}
}
