package br.com.rhscdeveloper.dto;

import java.util.List;

public class RespostaDTO<T> {

	private List<?> registros;
	private String mensagem;
	private Integer quantidade;
	private Integer pagina;
	
	private RespostaDTO() {
	
	}
	
	public static <T> RespostaDTO<T> newInstance(T dto, Integer pagina, String mensagem) {
		
		RespostaDTO<T> resposta = new RespostaDTO<T>();
		resposta.setMensagem(mensagem);
		resposta.setRegistros(List.of(dto));
		resposta.setPagina(pagina == null || pagina <= 0 ? 1 : pagina + 1);
		resposta.setQuantidade(1);
		return resposta;
	}
	
	public static <T> RespostaDTO<T> newInstance(List<T> dto, Integer pagina, String mensagem) {
		
		RespostaDTO<T> resposta = new RespostaDTO<T>();
		resposta.setMensagem(mensagem);
		resposta.setRegistros(dto);
		resposta.setPagina(pagina == null || pagina <= 0 ? 1 : pagina);
		resposta.setQuantidade(dto.size());
		return resposta;
	}
	
	public List<?> getRegistros() {
		return registros;
	}

	public void setRegistros(List<?> registros) {
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
}
