package br.com.rhscdeveloper.service;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import br.com.rhscdeveloper.dto.RespostaDTO;
import br.com.rhscdeveloper.dto.VeiculoRequestDTO;
import br.com.rhscdeveloper.dto.VeiculoResponseDTO;
import br.com.rhscdeveloper.enumerator.Enums.TipoOperacao;
import br.com.rhscdeveloper.exception.ValidacaoConstraintException;
import br.com.rhscdeveloper.mapper.VeiculoMapper;
import br.com.rhscdeveloper.model.VeiculoVO;
import br.com.rhscdeveloper.repository.VeiculoRepository;
import br.com.rhscdeveloper.util.Constantes;
import br.com.rhscdeveloper.util.Utils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class VeiculoService {

	@Inject private VeiculoRepository veiculoRepository;
	@Inject private VeiculoMapper veiculoMapper;
	
	public RespostaDTO<VeiculoResponseDTO> obterVeiculoById(Integer id) {
		
		VeiculoVO vo = veiculoRepository.findByIdOptional(id).orElse(null);
		VeiculoResponseDTO dto = veiculoMapper.voToDto(vo);
		
		String mensagem = Utils.getMensagemBuscaRegistro(vo);
		Integer pagina = Utils.getNroPaginaResp(null);
		
		return RespostaDTO.of(dto, pagina, mensagem);
	}

	public RespostaDTO<VeiculoResponseDTO> obterVeiculos(Integer pagina) {

		Integer nroPagina = Utils.getNroPagina(pagina);
		List<VeiculoVO> veiculos = veiculoRepository.findAll(nroPagina);
		
		if(veiculos.isEmpty()) {
			throw new NoSuchElementException(Constantes.MSG_REGISTROS_NAO_ENCONTRADOS);
		}
		
		String mensagem = veiculos.isEmpty() ? Constantes.MSG_REGISTROS_NAO_ENCONTRADOS : Constantes.MSG_REGISTROS_NAO_ENCONTRADOS;
		
		List<VeiculoResponseDTO> dto = veiculos.stream().map(veiculoMapper::voToDto).collect(Collectors.toList());
		return RespostaDTO.of(dto, Utils.getNroPaginaResp(nroPagina), mensagem);
	}

	public RespostaDTO<VeiculoResponseDTO> atualizarVeiculo(VeiculoRequestDTO dtoRequest) {
		
		validarVeiculoOperacao(dtoRequest, TipoOperacao.EDITAR);
		
		VeiculoVO voPersistenteId = veiculoRepository.findByIdOptional(dtoRequest.id()).orElse(null);
		VeiculoVO voPersistentePlaca = veiculoRepository.findByPlaca(dtoRequest.placa());
		
		if(isNull(voPersistenteId)) {
			throw new ValidacaoConstraintException(String.format(Constantes.MSG_ERRO_VEICULO_NAO_ENCONTRADO, dtoRequest.id()));
			
		} else if(nonNull(voPersistentePlaca) && !voPersistentePlaca.getId().equals(voPersistenteId.getId())) {
			throw new ValidacaoConstraintException(Constantes.MSG_ERRO_PLACA_EXISTE);
		}
		VeiculoResponseDTO dto = veiculoMapper.recordToDto(dtoRequest);
		veiculoMapper.updateVoFromDto(dto, voPersistenteId);
		voPersistenteId = criarAtualizarVeiculoBase(voPersistenteId);
		
		dto = veiculoMapper.voToDto(voPersistenteId);
		
		return RespostaDTO.of(dto, null, Constantes.MSG_SUCESSO_ATUALIZADO);
	}

	public RespostaDTO<VeiculoResponseDTO> cadastrarVeiculo(VeiculoRequestDTO filtro) {
		
		VeiculoVO vo = veiculoRepository.findByPlaca(filtro.placa());
		
		if(nonNull(vo)) {
			throw new ValidacaoConstraintException(Constantes.MSG_ERRO_PLACA_EXISTE);
		}
		
		vo = new VeiculoVO(filtro.modelo(), filtro.montadora(), LocalDate.now(), filtro.placa());
		vo = criarAtualizarVeiculoBase(vo);
		 
		VeiculoResponseDTO dto = veiculoMapper.voToDto(vo);
		return RespostaDTO.of(dto, Utils.getNroPaginaResp(null), Constantes.MSG_SUCESSO_CADASTRADO);
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
	
	@Transactional
	public String deletarVeiculo(Integer id) {
		
		veiculoRepository.findByIdOptional(id).orElseThrow(() -> new NoSuchElementException(Constantes.MSG_REGISTROS_NAO_ENCONTRADOS));
		veiculoRepository.deleteById(id);
		return new Gson().toJson(Constantes.MSG_SUCESSO_REGISTRO_EXCLUIDO);
	}
	
	private void validarVeiculoOperacao(VeiculoRequestDTO dto, TipoOperacao op) {
		
		if(op.equals(TipoOperacao.CADASTRAR)) {
			Objects.requireNonNull(dto.placa(), Constantes.MSG_SEM_PLACA);
			Objects.requireNonNull(dto.modelo(), Constantes.MSG_SEM_MODELO);
			Objects.requireNonNull(dto.montadora(), Constantes.MSG_SEM_MONTADORA);
			
		} else if(op.equals(TipoOperacao.EDITAR)) {
			Objects.requireNonNull(dto.id(), Constantes.MSG_SEM_ID_REGISTRO);
		}
	}
}

