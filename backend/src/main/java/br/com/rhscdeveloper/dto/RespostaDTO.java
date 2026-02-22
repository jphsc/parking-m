package br.com.rhscdeveloper.dto;

import static java.util.Objects.isNull;

import java.util.Collections;
import java.util.List;

public class RespostaDTO<T> {

	private List<T> registros;
	private String mensagem;
	private Integer quantidade;
	private Integer pagina;
	
	private RespostaDTO() {
	
	}
	
	public static <T> RespostaDTO<T> of(T dto, Integer pagina, String mensagem) {
		
		RespostaDTO<T> resposta = new RespostaDTO<T>();
		resposta.setMensagem(mensagem);
		resposta.setRegistros(validarRegsDto(dto));
		resposta.setPagina(pagina);
		resposta.setQuantidade(isNull(dto) ? 0 : 1);
		return resposta;
	}
	
	public static <T> RespostaDTO<T> of(List<T> dto, Integer pagina, String mensagem) {
		
		RespostaDTO<T> resposta = new RespostaDTO<T>();
		resposta.setMensagem(mensagem);
		resposta.setRegistros(dto);
		resposta.setPagina(pagina);
		resposta.setQuantidade(isNull(dto) ? 0 : dto.size());
		return resposta;
	}
	
	public List<T> getRegistros() {
		return registros;
	}

	public void setRegistros(List<T> registros) {
		this.registros = registros;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Integer getPagina() {
		return pagina;
	}

	public void setPagina(Integer pagina) {
		this.pagina = pagina;
	}
	
	private static <T> List<T> validarRegsDto(T dto){
		return dto == null ? Collections.emptyList() : List.of(dto);
	}
}
