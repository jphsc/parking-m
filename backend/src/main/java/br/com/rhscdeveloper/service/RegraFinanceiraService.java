package br.com.rhscdeveloper.service;

import static java.util.Objects.nonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import br.com.rhscdeveloper.bean.RegraFinanceiraBean;
import br.com.rhscdeveloper.dto.RegraFinancReqDTO;
import br.com.rhscdeveloper.dto.RegraFinancRespDTO;
import br.com.rhscdeveloper.dto.RespostaDTO;
import br.com.rhscdeveloper.enumerator.Enums.Situacao;
import br.com.rhscdeveloper.exception.GlobalException;
import br.com.rhscdeveloper.mapper.RegraFinanceiraMapper;
import br.com.rhscdeveloper.model.RegraFinanceiraVO;
import br.com.rhscdeveloper.repository.RegraFinanceiraRepository;
import br.com.rhscdeveloper.util.Constantes;
import br.com.rhscdeveloper.util.Utils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class RegraFinanceiraService {

	@Inject RegraFinanceiraRepository regraFinanceiraRepository;
	@Inject RegraFinanceiraMapper regraMapper;

	public RespostaDTO<RegraFinancRespDTO> obterRegraFinanceiraById(Integer id) {

		// Thread.sleep(4000);
		RegraFinanceiraBean bean = findOrThrow(regraFinanceiraRepository.findByIdOptional(id));
		RegraFinancRespDTO dto = regraMapper.beanToDto(bean);

		return RespostaDTO.of(dto, null, Constantes.MSG_REGISTROS_ENCONTRADOS);
	}

	public RespostaDTO<RegraFinancRespDTO> obterRegrasFinanceiras(Integer pagina) {

		// Thread.sleep(4000);
		Integer nroPagina = Utils.getNroPaginaConsulta(pagina);
		List<RegraFinancRespDTO> bean = regraFinanceiraRepository.findAll(nroPagina).stream()
				.map(regraMapper::voToBean)
				.map(regraMapper::beanToDto)
				.collect(Collectors.toList());
		
		String mensagem = Utils.getMensagemBuscaRegistro(bean);
		
		return RespostaDTO.of(bean, nroPagina, mensagem);
	}

	@Transactional
	public RespostaDTO<RegraFinancRespDTO> atualizarRegraFinanceira(RegraFinancReqDTO filtro) {

		// Thread.sleep(4000);
		RegraFinanceiraVO voPersistente = regraFinanceiraRepository.findByIdOptional(filtro.id())
				.orElseThrow(() -> new GlobalException(Constantes.COD_ERRO_INEXISTENTE, Constantes.MSG_REGISTROS_NAO_ENCONTRADOS));
		
		voPersistente.atualizarRegraFinanceira(filtro.id(), filtro.descricao(), filtro.valor(), filtro.tipoCobranca(), 
				filtro.tipoMovimento(), filtro.dtInicioValidade(), filtro.dtFimValidade(), filtro.situacao());
		
		if (nonNull(filtro.dtFimValidade()) && filtro.dtFimValidade().isBefore(LocalDate.now())) {
			voPersistente.setSituacao(Situacao.INATIVO.getId());
		}

		regraFinanceiraRepository.persist(voPersistente);
		RegraFinancRespDTO dto = regraMapper.voToDto(voPersistente);
		
		return RespostaDTO.of(dto, null, Constantes.MSG_REGISTROS_ENCONTRADOS);
	}

	@Transactional
	public RespostaDTO<RegraFinancRespDTO> cadastrarRegraFinanceira(RegraFinancReqDTO filtro) {

		// Thread.sleep(4000);
		RegraFinanceiraVO vo = RegraFinanceiraVO.criar(filtro.descricao(), filtro.valor(), filtro.tipoCobranca(), 
				filtro.tipoMovimento(), filtro.dtInicioValidade(), filtro.dtFimValidade(), Situacao.CADASTRADO.getId());
		
		regraFinanceiraRepository.persist(vo);
		RegraFinancRespDTO dto = regraMapper.voToDto(vo);
		return RespostaDTO.of(dto, null, Constantes.MSG_REGISTRO_CADASTRADO);
	}

	/**
	 * Remove uma regra financeira pelo identificador da regra.
	 * 
	 * @param id - Identificador da regra
	 */
	@Transactional
	public String deletarRegraFinanceira(Integer id) {

		// Thread.sleep(4000);
		findOrThrow(regraFinanceiraRepository.findByIdOptional(id));
		regraFinanceiraRepository.deleteById(id);
		return new Gson().toJson(Constantes.MSG_REGISTRO_EXCLUIDO);
	}
	
	private RegraFinanceiraBean findOrThrow(Optional<RegraFinanceiraVO> voOptional) {
		return voOptional
				.map(regraMapper::voToBean)
				.orElseThrow(() -> new GlobalException(Constantes.COD_ERRO_INEXISTENTE, Constantes.MSG_REGISTROS_NAO_ENCONTRADOS));
	}
}
