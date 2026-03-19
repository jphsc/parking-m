package br.com.rhscdev.application.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import br.com.rhscdev.application.dto.request.MovimentoVeiculoCriar;
import br.com.rhscdev.application.dto.request.MovimentoVeiculoEncerrar;
import br.com.rhscdev.application.dto.response.MovimentoVeiculoResponse;
import br.com.rhscdev.application.dto.response.RespostaPaginada;
import br.com.rhscdev.application.mapper.MovimentoVeiculoMapper;
import br.com.rhscdev.domain.entity.MovimentoFinanceiroVO;
import br.com.rhscdev.domain.entity.MovimentoVeiculoVO;
import br.com.rhscdev.domain.entity.RegraFinanceiraVO;
import br.com.rhscdev.domain.entity.VeiculoVO;
import br.com.rhscdev.domain.enumerator.Enums.SituacaoMovimento;
import br.com.rhscdev.domain.enumerator.Enums.TipoMovimento;
import br.com.rhscdev.infrastructure.config.Constantes;
import br.com.rhscdev.infrastructure.config.Utils;
import br.com.rhscdev.infrastructure.persistence.MovFinanceiroPanacheRepository;
import br.com.rhscdev.infrastructure.persistence.MovVeiculoPanacheRepository;
import br.com.rhscdev.infrastructure.persistence.RegraFinancPanacheRepository;
import br.com.rhscdev.infrastructure.persistence.VeiculoPanacheRepository;
import br.com.rhscdev.interfaces.rest.handler.GlobalException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class MovimentoVeiculoService {

	private VeiculoPanacheRepository vRepository;
	private RegraFinancPanacheRepository rfRepository;
	private MovVeiculoPanacheRepository mvRepository;
	private MovFinanceiroPanacheRepository mfRepository;
	private MovimentoVeiculoMapper mvMapper;
	
	public MovimentoVeiculoService(VeiculoPanacheRepository vRepository, RegraFinancPanacheRepository rfRepository, 
			MovVeiculoPanacheRepository mvRepository, MovFinanceiroPanacheRepository mfRepository, MovimentoVeiculoMapper mvMapper) {
		this.vRepository = vRepository;
		this.rfRepository = rfRepository;
		this.mvRepository = mvRepository;
		this.mfRepository = mfRepository;
		this.mvMapper = mvMapper;
	}
	
	@Transactional
	public RespostaPaginada<MovimentoVeiculoResponse> criarMovimentoVeiculo(MovimentoVeiculoCriar req) {
		
		//Thread.sleep(4000);	
		VeiculoVO veiculo = vRepository.findByIdOp(req.idVeiculo())
				.orElseThrow(() -> new GlobalException(Constantes.COD_INEXISTENTE, String.format(Constantes.MSG_ERRO_VEICULO_NAO_ENCONTRADO, req.idVeiculo())));
		
		MovimentoVeiculoVO movVeiculo = MovimentoVeiculoVO.
				criar(veiculo, req.tipoMovimento(), req.dtHrEntrada(), null, SituacaoMovimento.ABERTO.getId());
		
		movVeiculo = mvRepository.save(movVeiculo);
		
		if(req.tipoMovimento() == TipoMovimento.FINAL_SEMANA.getId() || req.tipoMovimento() == TipoMovimento.MENSALISTA.getId()) {
			Objects.requireNonNull(req.idRegra(), Constantes.MSG_REG_FIN_SEM_ID);
			
			RegraFinanceiraVO regraFinanceira = rfRepository.findByIdOp(req.idRegra())
					.orElseThrow(() -> new GlobalException(Constantes.COD_INEXISTENTE, String.format(Constantes.MSG_REG_FIN_NAO_ENCONTRADA, req.idRegra())));

			MovimentoFinanceiroVO mf = MovimentoFinanceiroVO.criar(regraFinanceira, movVeiculo, SituacaoMovimento.ABERTO.getId());
			mfRepository.persist(mf);
			movVeiculo.vincularFinanceiro(mf);
		}
		MovimentoVeiculoResponse resp = mvMapper.toResponse(movVeiculo);

		return RespostaPaginada.of(resp, null, Constantes.MSG_REGISTRO_CADASTRADO);
	}

	public RespostaPaginada<MovimentoVeiculoResponse> obterMovVeiculoById(Integer id) {
		
		MovimentoVeiculoVO vo = mvRepository.findByIdOp(id)
				.orElseThrow(() -> new GlobalException(Constantes.COD_INEXISTENTE, Constantes.MSG_REGISTROS_NAO_ENCONTRADOS));
		MovimentoVeiculoResponse dto = mvMapper.toResponse(vo);
		
		return RespostaPaginada.of(dto, null, Constantes.MSG_REGISTROS_ENCONTRADOS);
	}
	
	public RespostaPaginada<MovimentoVeiculoResponse> obterMovsVeiculo(Integer pagina) {
		
		//Thread.sleep(4000);
		int nroPagina = Utils.getNroPaginaConsulta(pagina);
		List<MovimentoVeiculoVO> movsVeiculo = mvRepository.findAll(nroPagina);
		List<MovimentoVeiculoResponse> resposta = movsVeiculo.stream().map(mvMapper::toResponse).collect(Collectors.toList());
		String mensagem = Utils.getMensagemBuscaRegistro(movsVeiculo);
		
		return RespostaPaginada.of(resposta, nroPagina, mensagem);
	}
	
	public RespostaPaginada<MovimentoVeiculoResponse> obterMovsVeiculoAberto(Integer pagina) {
		
		//Thread.sleep(4000);
		Integer nroPagina = Utils.getNroPaginaConsulta(pagina);
		List<MovimentoVeiculoResponse> movsVeiculo = mvRepository.findBySituacao(SituacaoMovimento.ABERTO.getId(), nroPagina)
				.stream().map(mvMapper::toResponse).collect(Collectors.toList());
		String mensagem = Utils.getMensagemBuscaRegistro(movsVeiculo);
		
		return RespostaPaginada.of(movsVeiculo, nroPagina, mensagem);
	}
	
	@Transactional
	public RespostaPaginada<MovimentoVeiculoResponse> encerrarMovVeiculo(MovimentoVeiculoEncerrar req) {
		
		//Thread.sleep(4000);		
		MovimentoVeiculoVO mvv = mvRepository.findByIdOp(req.idMovimento())
				.orElseThrow(() -> new GlobalException(Constantes.COD_INEXISTENTE, String.format(Constantes.MSG_MOV_VEI_NAO_ENCONTRADO, req.idMovimento())));
		
		if(mvv.isEncerrado()) {
			throw new GlobalException(Constantes.COD_VALIDACAO_REGISTRO, String.format(Constantes.MSG_MOV_VEI_JA_ENCERRADO, mvv.getId()));
		}
		
		mvv.setSituacao(SituacaoMovimento.ENCERRADO.getId());
		mvv.setDtHrSaida(req.dtHrSaida());
		
		MovimentoFinanceiroVO mvf = mvv.getMovFinanceiro();
        
        if (mvf != null) {
            mvf.setSituacao(SituacaoMovimento.ENCERRADO.getId());
            mfRepository.persist(mvf);
        } else if (req.idRegra() != null) {
            RegraFinanceiraVO regra = rfRepository.findByIdOp(req.idRegra())
                    .orElseThrow(() -> new GlobalException(Constantes.COD_INEXISTENTE, String.format(Constantes.MSG_REG_FIN_NAO_ENCONTRADA, req.idRegra())));
            
            mvf = MovimentoFinanceiroVO.criar(regra, mvv, SituacaoMovimento.ENCERRADO.getId());
            mfRepository.persist(mvf);
            mvv.vincularFinanceiro(mvf);
        }
        
        mvv = mvRepository.save(mvv);
        
        MovimentoVeiculoResponse resp = mvMapper.toResponse(mvv);
        return RespostaPaginada.of(resp, null, Constantes.MSG_MOV_VEI_ENCERRADO);

	}
}
