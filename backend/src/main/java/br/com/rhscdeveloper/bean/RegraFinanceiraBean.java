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
public class RegraFinanceiraBean extends BaseBean {

	private Integer id;
	private String descricao;
	private Double valor;
	private Integer tipoCobranca;
	private Integer tipoMovimento;
	private LocalDate dtInicioValidade;
	private LocalDate dtFimValidade;
	private Integer situacao;
	
	public RegraFinanceiraBean() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Integer getTipoCobranca() {
		return tipoCobranca;
	}

	public void setTipoCobranca(Integer tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
	}

	public Integer getTipoMovimento() {
		return tipoMovimento;
	}

	public void setTipoMovimento(Integer tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}

	public LocalDate getDtInicioValidade() {
		return dtInicioValidade;
	}

	public void setDtInicioValidade(LocalDate dtInicioValidade) {
		this.dtInicioValidade = dtInicioValidade;
	}

	public LocalDate getDtFimValidade() {
		return dtFimValidade;
	}

	public void setDtFimValidade(LocalDate dtFimValidade) {
		this.dtFimValidade = dtFimValidade;
	}

	public Integer getSituacao() {
		return situacao;
	}

	public void setSituacao(Integer situacao) {
		this.situacao = situacao;
	}

	@Override
	public String toString() {
		return "RegraFinanceiraBean [id=" + id + ", descricao=" + descricao + ", valor=" + valor + ", tipoCobranca="
				+ tipoCobranca + ", tipoMovimento=" + tipoMovimento + ", dtInicioValidade=" + dtInicioValidade
				+ ", dtFimValidade=" + dtFimValidade + ", situacao=" + situacao + "]";
	}
}
