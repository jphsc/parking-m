package br.com.rhscdeveloper.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.rhscdeveloper.util.Constantes;
import jakarta.validation.constraints.Size;

public class VeiculoDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String modelo;
	private String montadora;
	private LocalDate dtRegistro;
	@Size(max = 7, message = Constantes.MSG_ERRO_TAMANHO_PLACA)
	private String placa;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private LocalDateTime versao;
	
	public VeiculoDTO() {
		
	}

	public VeiculoDTO(Integer id, String modelo, String montadora, LocalDate dtRegistro, String placa, LocalDateTime versao) {
		this.id = id;
		this.modelo = modelo;
		this.montadora = montadora;
		this.dtRegistro = dtRegistro;
		this.placa = placa;
		this.versao = versao;
	}
	
	public VeiculoDTO(Builder build) {
		this.id = build.id;
		this.modelo = build.modelo;
		this.montadora = build.montadora;
		this.dtRegistro = build.dtRegistro;
		this.placa = build.placa;
		this.versao = build.versao;
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
	
	@Override
	public String toString() {
		return "VeiculoDTO [id=" + id + ", modelo=" + modelo + ", montadora=" + montadora + ", dtRegistro=" + dtRegistro
				+ ", placa=" + placa + ", versao=" + versao + "]";
	}

	public static class Builder {
		
		private Integer id;
		private String modelo;
		private String montadora;
		private LocalDate dtRegistro;
		private String placa;
		private LocalDateTime versao;
		
		public VeiculoDTO build() {
			return new VeiculoDTO(this);
		}

		public Builder setId(Integer id) {
			this.id = id;
			return this;
		}

		public Builder setModelo(String modelo) {
			this.modelo = modelo;
			return this;
		}

		public Builder setMontadora(String montadora) {
			this.montadora = montadora;
			return this;
		}

		public Builder setDtRegistro(LocalDate dtRegistro) {
			this.dtRegistro = dtRegistro;
			return this;
		}

		public Builder setPlaca(String placa) {
			this.placa = placa;
			return this;
		}

		public Builder setVersao(LocalDateTime versao) {
			this.versao = versao;
			return this;
		}
	}
}
