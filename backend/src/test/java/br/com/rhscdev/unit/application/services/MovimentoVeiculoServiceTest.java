package br.com.rhscdev.unit.application.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.rhscdev.application.dto.request.MovimentoVeiculoCriar;
import br.com.rhscdev.application.dto.request.MovimentoVeiculoEncerrar;
import br.com.rhscdev.application.dto.response.DataQueryResult;
import br.com.rhscdev.application.dto.response.MovimentoVeiculoResponse;
import br.com.rhscdev.application.mapper.MovimentoVeiculoMapper;
import br.com.rhscdev.application.service.MovimentoVeiculoService;
import br.com.rhscdev.domain.entity.MovimentoFinanceiroVO;
import br.com.rhscdev.domain.entity.MovimentoVeiculoVO;
import br.com.rhscdev.domain.entity.RegraFinanceiraVO;
import br.com.rhscdev.domain.entity.VeiculoVO;
import br.com.rhscdev.domain.enumerator.Enums.SituacaoMovimento;
import br.com.rhscdev.domain.enumerator.Enums.TipoMovimento;
import br.com.rhscdev.infrastructure.config.Constantes;
import br.com.rhscdev.infrastructure.persistence.MovFinanceiroPanacheRepository;
import br.com.rhscdev.infrastructure.persistence.MovVeiculoPanacheRepository;
import br.com.rhscdev.infrastructure.persistence.RegraFinancPanacheRepository;
import br.com.rhscdev.infrastructure.persistence.VeiculoPanacheRepository;
import br.com.rhscdev.interfaces.rest.handler.GlobalException;

@ExtendWith(MockitoExtension.class)
class MovimentoVeiculoServiceTest {

    @Mock
    VeiculoPanacheRepository vRepository;
    
    @Mock
    RegraFinancPanacheRepository rfRepository;
    
    @Mock
    MovVeiculoPanacheRepository mvRepository;
    
    @Mock
    MovFinanceiroPanacheRepository mfRepository;
    
    @Mock
    MovimentoVeiculoMapper mapper;

    @InjectMocks
    MovimentoVeiculoService service;

    private VeiculoVO veiculo;
    private MovimentoVeiculoVO movimento;
    private MovimentoVeiculoResponse response;

    private final LocalDateTime DATA_FIXA = LocalDateTime.of(2026, 3, 28, 10, 0);

    @BeforeEach
    void setup() {
        veiculo = VeiculoVO.criar("HB20", "HYUNDAI", "ABC1234");
        veiculo.setId(1);

        movimento = MovimentoVeiculoVO.criar(
                veiculo,
                TipoMovimento.HORA.getId(),
                DATA_FIXA,
                null,
                SituacaoMovimento.ABERTO.getId());
        movimento.setId(1);

        response = new MovimentoVeiculoResponse();
    }

    @Test
    @DisplayName("Deve criar movimento veículo sem financeiro com sucesso")
    void deveCriarMovimentoSemFinanceiro() {
    	MovimentoVeiculoCriar req = new MovimentoVeiculoCriar(1, null, TipoMovimento.HORA.getId(), DATA_FIXA);

        when(vRepository.findByIdOp(1)).thenReturn(Optional.of(veiculo));
        when(mvRepository.save(any())).thenReturn(movimento);
        when(mapper.toResponse(any())).thenReturn(response);

        var result = service.criarMovimentoVeiculo(req);

        assertThat(result.getRegistros()).hasSize(1);
        assertThat(result.getMensagem()).isEqualTo(Constantes.MSG_REGISTRO_CADASTRADO);

        verify(mfRepository, never()).persist(any(MovimentoFinanceiroVO.class));
    }

    @Test
    @DisplayName("Deve criar movimento veículo com financeiro com sucesso")
    void deveCriarMovimentoComFinanceiro() {
    	RegraFinanceiraVO regra = mock(RegraFinanceiraVO.class);
        MovimentoVeiculoCriar req = new MovimentoVeiculoCriar(1, 10, TipoMovimento.MENSALISTA.getId(), DATA_FIXA);
        
        when(regra.getId()).thenReturn(10);
        when(regra.getValor()).thenReturn(BigDecimal.TEN);
        when(vRepository.findByIdOp(1)).thenReturn(Optional.of(veiculo));
        when(rfRepository.findByIdOp(10)).thenReturn(Optional.of(regra));
        when(mvRepository.save(any())).thenReturn(movimento);
        when(mapper.toResponse(any())).thenReturn(response);

        var result = service.criarMovimentoVeiculo(req);

        assertThat(result.getRegistros()).hasSize(1);

        verify(mfRepository).persist(any(MovimentoFinanceiroVO.class));
    }

    @Test
    @DisplayName("Deve lançar excecao ao criar movimento veiculo com veiculo inexistente")
    void deveLancarErroQuandoVeiculoNaoExiste() {
        MovimentoVeiculoCriar req = new MovimentoVeiculoCriar(1, null, TipoMovimento.HORA.getId(), DATA_FIXA);
        
        when(vRepository.findByIdOp(1)).thenReturn(Optional.empty());

        assertThatExceptionOfType(GlobalException.class)
                .isThrownBy(() -> service.criarMovimentoVeiculo(req));
    }

    @Test
    @DisplayName("Deve lançar excecao ao criar movimento veiculo quando regra obrigatoria nao informada")
    void deveLancarErroQuandoRegraObrigatoriaNaoInformada() {
    	MovimentoVeiculoCriar req = new MovimentoVeiculoCriar(1, null, TipoMovimento.MENSALISTA.getId(), DATA_FIXA);

        when(vRepository.findByIdOp(1)).thenReturn(Optional.of(veiculo));
        when(mvRepository.save(any())).thenReturn(movimento);

        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> service.criarMovimentoVeiculo(req));
    }

    @Test
    @DisplayName("Deve lançar excecao ao criar movimento veiculo quando regra informada nao existe")
    void deveLancarErroQuandoRegraNaoExiste() {
    	MovimentoVeiculoCriar req = new MovimentoVeiculoCriar(1, 10, TipoMovimento.MENSALISTA.getId(), DATA_FIXA);

        when(vRepository.findByIdOp(1)).thenReturn(Optional.of(veiculo));
        when(rfRepository.findByIdOp(10)).thenReturn(Optional.empty());
        when(mvRepository.save(any())).thenReturn(movimento);

        assertThatExceptionOfType(GlobalException.class)
                .isThrownBy(() -> service.criarMovimentoVeiculo(req));
    }

    @Test
    @DisplayName("Encerra movimento com financeiro existente com sucesso")
    void deveEncerrarMovimentoComFinanceiroExistente() {
        MovimentoFinanceiroVO financeiro = mock(MovimentoFinanceiroVO.class);
        MovimentoVeiculoEncerrar req = new MovimentoVeiculoEncerrar(1, null, DATA_FIXA.plusHours(2));
        movimento.vincularFinanceiro(financeiro);

        when(mvRepository.findByIdOp(1)).thenReturn(Optional.of(movimento));
        when(mvRepository.save(any())).thenReturn(movimento);
        when(mapper.toResponse(any())).thenReturn(response);

        var result = service.encerrarMovVeiculo(req);

        assertThat(result.getMensagem()).isEqualTo(Constantes.MSG_MOV_VEI_ENCERRADO);

        verify(mfRepository).persist(financeiro);
    }

    @Test
    @DisplayName("Encerra movimento sem financeiro existente com sucesso")
    void deveEncerrarMovimentoCriandoFinanceiroQuandoNaoExiste() {
    	RegraFinanceiraVO regra = mock(RegraFinanceiraVO.class);
    	MovimentoVeiculoEncerrar req = new MovimentoVeiculoEncerrar(1, 10, DATA_FIXA.plusHours(2));
    	
        when(regra.getId()).thenReturn(10);
        when(regra.getValor()).thenReturn(BigDecimal.TEN);
        when(mvRepository.findByIdOp(1)).thenReturn(Optional.of(movimento));
        when(rfRepository.findByIdOp(10)).thenReturn(Optional.of(regra));
        when(mvRepository.save(any())).thenReturn(movimento);
        when(mapper.toResponse(any())).thenReturn(response);

        var result = service.encerrarMovVeiculo(req);

        assertThat(result.getMensagem()).isEqualTo(Constantes.MSG_MOV_VEI_ENCERRADO);

        verify(mfRepository).persist(any(MovimentoFinanceiroVO.class));
    }

    @Test
    @DisplayName("Deve lancar excecao ao encerrar movimento ja encerrado")
    void deveLancarErroAoEncerrarJaEncerrado() {
        MovimentoVeiculoEncerrar req = new MovimentoVeiculoEncerrar(1, null, DATA_FIXA.plusHours(2));
        movimento.setSituacao(SituacaoMovimento.ENCERRADO.getId());

        when(mvRepository.findByIdOp(1)).thenReturn(Optional.of(movimento));

        assertThatExceptionOfType(GlobalException.class)
                .isThrownBy(() -> service.encerrarMovVeiculo(req));
    }

    @Test
    @DisplayName("Deve lancar excecao ao encerrar movimento inexistente")
    void deveLancarErroAoEncerrarMovimentoInexistente() {
        MovimentoVeiculoEncerrar req = new MovimentoVeiculoEncerrar(1, null, DATA_FIXA.plusHours(2));
        
        when(mvRepository.findByIdOp(1)).thenReturn(Optional.empty());

        assertThatExceptionOfType(GlobalException.class)
                .isThrownBy(() -> service.encerrarMovVeiculo(req));
    }

    @Test
    @DisplayName("Busca movimento por id com sucesso")
    void deveBuscarMovimentoPorId() {
        when(mvRepository.findByIdOp(1)).thenReturn(Optional.of(movimento));
        when(mapper.toResponse(any())).thenReturn(response);

        var result = service.obterMovVeiculoById(1);

        assertThat(result.getRegistros()).hasSize(1);
    }

    @Test
    @DisplayName("Lista movimentos com sucesso")
    void deveListarMovimentos() {
        when(mvRepository.findAll(anyInt())).thenReturn(new DataQueryResult<MovimentoVeiculoVO>(List.of(movimento), 1L));
        when(mapper.toResponse(any())).thenReturn(response);

        var result = service.obterMovsVeiculo(1);

        assertThat(result.getRegistros()).hasSize(1);
    }

    @Test
    @DisplayName("Lista movimentos abertos com sucesso")
    void deveListarMovimentosAbertos() {
        when(mvRepository.findBySituacao(anyInt(), anyInt(), anyInt())).thenReturn(new DataQueryResult<MovimentoVeiculoVO>(List.of(movimento), 1L));
        when(mapper.toResponse(any())).thenReturn(response);

        var result = service.obterMovsVeiculoAberto(1, 1);

        assertThat(result.getRegistros()).hasSize(1);
    }
}