package br.com.rhscdeveloper.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import br.com.rhscdeveloper.bean.VeiculoBean;
import br.com.rhscdeveloper.dto.RespostaDTO;
import br.com.rhscdeveloper.dto.VeiculoReqDTO;
import br.com.rhscdeveloper.dto.VeiculoRespDTO;
import br.com.rhscdeveloper.exception.GlobalException;
import br.com.rhscdeveloper.exception.ValidacaoConstraintException;
import br.com.rhscdeveloper.mapper.VeiculoMapper;
import br.com.rhscdeveloper.model.VeiculoVO;
import br.com.rhscdeveloper.repository.VeiculoRepository;
import br.com.rhscdeveloper.util.Constantes;
import br.com.rhscdeveloper.util.Utils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class VeiculoService {

	@Inject private VeiculoRepository veiculoRepository;
	@Inject private VeiculoMapper veiculoMapper;
	
	public RespostaDTO<VeiculoRespDTO> obterVeiculoById(Integer id) {
		
		VeiculoBean bean = this.findOrThrow(veiculoRepository.findByIdOptional(id));
		VeiculoRespDTO dto = veiculoMapper.beanToDto(bean);
		
		return RespostaDTO.of(dto, null, Constantes.MSG_REGISTROS_ENCONTRADOS);
	}

	public RespostaDTO<VeiculoRespDTO> obterVeiculos(Integer pagina) {

		Integer nroPagina = Utils.getNroPaginaConsulta(pagina);
		
		List<VeiculoRespDTO> dto = veiculoRepository.findAll(nroPagina).stream()
				.map(veiculoMapper::voToBean)
				.map(veiculoMapper::beanToDto)
				.collect(Collectors.toList());
		
		String mensagem = Utils.getMensagemBuscaRegistro(dto);
		return RespostaDTO.of(dto, nroPagina, mensagem);
	}

	@Transactional
	public RespostaDTO<VeiculoRespDTO> cadastrarVeiculo(VeiculoReqDTO filtro) {
		
		veiculoRepository.findByPlaca(filtro.placa())
				.ifPresent(v -> { throw new ValidacaoConstraintException(Constantes.MSG_ERRO_PLACA_EXISTE); });
		
		VeiculoVO vo = VeiculoVO.criar(filtro.modelo(), filtro.montadora(), filtro.placa());
		veiculoRepository.persist(vo);
		 
		VeiculoRespDTO dto = veiculoMapper.voToDto(vo);
		return RespostaDTO.of(dto, null, Constantes.MSG_REGISTRO_CADASTRADO);
	}

	@Transactional
	public RespostaDTO<VeiculoRespDTO> atualizarVeiculo(VeiculoReqDTO filtro) {
		
		VeiculoVO voPersistenteId = veiculoRepository.findByIdOptional(filtro.id())
				.orElseThrow(() -> new ValidacaoConstraintException(String.format(Constantes.MSG_ERRO_VEICULO_NAO_ENCONTRADO, filtro.id())));
		
		veiculoRepository.findByPlaca(filtro.placa())
				.filter(v -> !v.getId().equals(voPersistenteId.getId()))
				.ifPresent(v -> { throw new ValidacaoConstraintException(Constantes.MSG_ERRO_PLACA_EXISTE); });
		
		voPersistenteId.atualizar(filtro.modelo(), filtro.montadora(), filtro.placa());
		VeiculoRespDTO dto = veiculoMapper.voToDto(voPersistenteId);
		
		return RespostaDTO.of(dto, null, Constantes.MSG_REGISTRO_ATUALIZADO);
	}
	
	@Transactional
	public String deletarVeiculo(Integer id) {	
		this.findOrThrow(veiculoRepository.findByIdOptional(id));
		veiculoRepository.deleteById(id);
		
		return new Gson().toJson(Constantes.MSG_REGISTRO_EXCLUIDO);
	}
	
	private VeiculoBean findOrThrow(Optional<VeiculoVO> voOptional) {
		return voOptional
				.map(veiculoMapper::voToBean)
				.orElseThrow(() -> new GlobalException(Constantes.COD_ERRO_INEXISTENTE, Constantes.MSG_REGISTROS_NAO_ENCONTRADOS));
	}
}

