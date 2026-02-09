package br.com.rhscdeveloper.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.jboss.logging.Logger;

import br.com.rhscdeveloper.dto.ErroDTO;
import br.com.rhscdeveloper.dto.MovimentoVeiculoDTO;
import br.com.rhscdeveloper.dto.MovimentoVeiculoRespostaDTO;
import br.com.rhscdeveloper.enumerator.Enums;
import br.com.rhscdeveloper.enumerator.Enums.SituacaoMovimento;
import br.com.rhscdeveloper.enumerator.Enums.TipoMovimento;
import br.com.rhscdeveloper.enumerator.Enums.TipoOperacao;
import br.com.rhscdeveloper.exception.GlobalException;
import br.com.rhscdeveloper.model.MovimentoFinanceiroId;
import br.com.rhscdeveloper.model.MovimentoFinanceiroVO;
import br.com.rhscdeveloper.model.MovimentoVeiculoVO;
import br.com.rhscdeveloper.model.RegraFinanceiraVO;
import br.com.rhscdeveloper.model.VeiculoVO;
import br.com.rhscdeveloper.repository.MovimentoFinanceiroRepository;
import br.com.rhscdeveloper.repository.MovimentoVeiculoRepository;
import br.com.rhscdeveloper.repository.RegraFinanceiraRepository;
import br.com.rhscdeveloper.repository.VeiculoRepository;
import br.com.rhscdeveloper.util.Constantes;
import br.com.rhscdeveloper.util.GeracaoException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class MovimentoVeiculoService {

	@Inject VeiculoRepository vRepository;
	@Inject RegraFinanceiraRepository rfRepository;
	@Inject MovimentoVeiculoRepository mvRepository;
	@Inject MovimentoFinanceiroRepository mfRepository;
	
	private static final Logger LOG = Logger.getLogger(GlobalException.class);
	
	public MovimentoVeiculoRespostaDTO criarMovimentoVeiculo(MovimentoVeiculoDTO dto) {
		
		try {
			//Thread.sleep(4000);	
			VeiculoVO veiculo = (VeiculoVO) vRepository.findByIdOptional(dto.getIdVeiculo()).orElseThrow(() -> new NullPointerException("Veículo de id "+dto.getIdVeiculo()+" não encontrado"));
			RegraFinanceiraVO regraFinanceira = (RegraFinanceiraVO) rfRepository.findByIdOptional(dto.getIdRegra()).orElseThrow(() -> new NullPointerException("Regra de id "+dto.getIdRegra()+" não encontrada"));;
			Double valor = 0.0;
			
			TipoMovimento tipoMovimento = (TipoMovimento) (Enums.getEnum(dto.getTipoMovimento()));
			SituacaoMovimento sitMovFinanceiro = (tipoMovimento == TipoMovimento.FINAL_SEMANA || tipoMovimento == TipoMovimento.MENSALISTA) 
					? SituacaoMovimento.ENCERRADO 
					: SituacaoMovimento.ABERTO ;
		
			MovimentoVeiculoVO movVeiculo = new MovimentoVeiculoVO.Builder()
					.setVeiculoVO(veiculo)
					.setTipoMovimento(tipoMovimento.getId())
					.setDtHrEntrada(dto.getDtHrEntrada())
					.setSituacao(SituacaoMovimento.ABERTO.getId()) // mv sempre nasce aberto | financeiro depende do tpMov
					.setVersao(LocalDateTime.now()).build();
			movVeiculo = this.salvarMovimento(movVeiculo);
			
			if(tipoMovimento == TipoMovimento.FINAL_SEMANA || tipoMovimento == TipoMovimento.MENSALISTA) {
				valor = this.gerarValorMovimento(movVeiculo, regraFinanceira, dto.getDtHrEntrada());
				
				MovimentoFinanceiroVO movFinanceiro = new MovimentoFinanceiroVO(regraFinanceira, movVeiculo, valor, sitMovFinanceiro , LocalDateTime.now());
				movFinanceiro = (MovimentoFinanceiroVO) this.salvarFinanceiro(movFinanceiro);
			}
			return MovimentoVeiculoRespostaDTO.newInstance(Arrays.asList(movVeiculo), TipoOperacao.CADASTRAR);
			
		} catch (NullPointerException e) {
			LOG.warn(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, e.getMessage());
			
		} catch (Exception e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
			ErroDTO erro = GeracaoException.mensagemExceptionGenerica(e);
			throw new GlobalException(erro.getCodigo(), erro.getMensagem());
		}
	}

	public MovimentoVeiculoRespostaDTO obterMovVeiculoById(Integer id) {
		
		try {
			//Thread.sleep(4000);	
			MovimentoVeiculoVO vo = mvRepository.findById(id);
			
			MovimentoVeiculoRespostaDTO resposta = MovimentoVeiculoRespostaDTO.newInstance(Arrays.asList(vo), TipoOperacao.CONSULTAR);
			obterSetarRegraFinanceiraMovVeiculo(Arrays.asList(vo), resposta);
			
			return resposta;
		} catch (NullPointerException e) {
			LOG.warn(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, Constantes.MSG_ERRO_NAO_ENCONTRADO);
			
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
			ErroDTO erro = GeracaoException.mensagemExceptionGenerica(e);
			throw new GlobalException(erro.getCodigo(), erro.getMensagem());
		}
	}
	
	public MovimentoVeiculoRespostaDTO obterMovsVeiculo() {
		try {
			//Thread.sleep(4000);	
			List<MovimentoVeiculoVO> movsVeiculo = mvRepository.findAll().list();
			
			MovimentoVeiculoRespostaDTO resposta = MovimentoVeiculoRespostaDTO.newInstance(movsVeiculo, TipoOperacao.CONSULTAR);
			
			obterSetarRegraFinanceiraMovVeiculo(movsVeiculo, resposta);
			
			return resposta;
		} catch (NullPointerException e) {
			LOG.warn(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, Constantes.MSG_ERRO_NAO_ENCONTRADO);
			
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
			ErroDTO erro = GeracaoException.mensagemExceptionGenerica(e);
			throw new GlobalException(erro.getCodigo(), erro.getMensagem());
		}
	}
	
	public MovimentoVeiculoRespostaDTO obterMovsVeiculoAbertos(MovimentoVeiculoDTO dto) {
		try {
			//Thread.sleep(4000);
			List<MovimentoVeiculoVO> movsVeiculo = mvRepository.findAll(dto);
			MovimentoVeiculoRespostaDTO resposta = MovimentoVeiculoRespostaDTO.newInstance(movsVeiculo, TipoOperacao.CONSULTAR);
			
			obterSetarRegraFinanceiraMovVeiculo(movsVeiculo, resposta);
			
			return resposta;
		} catch (NullPointerException e) {
			LOG.warn(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_INEXISTENTE, Constantes.MSG_ERRO_NAO_ENCONTRADO);
			
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
			ErroDTO erro = GeracaoException.mensagemExceptionGenerica(e);
			throw new GlobalException(erro.getCodigo(), erro.getMensagem());
		}
	}
	
	public MovimentoVeiculoRespostaDTO encerrarMovVeiculo(MovimentoVeiculoDTO dto) {
		
		try {
			//Thread.sleep(4000);	
			MovimentoVeiculoVO movVeiculo = (MovimentoVeiculoVO) mvRepository.findByIdOptional(dto.getIdMovimento()).orElseThrow(() -> new NullPointerException("Movimento de id "+dto.getIdMovimento()+" não encontrado"));
			
			if(movVeiculo.getSituacao().equals(SituacaoMovimento.ENCERRADO.getId())) {
				throw new GlobalException(Constantes.COD_ERRO_VALIDACAO_REGISTRO, "O movimento informado já está finalizado");
			}
			
			RegraFinanceiraVO regra = (RegraFinanceiraVO) rfRepository.findByIdOptional(dto.getIdRegra()).orElseThrow(() -> new NullPointerException("Regra de id não "+dto.getIdRegra()+" encontrada"));;
			MovimentoFinanceiroVO mf = mfRepository.findByIdOptional(new MovimentoFinanceiroId(regra, movVeiculo)).orElse(null);
			Double valor = gerarValorMovimento(movVeiculo, regra, dto.getDtHrEntrada());
			
			if(mf == null) {
				mf = new MovimentoFinanceiroVO(regra, movVeiculo, valor, SituacaoMovimento.ENCERRADO, LocalDateTime.now());
				this.salvarFinanceiro(mf);
			} else {
				mf.setValor(valor);
				mf.setSituacao(SituacaoMovimento.ENCERRADO);
				mf.setVersao(LocalDateTime.now());
			}
			
			movVeiculo.setSituacao(SituacaoMovimento.ENCERRADO);
			movVeiculo.setDtHrSaida(dto.getDtHrSaida());
			this.salvarMovimento(movVeiculo);
			
			MovimentoVeiculoRespostaDTO resposta = MovimentoVeiculoRespostaDTO.newInstance(Arrays.asList(movVeiculo), TipoOperacao.EDITAR);
			return resposta;
		} catch(IllegalArgumentException e) {
			LOG.warn(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_VALIDACAO_REGISTRO, "É necessário informar o identificador do movimento!");
			
		}  catch(NullPointerException e) {
			LOG.warn(e.getMessage());
			throw new GlobalException(Constantes.COD_ERRO_VALIDACAO_REGISTRO, e.getMessage());
			
		} catch (Exception e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
			ErroDTO erro = GeracaoException.mensagemExceptionGenerica(e);
			throw new GlobalException(erro.getCodigo(), erro.getMensagem());
		}
		
	}
	
	@Transactional
	public MovimentoVeiculoVO salvarMovimento(MovimentoVeiculoVO vo) {
	    if(vo.getId() == null) {
	        mvRepository.persist(vo);
	    } else {
	        vo = mvRepository.getEntityManager().merge(vo);
	    }
	    return vo;
	}

	@Transactional
	public MovimentoFinanceiroVO salvarFinanceiro(MovimentoFinanceiroVO vo) {
	    if(vo.getMovimento() == null) {
	        mfRepository.persist(vo);
	    } else {
	        vo = mfRepository.getEntityManager().merge(vo);
	    }
	    return vo;
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
			TipoMovimento tipoMovimento = (TipoMovimento) (Enums.getEnum(movimento.getTipoMovimento()));
			
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
			ErroDTO erro = GeracaoException.mensagemExceptionGenerica(e);
			throw new GlobalException(erro.getCodigo(), erro.getMensagem());
		}
	}
	
	private void obterSetarRegraFinanceiraMovVeiculo(List<MovimentoVeiculoVO> movsVeiculo, MovimentoVeiculoRespostaDTO resposta) {
		for(MovimentoVeiculoVO mvv : movsVeiculo) {
			
			try {
				MovimentoFinanceiroVO mvf = mvRepository.getMovFinanceiro(mvv);
				
				if(!Objects.isNull(mvf)) {
					resposta.getRegistros().stream().filter(mvvPers -> mvvPers.getIdMovimento().equals(mvf.getMovimento().getId())).forEach(mvvP -> mvvP.setIdRegra(mvf.getRegra().getId()));
				}
				
			} catch (NullPointerException e) {
				LOG.info(e.getMessage());
				throw new GlobalException(Constantes.COD_ERRO_VALIDACAO_REGISTRO, e.getMessage());
				
			} catch (Exception e) {
				LOG.error(e.getMessage());
				e.printStackTrace();
				ErroDTO erro = GeracaoException.mensagemExceptionGenerica(e);
				throw new GlobalException(erro.getCodigo(), erro.getMensagem());
			}
		}
	}
}
