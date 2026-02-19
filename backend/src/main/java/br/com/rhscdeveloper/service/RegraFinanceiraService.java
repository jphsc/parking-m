package br.com.rhscdeveloper.service;

import static java.util.Objects.nonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.hibernate.PropertyValueException;
import org.jboss.logging.Logger;
import org.jboss.resteasy.spi.UnhandledException;

import com.google.gson.Gson;

import br.com.rhscdeveloper.dto.RegraFinanceiraDTO;
import br.com.rhscdeveloper.dto.RegraFinanceiraFiltroDTO;
import br.com.rhscdeveloper.dto.RespostaDTO;
import br.com.rhscdeveloper.enumerator.Enums;
import br.com.rhscdeveloper.enumerator.Enums.Situacao;
import br.com.rhscdeveloper.enumerator.Enums.TipoCobranca;
import br.com.rhscdeveloper.enumerator.Enums.TipoMovimento;
import br.com.rhscdeveloper.exception.GlobalException;
import br.com.rhscdeveloper.mapper.RegraFinanceiraMapper;
import br.com.rhscdeveloper.model.RegraFinanceiraVO;
import br.com.rhscdeveloper.repository.RegraFinanceiraRepository;
import br.com.rhscdeveloper.util.Constantes;
import io.quarkus.arc.ArcUndeclaredThrowableException;
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
			RegraFinanceiraDTO dto = regraMapper.voToDto(vo);

			return RespostaDTO.newInstance(dto, null, Constantes.MSG_SUCESSO_REGISTROS_ENCONTRADOS);
		} catch (UnhandledException | NoSuchElementException e) {
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, e.getMessage());
			
		} catch (Exception e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
			throw new GlobalException(Constantes.COD_ERRO_SERVIDOR_INTERNO, Constantes.MSG_ERRO_GENERICO);
		}
	}

	public RespostaDTO<RegraFinanceiraDTO> obterRegrasFinanceiras(Integer pagina) {
		try {
			//Thread.sleep(4000);
			Integer nroPagina = pagina >= 1  ? pagina - 1 : 0;
			List<RegraFinanceiraVO> regras = regraFinanceiraRepository.findAll(nroPagina);
			
			if(regras.isEmpty()) {
				throw new NoSuchElementException(Constantes.MSG_ERRO_NAO_ENCONTRADO);
			}
			
			List<RegraFinanceiraDTO> dtos = regras.stream().map(regraMapper::voToDto).collect(Collectors.toList());
			
			return RespostaDTO.newInstance(dtos, nroPagina, Constantes.MSG_SUCESSO_REGISTROS_ENCONTRADOS);
			
		} catch (NoSuchElementException e) {
			LOG.warn(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, e.getMessage());
			
		} catch (NullPointerException e) {
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_VALIDACAO_REGISTRO, Constantes.MSG_ERRO_PAGINA_INVALIDA);
			
		} catch (Exception e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
			throw new GlobalException(Constantes.COD_ERRO_SERVIDOR_INTERNO, Constantes.MSG_ERRO_GENERICO);
		}
	}

	public RespostaDTO<RegraFinanceiraDTO> obterRegraFinanceiraFiltro(RegraFinanceiraFiltroDTO filtro) {
		try {
			//Thread.sleep(4000);
			
			if(nonNull(filtro.tipoCobranca())) {
				Enums.getEnum(TipoCobranca.class, filtro.tipoCobranca(), Constantes.DESC_ENUM_TIPO_COBRANCA);
			} else if(nonNull(filtro.tipoMovimento())) {
				Enums.getEnum(TipoMovimento.class, filtro.tipoMovimento(), Constantes.DESC_ENUM_TIPO_MOVIMENTO);
			} else if(nonNull(filtro.situacao())) {
				Enums.getEnum(Situacao.class, filtro.situacao(), Constantes.DESC_ENUM_SITUACAO);
			}
			
			List<RegraFinanceiraVO> regras = regraFinanceiraRepository.findAll(filtro);
			
			if(regras.isEmpty()) {
				throw new NoSuchElementException(Constantes.MSG_ERRO_NAO_ENCONTRADO);
			}
			
			List<RegraFinanceiraDTO> dtos = regras.stream().map(regraMapper::voToDto).collect(Collectors.toList());
			
			return RespostaDTO.newInstance(dtos, null, Constantes.MSG_SUCESSO_REGISTROS_ENCONTRADOS);
		} catch (NoSuchElementException e) {
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, e.getMessage());	
		
		} catch (NoSuchFieldError e) {
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_VALIDACAO_REGISTRO, e.getMessage());
			
		} catch (Exception e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
			throw new GlobalException(Constantes.COD_ERRO_SERVIDOR_INTERNO, Constantes.MSG_ERRO_GENERICO);
		}
	}

	public RespostaDTO<RegraFinanceiraDTO> atualizarRegraFinanceira(RegraFinanceiraFiltroDTO filtro) {
		
		try {
			//Thread.sleep(4000);	
			if(filtro.id() <= 0) {
				throw new NoSuchFieldError(Constantes.MSG_ERRO_ID);
			}

			Enums.getEnum(TipoCobranca.class, filtro.tipoCobranca(), Constantes.DESC_ENUM_TIPO_COBRANCA);
			Enums.getEnum(TipoMovimento.class, filtro.tipoMovimento(), Constantes.DESC_ENUM_TIPO_MOVIMENTO);
			Enums.getEnum(Situacao.class, filtro.situacao(), Constantes.DESC_ENUM_SITUACAO);
			
			RegraFinanceiraVO newVo = this.atualizarRegraFinanceiraTransacional(filtro);
			RegraFinanceiraDTO dto = regraMapper.voToDto(newVo);
			
			return RespostaDTO.newInstance(dto, null, Constantes.MSG_SUCESSO_REGISTROS_ENCONTRADOS);
		} catch (NullPointerException e) {
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, Constantes.MSG_ERRO_NAO_ENCONTRADO);
			
		} catch (NoSuchFieldError e) {
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_VALIDACAO_REGISTRO, e.getMessage());
			
		} catch (NoSuchElementException e) {
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, e.getMessage());
			
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
	public RegraFinanceiraVO atualizarRegraFinanceiraTransacional(RegraFinanceiraFiltroDTO filtro) {
		RegraFinanceiraVO voPersistente = regraFinanceiraRepository.findById(filtro.id());
		
		RegraFinanceiraDTO dto = regraMapper.recordToDto(filtro);
		
		if(filtro.dtFimValidade().isBefore(LocalDate.now())) {
			dto.setSituacao(Situacao.INATIVO.getId());
		}
		
		return RegraFinanceiraVO.dtoToVo(voPersistente, dto);
	} 
	
	public RespostaDTO<RegraFinanceiraDTO> cadastrarRegraFinanceira(RegraFinanceiraFiltroDTO filtro) {
		
		try {
			//Thread.sleep(4000);
			
			TipoCobranca tpCobranca = Enums.getEnum(TipoCobranca.class, filtro.tipoCobranca(), Constantes.DESC_ENUM_TIPO_COBRANCA);
			TipoMovimento tpMovimento = Enums.getEnum(TipoMovimento.class, filtro.tipoMovimento(), Constantes.DESC_ENUM_TIPO_MOVIMENTO);
			
			RegraFinanceiraVO vo = new RegraFinanceiraVO.Builder().setDescricao(filtro.descricao())
					.setDtInicioValidade(filtro.dtInicioValidade()).setDtFimValidade(filtro.dtFimValidade())
					.setSituacao(Situacao.CADASTRADO.getId()).setTipoCobranca(tpCobranca.getId())
					.setTipoMovimento(tpMovimento.getId()).setValor(filtro.valor()).setVersao(LocalDateTime.now())
					.build();
			
			vo = this.criarRegraFinanceiraTransacional(vo);
			RegraFinanceiraDTO dto = regraMapper.voToDto(vo);
			return RespostaDTO.newInstance(dto, null, Constantes.MSG_SUCESSO_CADASTRADO);
			
		} catch(PropertyValueException e) { 
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_VALIDACAO_REGISTRO, Constantes.MSG_ERRO_NULL);
			
		} catch (NoSuchElementException e) {
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, e.getMessage());	
		
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
