package br.com.rhscdeveloper.service;

import static java.util.Objects.nonNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;

import br.com.rhscdeveloper.dto.MovVeiculoCriarDTO;
import br.com.rhscdeveloper.dto.MovVeiculoEncerrarDTO;
import br.com.rhscdeveloper.dto.MovVeiculoRespDTO;
import br.com.rhscdeveloper.dto.RespostaDTO;
import br.com.rhscdeveloper.enumerator.Enums;
import br.com.rhscdeveloper.enumerator.Enums.SituacaoMovimento;
import br.com.rhscdeveloper.enumerator.Enums.TipoMovimento;
import br.com.rhscdeveloper.exception.GlobalException;
import br.com.rhscdeveloper.mapper.MovimentoVeiculoMapper;
import br.com.rhscdeveloper.model.MovimentoFinanceiroVO;
import br.com.rhscdeveloper.model.MovimentoFinanceiroVOId;
import br.com.rhscdeveloper.model.MovimentoVeiculoVO;
import br.com.rhscdeveloper.model.RegraFinanceiraVO;
import br.com.rhscdeveloper.model.VeiculoVO;
import br.com.rhscdeveloper.repository.MovimentoFinanceiroRepository;
import br.com.rhscdeveloper.repository.MovimentoVeiculoRepository;
import br.com.rhscdeveloper.repository.RegraFinanceiraRepository;
import br.com.rhscdeveloper.repository.VeiculoRepository;
import br.com.rhscdeveloper.util.Constantes;
import br.com.rhscdeveloper.util.Utils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class MovimentoVeiculoService {

	@Inject private VeiculoRepository vRepository;
	@Inject private RegraFinanceiraRepository rfRepository;
	@Inject private MovimentoVeiculoRepository mvRepository;
	@Inject private MovimentoFinanceiroRepository mfRepository;
	@Inject private MovimentoVeiculoMapper mvMapper;
	
	private static final Logger LOG = Logger.getLogger(GlobalException.class);
	
	@Transactional
	public RespostaDTO<MovVeiculoRespDTO> criarMovimentoVeiculo(MovVeiculoCriarDTO req) {
		
		//Thread.sleep(4000);	
		VeiculoVO veiculo = vRepository.findByIdOptional(req.idVeiculo())
				.orElseThrow(() -> new GlobalException(Constantes.COD_ERRO_INEXISTENTE, String.format(Constantes.MSG_ERRO_VEICULO_NAO_ENCONTRADO, req.idVeiculo())));
		
		MovimentoVeiculoVO movVeiculo = 
				MovimentoVeiculoVO.criar(veiculo, req.tipoMovimento(), req.dtHrEntrada(), null, SituacaoMovimento.ABERTO.getId());
		
		if(req.tipoMovimento() == TipoMovimento.FINAL_SEMANA.getId() || req.tipoMovimento() == TipoMovimento.MENSALISTA.getId()) {
			Objects.requireNonNull(req.idRegra(), Constantes.MSG_REG_FIN_SEM_ID);
			
			RegraFinanceiraVO regraFinanceira = rfRepository
					.findByIdOptional(req.idRegra()).orElseThrow(() -> new GlobalException(Constantes.COD_ERRO_INEXISTENTE, String.format(Constantes.MSG_REG_FIN_NAO_ENCONTRADA, req.idRegra())));
			
			Double valor = this.gerarValorMovimento(movVeiculo, regraFinanceira, req.dtHrEntrada());
			new MovimentoFinanceiroVO(regraFinanceira, movVeiculo, valor, SituacaoMovimento.ABERTO.getId());
		}
		
		mvRepository.persist(movVeiculo);
		MovVeiculoRespDTO resp = mvMapper.toDto(movVeiculo);
		
		return RespostaDTO.of(resp, null, Constantes.MSG_REGISTRO_CADASTRADO);
	}

	public RespostaDTO<MovVeiculoRespDTO> obterMovVeiculoById(Integer id) {
		
		MovimentoVeiculoVO vo = mvRepository.findByIdOptional(id)
				.orElseThrow(() -> new GlobalException(Constantes.COD_ERRO_INEXISTENTE, Constantes.MSG_REGISTROS_NAO_ENCONTRADOS));
		MovVeiculoRespDTO dto = mvMapper.toDto(vo);
		
		return RespostaDTO.of(dto, null, Constantes.MSG_REGISTROS_ENCONTRADOS);
	}
	
	public RespostaDTO<MovVeiculoRespDTO> obterMovsVeiculo(Integer pagina) {
		
		//Thread.sleep(4000);
		Integer nroPagina = Utils.getNroPaginaConsulta(pagina);
		List<MovimentoVeiculoVO> movsVeiculo = mvRepository.findMovsPaginado(nroPagina);
		List<MovVeiculoRespDTO> resposta = movsVeiculo.stream().map(mvMapper::toDto).collect(Collectors.toList());
		String mensagem = Utils.getMensagemBuscaRegistro(movsVeiculo);
		
		return RespostaDTO.of(resposta, nroPagina, mensagem);
	}
	
	public RespostaDTO<MovVeiculoRespDTO> obterMovsVeiculoAberto(Integer pagina) {
		
		//Thread.sleep(4000);
		Integer nroPagina = Utils.getNroPaginaConsulta(pagina);
		List<MovVeiculoRespDTO> movsVeiculo = mvRepository.findMovsAbertos(nroPagina)
				.stream().map(mvMapper::toDto).collect(Collectors.toList());
		String mensagem = Utils.getMensagemBuscaRegistro(movsVeiculo);
		
		return RespostaDTO.of(movsVeiculo, nroPagina, mensagem);
	}
	
	@Transactional
	public RespostaDTO<MovVeiculoRespDTO> encerrarMovVeiculo(MovVeiculoEncerrarDTO req) {
		
		//Thread.sleep(4000);		
		MovimentoVeiculoVO mvv = mvRepository.findByIdOptional(req.idMovimento())
				.orElseThrow(() -> new GlobalException(Constantes.COD_ERRO_INEXISTENTE, String.format(Constantes.MSG_MOV_VEI_NAO_ENCONTRADO, req.idMovimento())));
		
		if(mvv.getSituacao().equals(SituacaoMovimento.ENCERRADO.getId())) {
			throw new GlobalException(Constantes.COD_ERRO_VALIDACAO_REGISTRO, String.format(Constantes.MSG_MOV_VEI_JA_ENCERRADO, mvv.getId()));
		}

		RegraFinanceiraVO regra = null;
		
		if(nonNull(mvv.getMovFinanceiro())) {
			regra = mvv.getMovFinanceiro().getRegra();
		} else {
			Objects.requireNonNull(req.idRegra(), Constantes.MSG_REG_FIN_SEM_ID);
			
			regra = rfRepository.findByIdOptional(req.idRegra())
					.orElseThrow(() -> new GlobalException(Constantes.COD_ERRO_INEXISTENTE, String.format(Constantes.MSG_REG_FIN_NAO_ENCONTRADA, req.idRegra())));
		}
		
		Double valor = gerarValorMovimento(mvv, regra, mvv.getDtHrEntrada());
		MovimentoFinanceiroVO mvf = mfRepository
				.findById(new MovimentoFinanceiroVOId(regra.getId(), mvv.getId()));

		if(mvf == null) {
			mvf = new MovimentoFinanceiroVO(regra, mvv, valor, SituacaoMovimento.ENCERRADO.getId());
		} else {
			mvf.setSituacao(SituacaoMovimento.ENCERRADO.getId());
		}

		
		mvv.setSituacao(SituacaoMovimento.ENCERRADO.getId());
		mvv.setDtHrSaida(req.dtHrSaida());
		
		MovVeiculoRespDTO resp = mvMapper.toDto(mvv);
		return RespostaDTO.of(resp, null, Constantes.MSG_MOV_VEI_ENCERRADO);
	}
	
	/**
	 * Calcula o valor do movimento financeiro
	 * 
	 * <p>Dependendo do tipo de movimento (mensalista, final de semana, etc.), aplica regras diferentes:
	 * - Para mensalistas ou finais de semana, retorna o valor fixo da regra.
	 * - Para demais casos, calcula o valor com base no número de horas e valor da regra.
	 * 
	 * @param movimento - movimento do veículo do tipo MovimentoVeiculoVO
	 * @param regraFinanceira - regra financeira aplicável do tipo RegraFinanceiraVO
	 * @param dataHoraEntrada - Data e hora de entrada do veículo
	 * @return valor calculado do movimento do veiculo, considerando sua regra e entrada
	 *  */
	private Double gerarValorMovimento(MovimentoVeiculoVO movimento, RegraFinanceiraVO regra, LocalDateTime entrada) {
		Duration duracao = Duration.between(entrada, LocalDateTime.now());
		long dias = Duration.ofDays(duracao.toMinutes()).toDays();
		int horas = (int) Math.ceil(duracao.toMinutes()/60);
		Double valor = 0.0;
		
		try {
			TipoMovimento tipoMovimento = Enums.getEnum(TipoMovimento.class, movimento.getTipoMovimento(), Constantes.DESC_ENUM_TIPO_MOVIMENTO);
			
			switch(tipoMovimento) {
				case HORA:
					valor = regra.getValor() * horas;
					break;
				case DIA:
					valor = regra.getValor() * dias;
					break;
				default:
					valor = regra.getValor();
			}
			
			return valor;
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_SERVIDOR_INTERNO, Constantes.MSG_ERRO_GENERICO);
		}
	}
	
}
