package br.com.rhscdeveloper.service;

import static java.util.Objects.nonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import br.com.rhscdeveloper.dto.RegraFinRequestDTO;
import br.com.rhscdeveloper.dto.RegraFinResponseDTO;
import br.com.rhscdeveloper.dto.RespostaDTO;
import br.com.rhscdeveloper.enumerator.Enums.Situacao;
import br.com.rhscdeveloper.enumerator.Enums.TipoOperacao;
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

	public RespostaDTO<RegraFinResponseDTO> obterRegraFinanceiraById(Integer id) {

		// Thread.sleep(4000);
		RegraFinanceiraVO vo = regraFinanceiraRepository.findByIdOptional(id)
				.orElseThrow(() -> new GlobalException(Constantes.COD_ERRO_INEXISTENTE, Constantes.MSG_REGISTROS_NAO_ENCONTRADOS));
		RegraFinResponseDTO dto = regraMapper.voToDto(vo);
		String mensagem = Utils.getMensagemBuscaRegistro(vo);

		return RespostaDTO.of(dto, null, mensagem);
	}

	public RespostaDTO<RegraFinResponseDTO> obterRegrasFinanceiras(Integer pagina) {

		// Thread.sleep(4000);
		Integer nroPagina = Utils.getNroPaginaConsulta(pagina);
		List<RegraFinanceiraVO> regras = regraFinanceiraRepository.findAll(nroPagina);
		List<RegraFinResponseDTO> dtos = regras.stream().map(regraMapper::voToDto).collect(Collectors.toList());
		String mensagem = Utils.getMensagemBuscaRegistro(regras);
		
		return RespostaDTO.of(dtos, nroPagina, mensagem);
	}

	@Transactional
	public RespostaDTO<RegraFinResponseDTO> atualizarRegraFinanceira(RegraFinRequestDTO filtro) {

		// Thread.sleep(4000);
		this.validarRegraOperacao(filtro, TipoOperacao.EDITAR);
		
		RegraFinanceiraVO voPersistente = regraFinanceiraRepository.findByIdOptional(filtro.id())
				.orElseThrow(() -> new GlobalException(Constantes.COD_ERRO_INEXISTENTE, Constantes.MSG_REGISTROS_NAO_ENCONTRADOS));

		RegraFinResponseDTO dto = regraMapper.recordToDto(filtro);
		regraMapper.updateVoFromDto(dto, voPersistente);
		
		if (nonNull(dto.getDtFimValidade()) && dto.getDtFimValidade().isBefore(LocalDate.now())) {
			voPersistente.setSituacao(Situacao.INATIVO.getId());
		}
		dto = regraMapper.voToDto(voPersistente);
		
		System.out.println(dto);
		System.out.println(voPersistente);

		return RespostaDTO.of(dto, null, Constantes.MSG_REGISTROS_ENCONTRADOS);
	}

	@Transactional
	public RespostaDTO<RegraFinResponseDTO> cadastrarRegraFinanceira(RegraFinRequestDTO filtro) {

		// Thread.sleep(4000);
		this.validarRegraOperacao(filtro, TipoOperacao.CADASTRAR);
		
		RegraFinanceiraVO vo = new RegraFinanceiraVO.Builder().setDescricao(filtro.descricao())
				.setDtInicioValidade(filtro.dtInicioValidade()).setDtFimValidade(filtro.dtFimValidade())
				.setSituacao(Situacao.CADASTRADO.getId()).setTipoCobranca(filtro.tipoCobranca())
				.setTipoMovimento(filtro.tipoMovimento()).setValor(filtro.valor()).setVersao(LocalDateTime.now()).build();

		regraFinanceiraRepository.persist(vo);
		RegraFinResponseDTO dto = regraMapper.voToDto(vo);
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
		regraFinanceiraRepository.findByIdOptional(id)
			.orElseThrow(() -> new GlobalException(Constantes.COD_ERRO_INEXISTENTE, Constantes.MSG_REGISTROS_NAO_ENCONTRADOS));
		regraFinanceiraRepository.deleteById(id);
		
		return new Gson().toJson(Constantes.MSG_REGISTRO_EXCLUIDO);
	}
	
	private void validarRegraOperacao(RegraFinRequestDTO dto, TipoOperacao op) {
		
		if(op.equals(TipoOperacao.CADASTRAR)) {
			Objects.requireNonNull(dto.descricao(), String.format(Constantes.NECESSARIO_INFORMAR, Constantes.DESCRICAO));
			Objects.requireNonNull(dto.tipoCobranca(), String.format(Constantes.NECESSARIO_INFORMAR, Constantes.DESC_ENUM_TIPO_COBRANCA));
			Objects.requireNonNull(dto.tipoMovimento(), String.format(Constantes.NECESSARIO_INFORMAR, Constantes.DESC_ENUM_TIPO_MOVIMENTO));
			Objects.requireNonNull(dto.dtInicioValidade(), String.format(Constantes.NECESSARIO_INFORMAR, Constantes.DT_HR_INI_VALIDADE));
			
		} else if(op.equals(TipoOperacao.EDITAR)) {
			Objects.requireNonNull(dto.id(), Constantes.MSG_SEM_ID_REGISTRO);
		}
	}
}
