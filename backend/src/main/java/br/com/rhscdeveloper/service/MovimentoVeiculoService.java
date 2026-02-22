package br.com.rhscdeveloper.service;

import static java.util.Objects.nonNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;

import br.com.rhscdeveloper.dto.MovimentoVeiculoDTO;
import br.com.rhscdeveloper.dto.MovimentoVeiculoFiltroDTO;
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
	public RespostaDTO<MovimentoVeiculoDTO> criarMovimentoVeiculo(MovimentoVeiculoDTO filtro) {
		
		try {
			
			this.validarMovimentoOperacao(filtro, TipoOperacao.CADASTRAR);
			
			Enums.getEnum(TipoMovimento.class, filtro.getTipoMovimento(), Constantes.DESC_ENUM_TIPO_MOVIMENTO);
			
			//Thread.sleep(4000);	
			VeiculoVO veiculo = vRepository.findByIdOptional(filtro.getIdVeiculo()).orElseThrow(() -> new NullPointerException("Veículo de id "+filtro.getIdVeiculo()+" não encontrado"));
			
			MovimentoVeiculoVO movVeiculo = new MovimentoVeiculoVO.Builder()
					.setVeiculoVO(veiculo)
					.setTipoMovimento(filtro.getTipoMovimento())
					.setDtHrEntrada(filtro.getDtHrEntrada())
					.setSituacao(SituacaoMovimento.ABERTO.getId()).build();
			
			if(filtro.getTipoMovimento() == TipoMovimento.FINAL_SEMANA.getId() || filtro.getTipoMovimento() == TipoMovimento.MENSALISTA.getId()) {
				Objects.requireNonNull(filtro.getIdRegra(), "É necessário informar o id da regra financeira");
				
				RegraFinanceiraVO regraFinanceira = rfRepository
						.findByIdOptional(filtro.getIdRegra()).orElseThrow(() -> new NullPointerException("Regra de id "+filtro.getIdRegra()+" não encontrada"));;
				
				Double valor = this.gerarValorMovimento(movVeiculo, regraFinanceira, filtro.getDtHrEntrada());
				
				new MovimentoFinanceiroVO(regraFinanceira, movVeiculo, valor, SituacaoMovimento.ABERTO.getId());
			}
			movVeiculo = salvarESincronizar(movVeiculo);
			
			MovimentoVeiculoDTO dto = mvMapper.toDto(movVeiculo);
			
			return RespostaDTO.of(dto, null, Constantes.MSG_SUCESSO_CADASTRADO);
			
		} catch (NullPointerException e) {
			LOG.warn(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, e.getMessage());
			
		} catch (NoSuchElementException e) {
			LOG.warn(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, e.getMessage());
			
		} catch (Exception e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
			throw new GlobalException(Constantes.COD_ERRO_SERVIDOR_INTERNO, Constantes.MSG_ERRO_GENERICO);
		}
	}

	public RespostaDTO<MovimentoVeiculoDTO> obterMovVeiculoById(Integer id) {
		
		try {
			//Thread.sleep(4000);	
			MovimentoVeiculoVO vo = mvRepository.findById(id);
			MovimentoVeiculoDTO dto = mvMapper.toDto(vo);
			
			return RespostaDTO.of(dto, null, Constantes.MSG_SUCESSO_REGISTROS_ENCONTRADOS);
		} catch (NullPointerException e) {
			LOG.warn(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, Constantes.MSG_REGISTROS_NAO_ENCONTRADOS);
			
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
			throw new GlobalException(Constantes.COD_ERRO_SERVIDOR_INTERNO, Constantes.MSG_ERRO_GENERICO);
		}
	}
	
	public RespostaDTO<MovimentoVeiculoDTO> obterMovsVeiculo(Integer pagina) {
		try {
			//Thread.sleep(4000);
			
			Integer nroPagina = pagina >= 1  ? pagina - 1 : 0;
			List<MovimentoVeiculoVO> movsVeiculo = mvRepository.findAll().list();
			List<MovimentoVeiculoDTO> resposta = movsVeiculo.stream().map(mvMapper::toDto).collect(Collectors.toList());
			
			return RespostaDTO.of(resposta, nroPagina, Constantes.MSG_SUCESSO_REGISTROS_ENCONTRADOS);
		} catch (NullPointerException e) {
			LOG.warn(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, Constantes.MSG_REGISTROS_NAO_ENCONTRADOS);
			
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
			throw new GlobalException(Constantes.COD_ERRO_SERVIDOR_INTERNO, Constantes.MSG_ERRO_GENERICO);
		}
	}
	
	public RespostaDTO<MovimentoVeiculoDTO> obterMovsVeiculoAbertos(MovimentoVeiculoFiltroDTO filtro) {
		try {
			//Thread.sleep(4000);
			List<MovimentoVeiculoVO> movsVeiculo = mvRepository.findAll(filtro);
			List<MovimentoVeiculoDTO> resposta = movsVeiculo.stream().map(mvMapper::toDto).collect(Collectors.toList());
			
			return RespostaDTO.of(resposta, null, Constantes.MSG_SUCESSO_REGISTROS_ENCONTRADOS);
		} catch (NullPointerException e) {
			LOG.warn(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, Constantes.MSG_REGISTROS_NAO_ENCONTRADOS);
			
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
			throw new GlobalException(Constantes.COD_ERRO_SERVIDOR_INTERNO, Constantes.MSG_ERRO_GENERICO);
		}
	}
	
	@Transactional
	public RespostaDTO<MovimentoVeiculoDTO> encerrarMovVeiculo(MovimentoVeiculoDTO filtro) {
		
		try {

			//Thread.sleep(4000);	
			this.validarMovimentoOperacao(filtro, TipoOperacao.EDITAR);
			
			MovimentoVeiculoVO mvv = mvRepository
					.findByIdOptional(filtro.getIdMovimento()).orElseThrow(() -> new NoSuchElementException("Movimento de id "+filtro.getIdMovimento()+" não encontrado"));
			
			if(mvv.getSituacao().equals(SituacaoMovimento.ENCERRADO.getId())) {
				throw new IllegalStateException("O movimento informado já está finalizado");
			}
			
			RegraFinanceiraVO regra = nonNull(mvv.getMovFinanceiro()) ? mvv.getMovFinanceiro().getRegra() : rfRepository
				.findByIdOptional(filtro.getIdRegra()).orElseThrow(() -> new NoSuchElementException("Regra de id não "+filtro.getIdRegra()+" encontrada"));
			
//			RegraFinanceiraVO regra = rfRepository
//					.findByIdOptional(filtro.getIdRegra()).orElseThrow(() -> new NoSuchElementException("Regra de id não "+filtro.getIdRegra()+" encontrada"));
			Double valor = gerarValorMovimento(mvv, regra, mvv.getDtHrEntrada());
			MovimentoFinanceiroVO mvf = mfRepository
					.findById(new MovimentoFinanceiroVOId(regra.getId(), mvv.getId()));

			if(mvf == null) {
				mvf = new MovimentoFinanceiroVO(regra, mvv, valor, SituacaoMovimento.ENCERRADO.getId());
			} 

			
			mvv.setSituacao(SituacaoMovimento.ENCERRADO.getId());
			mvv.setDtHrSaida(filtro.getDtHrSaida());

			salvarESincronizar(mvv); 
			
			MovimentoVeiculoDTO dto = mvMapper.toDto(mvv);
			
			return RespostaDTO.of(dto, null, Constantes.MSG_SUCESSO_MOV_ENCERRADO);
		} catch(IllegalArgumentException e) {
			LOG.warn(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_VALIDACAO_REGISTRO, "É necessário informar o identificador do movimento!");
			
		} catch(NoSuchElementException e) {
			LOG.warn(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_VALIDACAO_REGISTRO, e.getMessage());
			
		} catch(IllegalStateException e) {
			LOG.warn(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_VALIDACAO_REGISTRO, e.getMessage());
			
		} catch(NullPointerException e) {
			LOG.warn(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_VALIDACAO_REGISTRO, Constantes.MSG_ERRO_CAMPOS);
			
		} catch (Exception e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
			throw new GlobalException(Constantes.COD_ERRO_SERVIDOR_INTERNO, Constantes.MSG_ERRO_GENERICO);
		}
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
	
	private void validarMovimentoOperacao(MovimentoVeiculoDTO dto, TipoOperacao op) {
		
		if(op.equals(TipoOperacao.CADASTRAR)) {
			Objects.requireNonNull(dto.getIdVeiculo(), "É necessário informar um id de veículo válido");
			Objects.requireNonNull(dto.getTipoMovimento(), "É necessário informar um tipo de movimento válido");
			Objects.requireNonNull(dto.getDtHrEntrada(), "É necessário informar a data e hora de entrada, no formato yyyy-MM-ddTHH:mm");
			
		} else if(op.equals(TipoOperacao.EDITAR)) {
			Objects.requireNonNull(dto.getIdMovimento(), "É necessário informar o id do movimento");
			Objects.requireNonNull(dto.getDtHrSaida(), "É necessário informar a data e hora da saída, no formato yyyy-MM-ddTHH:mm");
		}
	}
}
