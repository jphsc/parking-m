package br.com.rhscdeveloper.service;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.hibernate.PropertyValueException;
import org.jboss.logging.Logger;

import com.google.gson.Gson;

import br.com.rhscdeveloper.dto.RespostaDTO;
import br.com.rhscdeveloper.dto.VeiculoDTO;
import br.com.rhscdeveloper.exception.GlobalException;
import br.com.rhscdeveloper.exception.MyConstraintViolationException;
import br.com.rhscdeveloper.mapper.VeiculoMapper;
import br.com.rhscdeveloper.model.VeiculoVO;
import br.com.rhscdeveloper.repository.VeiculoRepository;
import br.com.rhscdeveloper.util.Constantes;
import io.quarkus.arc.ArcUndeclaredThrowableException;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class VeiculoService {

	private static final Logger LOG = Logger.getLogger(GlobalException.class);
	@Inject private VeiculoRepository veiculoRepository;
	@Inject private VeiculoMapper veiculoMapper;
	
	public RespostaDTO<VeiculoDTO> obterVeiculoById(Integer id) {
				
		try {
			Thread.sleep(4000);
			
			VeiculoVO vo = veiculoRepository.findByIdOptional(id).orElseThrow(() -> new NoSuchElementException(Constantes.MSG_SUCESSO_REGISTROS_NAO_ENCONTRADOS));
			VeiculoDTO dto = veiculoMapper.toDto(vo);
			
			return RespostaDTO.newInstance(dto, Constantes.MSG_SUCESSO_REGISTROS_ENCONTRADOS);
		} catch (NoSuchElementException e) {
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, e.getMessage());
			
		} catch (Exception e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
			throw new GlobalException(Constantes.COD_ERRO_SERVIDOR_INTERNO, Constantes.MSG_ERRO_GENERICO);
		}
	}

	public RespostaDTO<VeiculoDTO> obterVeiculos() {
		try {
			Thread.sleep(4000);
			
			List<VeiculoVO> veiculos = veiculoRepository.findAll(Sort.by("id")).list();
			
			if(veiculos.isEmpty()) {
				throw new NoSuchElementException(Constantes.MSG_SUCESSO_REGISTROS_NAO_ENCONTRADOS);
			}
			
			List<VeiculoDTO> dto = veiculos.stream().map(veiculoMapper::toDto).collect(Collectors.toList());
			
			return RespostaDTO.newInstance(dto, Constantes.MSG_SUCESSO_REGISTROS_ENCONTRADOS);
		} catch (NoSuchElementException e) {
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, e.getMessage());
			
		} catch (Exception e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
			throw new GlobalException(Constantes.COD_ERRO_SERVIDOR_INTERNO, Constantes.MSG_ERRO_GENERICO);
		}
	}

	public RespostaDTO<VeiculoDTO> obterVeiculosFiltro(VeiculoDTO filtro) {
		try {
			Thread.sleep(4000);
			
			List<VeiculoVO> veiculos = veiculoRepository.findAll(filtro);
			
			if(veiculos.isEmpty()) {
				throw new NoSuchElementException(Constantes.MSG_ERRO_NAO_ENCONTRADO);
			}
			
			List<VeiculoDTO> dtos = veiculos.stream().map(veiculoMapper::toDto).collect(Collectors.toList());
			
			return RespostaDTO.newInstance(dtos, Constantes.MSG_SUCESSO_REGISTROS_ENCONTRADOS);
		} catch (NoSuchElementException e) {
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, e.getMessage());
			
		}	catch (Exception e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
			throw new GlobalException(Constantes.COD_ERRO_SERVIDOR_INTERNO, Constantes.MSG_ERRO_GENERICO);
		}
	}

	// TODO implementar excecao de veiculo nao encontrado
	// TODO implementar validacao dos enumeradores
	public RespostaDTO<VeiculoDTO> atualizarVeiculo(VeiculoDTO filtro) {
		
		try {
			Thread.sleep(4000);
			
			VeiculoVO newVo = atualizarVeiculoBase(filtro);
			VeiculoDTO dto = veiculoMapper.toDto(newVo);
			
			return RespostaDTO.newInstance(dto, Constantes.MSG_SUCESSO_ATUALIZADO);
		} catch (NullPointerException e) {
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, Constantes.MSG_ERRO_NAO_ENCONTRADO);
			
		} catch(PropertyValueException e) { 
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_VALIDACAO_REGISTRO, Constantes.MSG_ERRO_NULL);
			
		} catch(IllegalArgumentException e) { 
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_VALIDACAO_REGISTRO, Constantes.MSG_ERRO_ID_REGISTRO);
			
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
	protected VeiculoVO atualizarVeiculoBase(VeiculoDTO filtro) {

		VeiculoVO voPersistenteId = veiculoRepository.findByIdOptional(filtro.getId()).orElseThrow(() -> new NoSuchElementException(Constantes.MSG_ERRO_NAO_ENCONTRADO));
		VeiculoVO voPersistentePlaca = veiculoRepository.findByPlaca(filtro.getPlaca());
		
		if(nonNull(voPersistentePlaca) && voPersistenteId.getId() != voPersistentePlaca.getId()) {
			throw new MyConstraintViolationException("A placa informada já existe no sistema. Verifique se o veículo está correto");
		}
		
		voPersistenteId = VeiculoVO.dtoToVo(voPersistenteId, filtro);
		veiculoRepository.persist(voPersistenteId);
		
		return voPersistenteId;
	}

	// TODO implementar validacao dos enumeradores
	public RespostaDTO<VeiculoDTO> cadastrarVeiculo(VeiculoDTO filtro) {
		
		try {
			Thread.sleep(4000);
			VeiculoVO vo = new VeiculoVO(filtro.getModelo(), filtro.getMontadora(), LocalDateTime.now(), filtro.getPlaca(), LocalDateTime.now());
			
			this.cadastrarVeiculoBase(vo);
			VeiculoDTO dto = veiculoMapper.toDto(vo);
			return RespostaDTO.newInstance(dto, Constantes.MSG_SUCESSO_CADASTRADO);
			
		} catch(NullPointerException e) {
			LOG.info(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_VALIDACAO_REGISTRO, e.getMessage());
			
		}  catch (MyConstraintViolationException e) {
			LOG.warn(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_VALIDACAO_REGISTRO, e.getMessage());
			
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
	public VeiculoVO cadastrarVeiculoBase(VeiculoVO vo) {
		VeiculoVO voPersistente = veiculoRepository.findByPlaca(vo.getPlaca());
		
		if(isNull(voPersistente)) {
			veiculoRepository.persist(vo);
		} else {
			throw new MyConstraintViolationException(Constantes.MSG_ERRO_PLACA_EXISTE);
		}
		
		return vo;
	}
	
	public String deletarVeiculo(Integer id) {
		try {
			Thread.sleep(4000);
			
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

