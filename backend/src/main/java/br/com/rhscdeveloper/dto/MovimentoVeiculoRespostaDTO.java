package br.com.rhscdeveloper.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.rhscdeveloper.enumerator.Enums.TipoOperacao;
import br.com.rhscdeveloper.model.MovimentoVeiculoVO;
import br.com.rhscdeveloper.util.MensagemResposta;

public class MovimentoVeiculoRespostaDTO {

	private List<MovimentoVeiculoDTO> registros;
	private String mensagem;
	private Long quantidade;
	private Integer pagina;
	
	public static MovimentoVeiculoRespostaDTO newInstance(List<MovimentoVeiculoVO> registrosPersistentes, TipoOperacao tipoOperacao) {
		
		List<MovimentoVeiculoDTO> registros = new ArrayList<MovimentoVeiculoDTO>();
		MovimentoVeiculoRespostaDTO resposta = new MovimentoVeiculoRespostaDTO();
		String mensagem = registrosPersistentes.isEmpty() && !tipoOperacao.equals(TipoOperacao.EXCLUIR)
				? "Não foram encontrados registros" 
				: new MensagemResposta().gerarMensagem(tipoOperacao, MovimentoVeiculoVO.class);
		
		registrosPersistentes.stream().map(
			vo -> new MovimentoVeiculoDTO.Builder()
			.setDtHrEntrada(vo.getDtHrEntrada())
			.setDtHrSaida(vo.getDtHrSaida())
			.setId(vo.getId())
			.setIdVeiculo(vo.getVeiculo().getId())
			.setIdRegra(vo.getMovFinanceiro() == null ? null : vo.getMovFinanceiro().getRegra().getId())
			.setMovimentoFinanceiro(
					vo.getMovFinanceiro() == null ? null : MovimentoFinanceiroDTO.newInstance(vo.getMovFinanceiro())
//					MovimentoFinanceiroDTO.newInstance(vo.getMovFinanceiro())
					)
			.setSituacao(vo.getSituacao())
			.setTipoMovimento(vo.getTipoMovimento())
			.setPlaca(vo.getVeiculo().getPlaca())
			.setVersao(vo.getVersao())
			.build()
		).forEach(registros::add);
		
		resposta.setRegistros(registros);
		resposta.setQuantidade(Long.valueOf(registros.size()));
		resposta.setPagina(1); // TODO Paginacao
		resposta.setMensagem(mensagem);
		
		return resposta;
	}

	public List<MovimentoVeiculoDTO> getRegistros() {
		return registros;
	}

	public void setRegistros(List<MovimentoVeiculoDTO> registros) {
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
