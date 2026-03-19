package br.com.rhscdev.application.service;

import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import br.com.rhscdev.application.dto.request.VeiculoRequest;
import br.com.rhscdev.application.dto.response.RespostaPaginada;
import br.com.rhscdev.application.dto.response.VeiculoResponse;
import br.com.rhscdev.application.mapper.VeiculoMapper;
import br.com.rhscdev.domain.entity.VeiculoVO;
import br.com.rhscdev.infrastructure.config.Constantes;
import br.com.rhscdev.infrastructure.config.Utils;
import br.com.rhscdev.infrastructure.persistence.VeiculoPanacheRepository;
import br.com.rhscdev.interfaces.rest.handler.GlobalException;
import br.com.rhscdev.interfaces.rest.handler.ValidacaoConstraintException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class VeiculoService {

	private VeiculoPanacheRepository repository;
	private VeiculoMapper mapper;
	
	public VeiculoService(VeiculoPanacheRepository repository, VeiculoMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}
	
	public RespostaPaginada<VeiculoResponse> obterVeiculoById(Integer id) {
		
		VeiculoVO vo = this.repository.findByIdOp(id)
				.orElseThrow(() -> new GlobalException(Constantes.COD_INEXISTENTE, Constantes.MSG_REGISTROS_NAO_ENCONTRADOS));
		VeiculoResponse dto = mapper.toResponse(vo);
		
		return RespostaPaginada.of(dto, null, Constantes.MSG_REGISTROS_ENCONTRADOS);
	}

	public RespostaPaginada<VeiculoResponse> obterVeiculos(Integer pagina) {

		Integer nroPagina = Utils.getNroPaginaConsulta(pagina);
		List<VeiculoResponse> dto = repository.findAll(nroPagina).stream()
				.map(mapper::toResponse)
				.collect(Collectors.toList());
		String mensagem = Utils.getMensagemBuscaRegistro(dto);
		
		return RespostaPaginada.of(dto, nroPagina, mensagem);
	}

	@Transactional
	public RespostaPaginada<VeiculoResponse> cadastrarVeiculo(VeiculoRequest filtro) {
		
		repository.findByPlaca(filtro.placa())
				.ifPresent(v -> { throw new ValidacaoConstraintException(Constantes.MSG_ERRO_PLACA_EXISTE); });

		VeiculoVO vo = VeiculoVO.criar(filtro.modelo(), filtro.montadora(), filtro.placa());
		vo = repository.save(vo);
		VeiculoResponse dto = mapper.toResponse(vo);
		
		return RespostaPaginada.of(dto, null, Constantes.MSG_REGISTRO_CADASTRADO);
	}

	@Transactional
	public RespostaPaginada<VeiculoResponse> atualizarVeiculo(VeiculoRequest filtro) {
		
		VeiculoVO voPersistenteId = repository.findByIdOp(filtro.id())
				.orElseThrow(() -> new ValidacaoConstraintException(String.format(Constantes.MSG_ERRO_VEICULO_NAO_ENCONTRADO, filtro.id())));
		
		repository.findByPlaca(filtro.placa())
				.filter(v -> !v.getId().equals(voPersistenteId.getId()))
				.ifPresent(v -> { throw new ValidacaoConstraintException(Constantes.MSG_ERRO_PLACA_EXISTE); });
		
		voPersistenteId.atualizar(filtro.modelo(), filtro.montadora(), filtro.placa());
		VeiculoVO voAtualizado = repository.save(voPersistenteId);
		VeiculoResponse dto = mapper.toResponse(voAtualizado);
		
		return RespostaPaginada.of(dto, null, Constantes.MSG_REGISTRO_ATUALIZADO);
	}
	
	@Transactional
	public String deletarVeiculo(Integer id) {	
		
		repository.findByIdOp(id)
			.orElseThrow(() -> new GlobalException(Constantes.COD_INEXISTENTE, Constantes.MSG_REGISTROS_NAO_ENCONTRADOS));
		repository.delete(id);
		
		return new Gson().toJson(Constantes.MSG_REGISTRO_EXCLUIDO);
	}
}

