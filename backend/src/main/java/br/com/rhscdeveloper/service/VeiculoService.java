package br.com.rhscdeveloper.service;

import static java.util.Objects.nonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.hibernate.PropertyValueException;
import org.hibernate.exception.DataException;
import org.jboss.logging.Logger;

import com.google.gson.Gson;

import br.com.rhscdeveloper.dto.RespostaDTO;
import br.com.rhscdeveloper.dto.VeiculoDTO;
import br.com.rhscdeveloper.dto.VeiculoFiltroDTO;
import br.com.rhscdeveloper.exception.GlobalException;
import br.com.rhscdeveloper.exception.MyConstraintViolationException;
import br.com.rhscdeveloper.mapper.VeiculoMapper;
import br.com.rhscdeveloper.model.VeiculoVO;
import br.com.rhscdeveloper.repository.VeiculoRepository;
import br.com.rhscdeveloper.util.Constantes;
import io.quarkus.arc.ArcUndeclaredThrowableException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class VeiculoService {

	private static final Logger LOG = Logger.getLogger(GlobalException.class);
	@Inject private VeiculoRepository veiculoRepository;
	@Inject private VeiculoMapper veiculoMapper;
	
	public RespostaDTO<VeiculoDTO> obterVeiculoById(Integer id) {
				
		try {
			////Thread.sleep(4000);
			
			VeiculoVO vo = veiculoRepository.findByIdOptional(id)
					.orElseThrow(() -> new NoSuchElementException(Constantes.MSG_SUCESSO_REGISTROS_NAO_ENCONTRADOS));
			VeiculoDTO dto = veiculoMapper.voToDto(vo);
			
			return RespostaDTO.newInstance(dto, null, Constantes.MSG_SUCESSO_REGISTROS_ENCONTRADOS);
		} catch (NoSuchElementException e) {
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, e.getMessage());
			
		} catch (Exception e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
			throw new GlobalException(Constantes.COD_ERRO_SERVIDOR_INTERNO, Constantes.MSG_ERRO_GENERICO);
		}
	}

	public RespostaDTO<VeiculoDTO> obterVeiculos(Integer pagina) {
		try {
			////Thread.sleep(4000);

			Integer nroPagina = pagina >= 1  ? pagina - 1 : 0;
			List<VeiculoVO> veiculos = veiculoRepository.findAll(nroPagina);
			
			if(veiculos.isEmpty()) {
				throw new NoSuchElementException(Constantes.MSG_SUCESSO_REGISTROS_NAO_ENCONTRADOS);
			}
			
			List<VeiculoDTO> dto = veiculos.stream().map(veiculoMapper::voToDto).collect(Collectors.toList());
			
			return RespostaDTO.newInstance(dto, nroPagina, Constantes.MSG_SUCESSO_REGISTROS_ENCONTRADOS);
		} catch (NoSuchElementException e) {
			LOG.info(e.getMessage());
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

	public RespostaDTO<VeiculoDTO> obterVeiculosFiltro(VeiculoFiltroDTO filtro) {
		try {
			////Thread.sleep(4000);
			List<VeiculoVO> veiculos = veiculoRepository.findAll(filtro);
			
			if(veiculos.isEmpty()) {
				throw new NoSuchElementException(Constantes.MSG_ERRO_NAO_ENCONTRADO);
			}
			
			List<VeiculoDTO> dtos = veiculos.stream().map(veiculoMapper::voToDto).collect(Collectors.toList());
			Integer nroPagina = filtro.pagina() == null || filtro.pagina() <= 0 ? 0 : filtro.pagina();
			
			return RespostaDTO.newInstance(dtos, nroPagina, Constantes.MSG_SUCESSO_REGISTROS_ENCONTRADOS);
		} catch (NoSuchElementException e) {
			LOG.info(e.getMessage());
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

	// TODO implementar excecao de veiculo nao encontrado
	// TODO implementar validacao dos enumeradores
	public RespostaDTO<VeiculoDTO> atualizarVeiculo(VeiculoFiltroDTO filtro) {
		
		try {
			////Thread.sleep(4000);

			VeiculoVO voPersistenteId = veiculoRepository.findByIdOptional(filtro.id()).orElseThrow(() -> new NoSuchElementException(Constantes.MSG_ERRO_NAO_ENCONTRADO));
			VeiculoVO voPersistentePlaca = veiculoRepository.findByPlaca(filtro.placa());
			
			if(nonNull(voPersistentePlaca) && !voPersistentePlaca.getId().equals(voPersistenteId.getId())) {
				throw new MyConstraintViolationException(Constantes.MSG_ERRO_PLACA_EXISTE);
			}
			VeiculoDTO dto = veiculoMapper.recordToDto(filtro);
			veiculoMapper.updateVoFromDto(dto, voPersistenteId);
			voPersistenteId = criarAtualizarVeiculoBase(voPersistenteId);
			
			dto = veiculoMapper.voToDto(voPersistenteId);
			
			return RespostaDTO.newInstance(dto, null, Constantes.MSG_SUCESSO_ATUALIZADO);
		} catch(PropertyValueException e) { 
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_VALIDACAO_REGISTRO, Constantes.MSG_ERRO_NULL);
			
		} catch (NoSuchElementException e) {
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, e.getMessage());
			
		} catch(MyConstraintViolationException e) { 
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_VALIDACAO_REGISTRO, e.getMessage());
			
		} catch (Exception e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
			throw new GlobalException(Constantes.COD_ERRO_SERVIDOR_INTERNO, Constantes.MSG_ERRO_GENERICO);
		}
	}
	
	@Transactional
	protected VeiculoVO criarAtualizarVeiculoBase(VeiculoVO vo) {
		EntityManager em = veiculoRepository.getEntityManager();
		VeiculoVO voGerenciado;
		
		if (vo.getId() == null) {
			veiculoRepository.persist(vo);
			voGerenciado = vo;
		} else {
			voGerenciado = em.merge(vo);
		} 
		
		em.flush();
		em.refresh(voGerenciado);
		return voGerenciado;
	}

	// TODO implementar validacao dos enumeradores
	public RespostaDTO<VeiculoDTO> cadastrarVeiculo(VeiculoFiltroDTO filtro) {
		
		try {
			////Thread.sleep(4000);
			VeiculoVO vo = veiculoRepository.findByPlaca(filtro.placa());
			
			if(nonNull(vo)) {
				throw new MyConstraintViolationException(Constantes.MSG_ERRO_PLACA_EXISTE);
			}
			
			vo = new VeiculoVO(filtro.modelo(), filtro.montadora(), LocalDate.now(), filtro.placa());
			vo = criarAtualizarVeiculoBase(vo);
			 
			VeiculoDTO dto = veiculoMapper.voToDto(vo);
			return RespostaDTO.newInstance(dto, null, Constantes.MSG_SUCESSO_CADASTRADO);
			
		} catch(NullPointerException e) {
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_VALIDACAO_REGISTRO, e.getMessage());
			
		}  catch (MyConstraintViolationException e) {
			LOG.warn(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_VALIDACAO_REGISTRO, e.getMessage());
			
		} catch(PropertyValueException e) { 
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_VALIDACAO_REGISTRO, Constantes.MSG_ERRO_NULL);
			
		} catch(DataException e) { 
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_VALIDACAO_REGISTRO, Constantes.MSG_ERRO_TAMANHO_PLACA);
			
		} catch (Exception e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
			throw new GlobalException(Constantes.COD_ERRO_SERVIDOR_INTERNO, Constantes.MSG_ERRO_GENERICO);
		}
	}
	
	public String deletarVeiculo(Integer id) {
		try {
			////Thread.sleep(4000);
			
			this.deletarVeiculoBase(id);
			return new Gson().toJson(Constantes.MSG_SUCESSO_EXCLUIDO);
		} catch (NoSuchElementException e) {
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, e.getMessage());
			
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
	public Boolean deletarVeiculoBase(Integer id) {
		veiculoRepository.findByIdOptional(id).orElseThrow(() -> new NoSuchElementException(Constantes.MSG_ERRO_NAO_ENCONTRADO));
		return veiculoRepository.deleteById(id);
	}
}

