package br.com.rhscdeveloper.dto;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.rhscdeveloper.enumerator.Enums.TipoOperacao;
import br.com.rhscdeveloper.model.VeiculoVO;
import br.com.rhscdeveloper.util.MensagemResposta;

public class VeiculoRespostaDTO {

	private List<VeiculoDTO> registros;
	private String mensagem;
	private Long quantidade;
	private Integer pagina;
	
	public static RespostaDTO<VeiculoDTO> newInstance(VeiculoDTO dto, String mensagem){
		
		return null;
	}
	
	public static VeiculoRespostaDTO newInstance(Collection<VeiculoVO> registrosPersistentes, TipoOperacao tipoOperacao) {
		
		List<VeiculoDTO> registros = new ArrayList<VeiculoDTO>();
		VeiculoRespostaDTO resposta = new VeiculoRespostaDTO();
		String mensagem = registrosPersistentes.isEmpty() && !tipoOperacao.equals(TipoOperacao.EXCLUIR)
				? "Não foram encontrados registros" 
				: new MensagemResposta().gerarMensagem(tipoOperacao, VeiculoVO.class);
		
		registrosPersistentes.stream().map(
			vo -> new VeiculoDTO.Builder()
				.setDtRegistro(vo.getDtRegistro())
				.setId(vo.getId())
				.setModelo(vo.getModelo())
				.setMontadora(vo.getMontadora())
				.setPlaca(vo.getPlaca())
				.setVersao(vo.getVersao()).build()
		).forEach(registros::add);
		
		resposta.setRegistros(registros);
		resposta.setQuantidade(Long.valueOf(registros.size()));
		resposta.setPagina(1); // TODO Paginacao
		resposta.setMensagem(mensagem);
		
		return resposta;
	}
	
	public static VeiculoRespostaDTO newInstance(Collection<VeiculoVO> veiculosPersistentes, VeiculoDTO dto, TipoOperacao tipoOperacao) {
		VeiculoRespostaDTO resposta = newInstance(veiculosPersistentes, tipoOperacao);
		resposta.setPagina(isNull(dto) || dto.getPagina() == null ? 1 : dto.getPagina());
		return resposta;
	}

	public List<VeiculoDTO> getRegistros() {
		return registros;
	}

	public void setRegistros(List<VeiculoDTO> registros) {
		this.registros = registros;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}

	public Integer getPagina() {
		return pagina;
	}

	public void setPagina(Integer pagina) {
		this.pagina = pagina;
	}
}
