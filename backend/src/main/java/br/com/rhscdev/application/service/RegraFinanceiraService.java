package br.com.rhscdev.application.service;

import static java.util.Objects.nonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import br.com.rhscdev.application.dto.request.RegraFinanceiraRequest;
import br.com.rhscdev.application.dto.response.DataQueryResult;
import br.com.rhscdev.application.dto.response.RegraFinanceiraResponse;
import br.com.rhscdev.application.dto.response.RespostaPaginada;
import br.com.rhscdev.application.mapper.RegraFinanceiraMapper;
import br.com.rhscdev.domain.entity.RegraFinanceiraVO;
import br.com.rhscdev.domain.enumerator.Enums.Situacao;
import br.com.rhscdev.infrastructure.config.Constantes;
import br.com.rhscdev.infrastructure.config.Utils;
import br.com.rhscdev.infrastructure.persistence.RegraFinancPanacheRepository;
import br.com.rhscdev.interfaces.rest.handler.GlobalException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class RegraFinanceiraService {

	private RegraFinanceiraMapper regraMapper;
	private RegraFinancPanacheRepository regraFinancPanacheRepository;


	RegraFinanceiraService(RegraFinancPanacheRepository regraFinancPanacheRepository, RegraFinanceiraMapper regraMapper) {
		this.regraFinancPanacheRepository = regraFinancPanacheRepository;
		this.regraMapper = regraMapper;
	}	
		public RespostaPaginada<RegraFinanceiraResponse> obterRegraFinanceiraById(Integer id) {

		
		RegraFinanceiraVO vo = regraFinancPanacheRepository.findByIdOp(id)
				.orElseThrow(() -> new GlobalException(Constantes.COD_INEXISTENTE, Constantes.MSG_REGISTROS_NAO_ENCONTRADOS));
		RegraFinanceiraResponse dto = regraMapper.toResponse(vo);

		return RespostaPaginada.of(dto, null, Constantes.MSG_REGISTROS_ENCONTRADOS);
	}

	public RespostaPaginada<RegraFinanceiraResponse> obterRegrasFinanceiras(Integer pagina, Integer qtdRegistros) {

		Integer nroPagina = Utils.getNroPaginaConsulta(pagina);
		DataQueryResult<RegraFinanceiraVO> res = regraFinancPanacheRepository.findAll(nroPagina, qtdRegistros);
		List<RegraFinanceiraResponse> bean = res.registros().stream().map(regraMapper::toResponse).collect(Collectors.toList());
		String mensagem = Utils.getMensagemBuscaRegistro(bean);
		
		return RespostaPaginada.of(bean, nroPagina, mensagem, res.quantidade());
	}

	@Transactional
	public RespostaPaginada<RegraFinanceiraResponse> atualizarRegraFinanceira(RegraFinanceiraRequest filtro) {

		RegraFinanceiraVO voPersistente = regraFinancPanacheRepository.findByIdOp(filtro.id())
				.orElseThrow(() -> new GlobalException(Constantes.COD_INEXISTENTE, Constantes.MSG_REGISTROS_NAO_ENCONTRADOS));
		
		voPersistente.atualizar(filtro.id(), filtro.descricao(), filtro.valor(), filtro.tipoCobranca(), 
				filtro.tipoMovimento(), filtro.dtInicioValidade(), filtro.dtFimValidade(), filtro.situacao());
		
		if (nonNull(filtro.dtFimValidade()) && filtro.dtFimValidade().isBefore(LocalDate.now())) {
			voPersistente.setSituacao(Situacao.INATIVO.getId());
		}

		voPersistente = regraFinancPanacheRepository.save(voPersistente);
		RegraFinanceiraResponse dto = regraMapper.toResponse(voPersistente);
		
		return RespostaPaginada.of(dto, null, Constantes.MSG_REGISTROS_ENCONTRADOS);
	}

	@Transactional
	public RespostaPaginada<RegraFinanceiraResponse> cadastrarRegraFinanceira(RegraFinanceiraRequest filtro) {

		RegraFinanceiraVO vo = RegraFinanceiraVO.criar(filtro.descricao(), filtro.valor(), filtro.tipoCobranca(), 
				filtro.tipoMovimento(), filtro.dtInicioValidade(), filtro.dtFimValidade(), Situacao.CADASTRADO.getId());
		
		vo = regraFinancPanacheRepository.save(vo);
		RegraFinanceiraResponse dto = regraMapper.toResponse(vo);
		return RespostaPaginada.of(dto, null, Constantes.MSG_REGISTRO_CADASTRADO);
	}

	/**
	 * Remove uma regra financeira pelo identificador da regra.
	 * 
	 * @param id - Identificador da regra
	 */
	@Transactional
	public String deletarRegraFinanceira(Integer id) {

		regraFinancPanacheRepository.findByIdOp(id)
			.orElseThrow(() -> new GlobalException(Constantes.COD_INEXISTENTE, Constantes.MSG_REGISTROS_NAO_ENCONTRADOS));
		
		regraFinancPanacheRepository.delete(id);
		return new Gson().toJson(Constantes.MSG_REGISTRO_EXCLUIDO);
	}
}
