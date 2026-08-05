package br.com.rhscdev.unit.application.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.rhscdev.application.dto.request.RegraFinanceiraRequest;
import br.com.rhscdev.application.dto.response.DataQueryResult;
import br.com.rhscdev.application.dto.response.RegraFinanceiraResponse;
import br.com.rhscdev.application.mapper.RegraFinanceiraMapper;
import br.com.rhscdev.application.service.RegraFinanceiraService;
import br.com.rhscdev.domain.entity.RegraFinanceiraVO;
import br.com.rhscdev.domain.enumerator.Enums.Situacao;
import br.com.rhscdev.domain.enumerator.Enums.TipoCobranca;
import br.com.rhscdev.domain.enumerator.Enums.TipoMovimento;
import br.com.rhscdev.infrastructure.config.Constantes;
import br.com.rhscdev.infrastructure.persistence.RegraFinancPanacheRepository;
import br.com.rhscdev.interfaces.rest.handler.GlobalException;

@ExtendWith(MockitoExtension.class)
class RegraFinanceiraServiceTest {

    @Mock
    RegraFinancPanacheRepository repository;

    @Mock
    RegraFinanceiraMapper mapper;

    @InjectMocks
    RegraFinanceiraService service;

    private RegraFinanceiraVO regraVO;
    private RegraFinanceiraRequest request;
    private RegraFinanceiraResponse response;

    private final LocalDate DATA_FIXA = LocalDate.of(2026, 3, 20);

    @BeforeEach
    void setup() {
        request = new RegraFinanceiraRequest(
                null,
                "RegraX",
                BigDecimal.ONE,
                TipoCobranca.DINHEIRO.getId(),
                TipoMovimento.DIA.getId(),
                DATA_FIXA,
                null,
                Situacao.CADASTRADO.getId()
        );

        regraVO = RegraFinanceiraVO.criar(
                "RegraY",
                BigDecimal.TEN,
                TipoCobranca.CREDITO.getId(),
                TipoMovimento.FINAL_SEMANA.getId(),
                DATA_FIXA,
                null,
                Situacao.ATIVO.getId()
        );

        response = new RegraFinanceiraResponse(
                1,
                "RegraY",
                BigDecimal.TEN,
                TipoCobranca.CREDITO.getId(),
                TipoMovimento.FINAL_SEMANA.getId(),
                DATA_FIXA,
                null,
                Situacao.ATIVO.getId(),
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("Deve retornar regra financeira por ID quando existir")
    void deveBuscarPorIdComSucesso() {
        int id = 1;

        when(repository.findByIdOp(id)).thenReturn(Optional.of(regraVO));
        when(mapper.toResponse(regraVO)).thenReturn(response);

        var result = service.obterRegraFinanceiraById(id);

        assertThat(result.getRegistros().get(0)).isEqualTo(response);
        assertThat(result.getRegistros().get(0).getDescricao()).isEqualTo("RegraY");
        assertEquals(Constantes.MSG_REGISTROS_ENCONTRADOS, result.getMensagem());

        verify(repository).findByIdOp(id);
        verify(mapper).toResponse(regraVO);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar regra inexistente por ID")
    void deveLancarExcecaoAoBuscarPorIdInexistente() {
        when(repository.findByIdOp(anyInt())).thenReturn(Optional.empty());

        assertThatExceptionOfType(GlobalException.class)
                .isThrownBy(() -> service.obterRegraFinanceiraById(1))
                .withMessage(Constantes.MSG_REGISTROS_NAO_ENCONTRADOS);
    }

    @Test
    @DisplayName("Deve retornar lista de regras quando existirem registros")
    void deveListarRegrasComSucesso() {
        var regra2 = RegraFinanceiraVO.criar(
                "RegraA",
                BigDecimal.ONE,
                TipoCobranca.DEBITO.getId(),
                TipoMovimento.HORA.getId(),
                DATA_FIXA,
                null,
                Situacao.ATIVO.getId()
        );
        
        var resultDb = new DataQueryResult<RegraFinanceiraVO>(List.of(regraVO, regra2), 10l);

        when(repository.findAll(anyInt(), anyInt())).thenReturn(resultDb);
        when(mapper.toResponse(any()))
                .thenReturn(response, new RegraFinanceiraResponse(2, "RegraA", null, null, null, null, null, null, null));

        var result = service.obterRegrasFinanceiras(1, 10);

        assertThat(result.getRegistros()).hasSize(2);
        assertThat(result.getRegistros())
                .extracting(RegraFinanceiraResponse::getDescricao)
                .containsExactlyInAnyOrder("RegraY", "RegraA");

        assertEquals(Constantes.MSG_REGISTROS_ENCONTRADOS, result.getMensagem());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não existirem registros")
    void deveRetornarListaVazia() {
        when(repository.findAll(anyInt(), anyInt())).thenReturn(new DataQueryResult<RegraFinanceiraVO>(Collections.emptyList(), 10l));

        var result = service.obterRegrasFinanceiras(1, 10);

        assertThat(result.getRegistros()).isEmpty();
        assertEquals(Constantes.MSG_REGISTROS_NAO_ENCONTRADOS, result.getMensagem());
    }

    @Test
    @DisplayName("Deve atualizar regra mantendo situação quando data fim futura")
    void deveAtualizarRegraComSucessoSemInativar() {
        int id = 1;
        regraVO.setId(id);

        request = new RegraFinanceiraRequest(
                id,
                "NovaRegra",
                new BigDecimal("200"),
                TipoCobranca.DEBITO.getId(),
                TipoMovimento.MENSALISTA.getId(),
                DATA_FIXA.minusDays(5),
                DATA_FIXA.plusDays(10),
                Situacao.CADASTRADO.getId()
        );

        when(repository.findByIdOp(id)).thenReturn(Optional.of(regraVO));
        when(repository.save(any())).thenReturn(regraVO);
        when(mapper.toResponse(any())).thenReturn(response);

        var result = service.atualizarRegraFinanceira(request);

        assertThat(result.getRegistros().get(0)).isEqualTo(response);
        assertThat(result.getRegistros().get(0).getSituacao()).isNotEqualTo(Situacao.INATIVO.getId());

        verify(repository).save(argThat(vo ->
                vo.getDescricao().equals("NOVAREGRA") &&
                vo.getSituacao().equals(Situacao.CADASTRADO.getId())));
    }

    @Test
    @DisplayName("Deve atualizar regra e inativar quando data fim passada")
    void deveAtualizarEInativar() {
        int id = 1;
        regraVO.setId(id);

        request = new RegraFinanceiraRequest(
                id,
                "NovaRegra",
                new BigDecimal("200"),
                TipoCobranca.DEBITO.getId(),
                TipoMovimento.MENSALISTA.getId(),
                DATA_FIXA.minusDays(5),
                DATA_FIXA.minusDays(1),
                Situacao.CADASTRADO.getId()
        );

        when(repository.findByIdOp(id)).thenReturn(Optional.of(regraVO));
        when(repository.save(any())).thenReturn(regraVO);
        when(mapper.toResponse(any())).thenReturn(response);

        var result = service.atualizarRegraFinanceira(request);

        assertThat(result.getRegistros().get(0)).isEqualTo(response);

        verify(repository).save(argThat(vo -> vo.getSituacao().equals(Situacao.INATIVO.getId())));
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar atualizar regra inexistente")
    void deveLancarErroAoAtualizar() {
    	
        request = new RegraFinanceiraRequest(
                1,
                "RegraX",
                BigDecimal.ONE,
                TipoCobranca.DINHEIRO.getId(),
                TipoMovimento.DIA.getId(),
                DATA_FIXA,
                null,
                Situacao.CADASTRADO.getId()
        );
        when(repository.findByIdOp(anyInt())).thenReturn(Optional.empty());

        assertThatExceptionOfType(GlobalException.class)
                .isThrownBy(() -> service.atualizarRegraFinanceira(request))
                .withMessage(Constantes.MSG_REGISTROS_NAO_ENCONTRADOS);
        
        verify(repository, never()).save(any());
        verify(repository).findByIdOp(anyInt());
    }

    @Test
    @DisplayName("Deve cadastrar regra com sucesso")
    void deveCadastrarRegraComSucesso() {
        when(repository.save(any())).thenReturn(regraVO);
        when(mapper.toResponse(any())).thenReturn(response);

        var result = service.cadastrarRegraFinanceira(request);

        assertThat(result.getRegistros().get(0)).isEqualTo(response);
        assertEquals(Constantes.MSG_REGISTRO_CADASTRADO, result.getMensagem());

        verify(repository).save(argThat(vo ->
                vo.getDescricao().equals("REGRAX") &&
                vo.getSituacao().equals(Situacao.CADASTRADO.getId())
        ));
    }

    @Test
    @DisplayName("Deve excluir regra com sucesso")
    void deveExcluirRegraComSucesso() {
        int id = 1;

        when(repository.findByIdOp(id)).thenReturn(Optional.of(regraVO));

        String result = service.deletarRegraFinanceira(id);

        assertThat(result).contains(Constantes.MSG_REGISTRO_EXCLUIDO);

        verify(repository).findByIdOp(id);
        verify(repository).delete(id);
    }

    @Test
    @DisplayName("Deve lançar erro ao excluir regra inexistente")
    void deveLancarErroAoExcluirUmaRegraInexistente() {
        when(repository.findByIdOp(anyInt())).thenReturn(Optional.empty());

        assertThatExceptionOfType(GlobalException.class)
                .isThrownBy(() -> service.deletarRegraFinanceira(1))
                .withMessage(Constantes.MSG_REGISTROS_NAO_ENCONTRADOS);
    }
}
