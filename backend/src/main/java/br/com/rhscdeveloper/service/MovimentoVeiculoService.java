package br.com.rhscdeveloper.service;

import static java.util.Objects.nonNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;

import br.com.rhscdeveloper.dto.MovVeiculoReqDTO;
import br.com.rhscdeveloper.dto.MovimentoVeiculoDTO;
import br.com.rhscdeveloper.dto.RespostaDTO;
import br.com.rhscdeveloper.enumerator.Enums;
import br.com.rhscdeveloper.enumerator.Enums.SituacaoMovimento;
import br.com.rhscdeveloper.enumerator.Enums.TipoMovimento;
import br.com.rhscdeveloper.enumerator.Enums.TipoOperacao;
import br.com.rhscdeveloper.exception.GlobalException;
import br.com.rhscdeveloper.mapper.MovimentoVeiculoMapper;
import br.com.rhscdeveloper.model.BaseVO;
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
import jakarta.persistence.EntityManager;
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
	public RespostaDTO<MovimentoVeiculoDTO> criarMovimentoVeiculo(MovVeiculoReqDTO filtro) {
		
		this.validarMovimentoOperacao(filtro, TipoOperacao.CADASTRAR);
		
		//Thread.sleep(4000);	
		VeiculoVO veiculo = vRepository.findByIdOptional(filtro.idVeiculo())
				.orElseThrow(() -> new GlobalException(Constantes.COD_ERRO_INEXISTENTE, String.format(Constantes.MSG_ERRO_VEICULO_NAO_ENCONTRADO, filtro.idVeiculo())));
		
		MovimentoVeiculoVO movVeiculo = new MovimentoVeiculoVO.Builder()
				.setVeiculoVO(veiculo)
				.setTipoMovimento(filtro.tipoMovimento())
				.setDtHrEntrada(filtro.dtHrEntrada())
				.setSituacao(SituacaoMovimento.ABERTO.getId()).build();
		
		if(filtro.tipoMovimento() == TipoMovimento.FINAL_SEMANA.getId() || filtro.tipoMovimento() == TipoMovimento.MENSALISTA.getId()) {
			Objects.requireNonNull(filtro.idRegra(), Constantes.MSG_REG_FINAC_SEM_ID);
			
			RegraFinanceiraVO regraFinanceira = rfRepository
					.findByIdOptional(filtro.idRegra()).orElseThrow(() -> new GlobalException(Constantes.COD_ERRO_INEXISTENTE, String.format(Constantes.MSG_REG_NAO_ENCONTRADA, filtro.idRegra())));
			
			Double valor = this.gerarValorMovimento(movVeiculo, regraFinanceira, filtro.dtHrEntrada());
			
			new MovimentoFinanceiroVO(regraFinanceira, movVeiculo, valor, SituacaoMovimento.ABERTO.getId());
		}
		
		movVeiculo = this.salvarESincronizar(movVeiculo);
		MovimentoVeiculoDTO dto = mvMapper.toDto(movVeiculo);
		
		return RespostaDTO.of(dto, null, Constantes.MSG_REGISTRO_CADASTRADO);
	}

	public RespostaDTO<MovimentoVeiculoDTO> obterMovVeiculoById(Integer id) {
		
		MovimentoVeiculoVO vo = mvRepository.findByIdOptional(id)
				.orElseThrow(() -> new GlobalException(Constantes.COD_ERRO_INEXISTENTE, Constantes.MSG_REGISTROS_NAO_ENCONTRADOS));
		MovimentoVeiculoDTO dto = mvMapper.toDto(vo);
		
		return RespostaDTO.of(dto, null, Constantes.MSG_REGISTROS_ENCONTRADOS);
	}
	
	public RespostaDTO<MovimentoVeiculoDTO> obterMovsVeiculo(Integer pagina) {
		
		//Thread.sleep(4000);
		Integer nroPagina = Utils.getNroPaginaConsulta(pagina);
		List<MovimentoVeiculoVO> movsVeiculo = mvRepository.findMovsPaginado(nroPagina);
		List<MovimentoVeiculoDTO> resposta = movsVeiculo.stream().map(mvMapper::toDto).collect(Collectors.toList());
		String mensagem = Utils.getMensagemBuscaRegistro(movsVeiculo);
		
		return RespostaDTO.of(resposta, nroPagina, mensagem);
	}
	
	public RespostaDTO<MovimentoVeiculoDTO> obterMovsVeiculoAberto() {
		
		//Thread.sleep(4000);
		List<MovimentoVeiculoVO> movsVeiculo = mvRepository.findMovsAbertos();
		List<MovimentoVeiculoDTO> resposta = movsVeiculo.stream().map(mvMapper::toDto).collect(Collectors.toList());
		String mensagem = Utils.getMensagemBuscaRegistro(movsVeiculo);
		
		return RespostaDTO.of(resposta, null, mensagem);
	}
	
	@Transactional
	public RespostaDTO<MovimentoVeiculoDTO> encerrarMovVeiculo(MovVeiculoReqDTO filtro) {
		
		//Thread.sleep(4000);	
		this.validarMovimentoOperacao(filtro, TipoOperacao.EDITAR);
		
		MovimentoVeiculoVO mvv = mvRepository.findByIdOptional(filtro.idMovimento())
				.orElseThrow(() -> new GlobalException(Constantes.COD_ERRO_INEXISTENTE, String.format(Constantes.MSG_MOV_VEI_NAO_ENCONTRADO, filtro.idMovimento())));
		
		if(mvv.getSituacao().equals(SituacaoMovimento.ENCERRADO.getId())) {
			throw new GlobalException(Constantes.COD_ERRO_VALIDACAO_REGISTRO, String.format(Constantes.MSG_MOV_VEI_JA_ENCERRADO, mvv.getId()));
		}

		RegraFinanceiraVO regra = null;
		
		if(nonNull(mvv.getMovFinanceiro())) {
			regra = mvv.getMovFinanceiro().getRegra();
		} else {
			Objects.requireNonNull(filtro.idRegra(), "É necessário informar um id de regra financeira válido");
			
			regra = rfRepository.findByIdOptional(filtro.idRegra())
					.orElseThrow(() -> new GlobalException(Constantes.COD_ERRO_INEXISTENTE, String.format(Constantes.MSG_REG_NAO_ENCONTRADA, filtro.idRegra())));
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
		mvv.setDtHrSaida(filtro.dtHrSaida());

		salvarESincronizar(mvv); 
		
		MovimentoVeiculoDTO dto = mvMapper.toDto(mvv);
		
		return RespostaDTO.of(dto, null, Constantes.MSG_MOV_VEI_ENCERRADO);
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
	
	@Transactional
	protected <T extends BaseVO> T salvarESincronizar(T entity) {
		
		EntityManager em = mvRepository.getEntityManager();
		T resultado;
		
		if(Objects.nonNull(entity)) {
			em.persist(entity);
			resultado = entity;
		} else {
			resultado = em.merge(entity);
		}
		
		em.flush();
		em.refresh(resultado);
		
		return resultado;
	}
	
	private void validarMovimentoOperacao(MovVeiculoReqDTO dto, TipoOperacao op) {
		
		if(op.equals(TipoOperacao.CADASTRAR)) {
			Objects.requireNonNull(dto.idVeiculo(), "É necessário informar um id de veículo válido");
			Objects.requireNonNull(dto.tipoMovimento(), "É necessário informar um tipo de movimento válido");
			Objects.requireNonNull(dto.dtHrEntrada(), "É necessário informar a data e hora de entrada, no formato yyyy-MM-ddTHH:mm");
			
		} else if(op.equals(TipoOperacao.EDITAR)) {
			Objects.requireNonNull(dto.idMovimento(), "É necessário informar o id do movimento");
			Objects.requireNonNull(dto.dtHrSaida(), "É necessário informar a data e hora da saída, no formato yyyy-MM-ddTHH:mm");
		}
	}
}
