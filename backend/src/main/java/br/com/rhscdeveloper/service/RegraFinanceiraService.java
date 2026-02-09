package br.com.rhscdeveloper.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.hibernate.PropertyValueException;
import org.jboss.logging.Logger;
import org.jboss.resteasy.spi.UnhandledException;

import com.google.gson.Gson;

import br.com.rhscdeveloper.dto.RegraFinanceiraDTO;
import br.com.rhscdeveloper.dto.RegraFinanceiraRespostaDTO;
import br.com.rhscdeveloper.dto.RespostaDTO;
import br.com.rhscdeveloper.enumerator.Enums;
import br.com.rhscdeveloper.enumerator.Enums.Situacao;
import br.com.rhscdeveloper.enumerator.Enums.TipoCobranca;
import br.com.rhscdeveloper.enumerator.Enums.TipoMovimento;
import br.com.rhscdeveloper.enumerator.Enums.TipoOperacao;
import br.com.rhscdeveloper.exception.GlobalException;
import br.com.rhscdeveloper.mapper.RegraFinanceiraMapper;
import br.com.rhscdeveloper.model.RegraFinanceiraVO;
import br.com.rhscdeveloper.repository.RegraFinanceiraRepository;
import br.com.rhscdeveloper.util.Constantes;
import io.quarkus.arc.ArcUndeclaredThrowableException;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class RegraFinanceiraService {
	
	@Inject RegraFinanceiraRepository regraFinanceiraRepository;
	@Inject RegraFinanceiraMapper regraMapper;
	
	private static final Logger LOG = Logger.getLogger(GlobalException.class);
	
	public RespostaDTO<RegraFinanceiraDTO> obterRegraFinanceiraById(Integer id) {
		
		try {
			//Thread.sleep(4000);
			
			RegraFinanceiraVO vo = regraFinanceiraRepository
					.findByIdOptional(id).orElseThrow(() -> new NoSuchElementException(Constantes.MSG_ERRO_NAO_ENCONTRADO));
			RegraFinanceiraDTO dto = regraMapper.toDto(vo);

			return RespostaDTO.newInstance(dto, Constantes.MSG_SUCESSO_REGISTROS_ENCONTRADOS);
		} catch (UnhandledException | NoSuchElementException e) {
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, e.getMessage());
			
		} catch (Exception e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
			throw new GlobalException(Constantes.COD_ERRO_SERVIDOR_INTERNO, Constantes.MSG_ERRO_GENERICO);
		}
	}

	public RespostaDTO<RegraFinanceiraDTO> obterRegrasFinanceiras() {
		try {
			//Thread.sleep(4000);
			
			List<RegraFinanceiraVO> regras = regraFinanceiraRepository.findAll(Sort.by("id")).list();
			
			if(regras.isEmpty()) {
				throw new NoSuchElementException(Constantes.MSG_ERRO_NAO_ENCONTRADO);
			}
			
			List<RegraFinanceiraDTO> dtos = regras.stream().map(regraMapper::toDto).collect(Collectors.toList());
			
			return RespostaDTO.newInstance(dtos, Constantes.MSG_SUCESSO_REGISTROS_ENCONTRADOS);
			
		} catch (NoSuchElementException e) {
			LOG.warn(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, e.getMessage());
			
		} catch (Exception e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
			throw new GlobalException(Constantes.COD_ERRO_SERVIDOR_INTERNO, Constantes.MSG_ERRO_GENERICO);
		}
	}

	public RespostaDTO<RegraFinanceiraDTO> obterRegraFinanceiraFiltro(RegraFinanceiraDTO filtro) {
		try {
			//Thread.sleep(4000);	
			
			List<RegraFinanceiraVO> regras = regraFinanceiraRepository.findAll(filtro);
			
			if(regras.isEmpty()) {
				throw new NoSuchElementException(Constantes.MSG_ERRO_NAO_ENCONTRADO);
			}
			
			List<RegraFinanceiraDTO> dtos = regras.stream().map(regraMapper::toDto).collect(Collectors.toList());
			
			return RespostaDTO.newInstance(dtos, Constantes.MSG_SUCESSO_REGISTROS_ENCONTRADOS);
		} catch (NoSuchElementException e) {
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, e.getMessage());	
		
		} catch (Exception e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
			throw new GlobalException(Constantes.COD_ERRO_SERVIDOR_INTERNO, Constantes.MSG_ERRO_GENERICO);
		}
	}

	// TODO implementar validacao dos enumeradores
	public RegraFinanceiraRespostaDTO atualizarRegraFinanceira(RegraFinanceiraDTO filtro) {
		
		try {
			//Thread.sleep(4000);	
			if(filtro.getId() == null || filtro.getId() == 0) {
				throw new NoSuchFieldError(Constantes.MSG_ERRO_ID);
			}

			TipoCobranca tpCobranca = Enums.getEnum(TipoCobranca.class, filtro.getTipoCobranca(), Constantes.DESC_ENUM_TIPO_COBRANCA);
			TipoMovimento tpMovimento = Enums.getEnum(TipoMovimento.class, filtro.getTipoMovimento(), Constantes.DESC_ENUM_TIPO_MOVIMENTO);
			Situacao situacao = Enums.getEnum(Situacao.class, filtro.getSituacao(), Constantes.DESC_ENUM_SITUACAO);

			filtro.setTipoCobranca(tpCobranca.getId());
			filtro.setTipoMovimento(tpMovimento.getId());
			filtro.setSituacao(situacao.getId());
			
			RegraFinanceiraVO newVo = this.atualizarRegraFinanceiraTransacional(filtro);
			
			return RegraFinanceiraRespostaDTO.newInstance(Arrays.asList(newVo), TipoOperacao.EDITAR);
		} catch (NullPointerException e) {
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, Constantes.MSG_ERRO_CAMPOS);
			
		} catch (NoSuchElementException e) {
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, Constantes.MSG_ERRO_NAO_ENCONTRADO);
			
		} catch(PropertyValueException e) { 
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_VALIDACAO_REGISTRO, Constantes.MSG_ERRO_NULL);
		
		} catch (Exception e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
			throw new GlobalException(Constantes.COD_ERRO_SERVIDOR_INTERNO, Constantes.MSG_ERRO_GENERICO);
		}
	}
	
	@Transactional
	public RegraFinanceiraVO atualizarRegraFinanceiraTransacional(RegraFinanceiraDTO filtro) {
		RegraFinanceiraVO voPersistente = regraFinanceiraRepository.findById(filtro.getId());
		
		if(filtro.getDtFimValidade().isBefore(LocalDate.now())) {
			filtro.setSituacao(Situacao.INATIVO.getId());
		}
		
		return RegraFinanceiraVO.dtoToVo(voPersistente, filtro);
	} 
	
	// TODO implementar validacao dos enumeradores
	public RespostaDTO<RegraFinanceiraDTO> cadastrarRegraFinanceira(RegraFinanceiraDTO filtro) {
		
		try {
			//Thread.sleep(4000);
			
			TipoCobranca tpCobranca = Enums.getEnum(TipoCobranca.class, filtro.getTipoCobranca(), Constantes.DESC_ENUM_TIPO_COBRANCA);
			TipoMovimento tpMovimento = Enums.getEnum(TipoMovimento.class, filtro.getTipoMovimento(), Constantes.DESC_ENUM_TIPO_MOVIMENTO);
			
			RegraFinanceiraVO vo = new RegraFinanceiraVO.Builder().setDescricao(filtro.getDescricao())
					.setDtInicioValidade(filtro.getDtInicioValidade()).setDtFimValidade(filtro.getDtFimValidade())
					.setSituacao(Situacao.CADASTRADO.getId()).setTipoCobranca(tpCobranca.getId())
					.setTipoMovimento(tpMovimento.getId()).setValor(filtro.getValor()).setVersao(LocalDateTime.now())
					.build();
			
			vo = this.criarRegraFinanceiraTransacional(vo);
			RegraFinanceiraDTO dto = regraMapper.toDto(vo);
			return RespostaDTO.newInstance(dto, Constantes.MSG_SUCESSO_CADASTRADO);
			
		} catch(PropertyValueException e) { 
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_VALIDACAO_REGISTRO, Constantes.MSG_ERRO_NULL);
			
		} catch (Exception e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
			throw new GlobalException(Constantes.COD_ERRO_SERVIDOR_INTERNO, Constantes.MSG_ERRO_GENERICO);
		}
	}
	
	@Transactional
	protected RegraFinanceiraVO criarRegraFinanceiraTransacional(RegraFinanceiraVO vo) {
		regraFinanceiraRepository.persist(vo);
		return vo;
	}
	
	/**
	 * <p>Remove uma regra financeira pelo identificador da regra.</p>
	 * @param id - Identificador da regra
	 * */
	public String deletarRegraFinanceira(Integer id) {
		try {
			//Thread.sleep(4000);	
			RegraFinanceiraVO regra = regraFinanceiraRepository.findByIdOptional(id)
					.orElseThrow(() -> new NoSuchElementException(Constantes.MSG_ERRO_NAO_ENCONTRADO));

			this.deletarRegraFinanceiraTransacional(regra.getId());
			return new Gson().toJson(Constantes.MSG_SUCESSO_EXCLUIDO);
			
		} catch (NoSuchElementException e) {
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, Constantes.MSG_ERRO_NAO_ENCONTRADO);
			
		} catch (ArcUndeclaredThrowableException e) {
			LOG.info(e.getCause());
			throw new GlobalException(Constantes.COD_ERRO_VALIDACAO_REGISTRO, Constantes.MSG_ERRO_VINCULO);
			
		} catch (Exception e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
			throw new GlobalException(Constantes.COD_ERRO_SERVIDOR_INTERNO, Constantes.MSG_ERRO_GENERICO);
		}
	}
	
	@Transactional
	protected Boolean deletarRegraFinanceiraTransacional(Integer id) {
		return regraFinanceiraRepository.deleteById(id);
	}
}
