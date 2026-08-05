package br.com.rhscdev.unit.application.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.rhscdev.application.dto.request.VeiculoRequest;
import br.com.rhscdev.application.dto.response.DataQueryResult;
import br.com.rhscdev.application.dto.response.VeiculoResponse;
import br.com.rhscdev.application.mapper.VeiculoMapper;
import br.com.rhscdev.application.service.VeiculoService;
import br.com.rhscdev.domain.entity.VeiculoVO;
import br.com.rhscdev.infrastructure.config.Constantes;
import br.com.rhscdev.infrastructure.persistence.VeiculoPanacheRepository;
import br.com.rhscdev.interfaces.rest.handler.GlobalException;
import br.com.rhscdev.interfaces.rest.handler.ValidacaoConstraintException;

@ExtendWith(MockitoExtension.class)
class VeiculoServiceTest {

    @Mock
    VeiculoPanacheRepository repository;

    @Mock
    VeiculoMapper mapper;

    @InjectMocks
    VeiculoService service;

    private VeiculoRequest request;
    private VeiculoVO veiculo;
    private VeiculoResponse response;

    @BeforeEach
    void setup() {
        request = new VeiculoRequest(null, "hb20", "hyundai", "ABC1234");
        veiculo = VeiculoVO.criar("HB20", "HYUNDAI", "ABC1234");
        response = new VeiculoResponse();
    }

    @Test
    void deveCadastrarVeiculoComSucesso() {
        when(repository.findByPlaca("ABC1234")).thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(veiculo);
        when(mapper.toResponse(veiculo)).thenReturn(response);

        var result = service.cadastrarVeiculo(request);

        assertThat(result.getMensagem()).isEqualTo(Constantes.MSG_REGISTRO_CADASTRADO);
        assertThat(result.getRegistros()).hasSize(1);

        verify(repository).save(argThat(v ->
                v.getModelo().equals("HB20") &&
                v.getMontadora().equals("HYUNDAI") &&
                v.getPlaca().equals("ABC1234")
        ));
    }

    @Test
    void deveLancarErroAoCadastrarComPlacaExistente() {
        when(repository.findByPlaca("ABC1234")).thenReturn(Optional.of(veiculo));

        assertThatExceptionOfType(ValidacaoConstraintException.class)
                .isThrownBy(() -> service.cadastrarVeiculo(request))
                .withMessage(Constantes.MSG_ERRO_PLACA_EXISTE);

        verify(repository, never()).save(any());
    }

    @Test
    void deveAtualizarVeiculoComSucesso() {
        veiculo.setId(1);

        var update = new VeiculoRequest(1, "ONIX", "CHEVROLET", "ABC1234");

        when(repository.findByIdOp(1)).thenReturn(Optional.of(veiculo));
        when(repository.findByPlaca("ABC1234")).thenReturn(Optional.of(veiculo));
        when(repository.save(any())).thenReturn(veiculo);
        when(mapper.toResponse(any())).thenReturn(response);

        var result = service.atualizarVeiculo(update);

        assertThat(result.getMensagem()).isEqualTo(Constantes.MSG_REGISTRO_ATUALIZADO);

        verify(repository).save(argThat(v ->
                v.getModelo().equals("ONIX") &&
                v.getMontadora().equals("CHEVROLET")
        ));
    }

    @Test
    void deveLancarErroAoAtualizarQuandoIdNaoExiste() {
        when(repository.findByIdOp(1)).thenReturn(Optional.empty());

        var update = new VeiculoRequest(1, "ONIX", "CHEVROLET", "ABC1234");

        assertThatExceptionOfType(ValidacaoConstraintException.class)
                .isThrownBy(() -> service.atualizarVeiculo(update));
    }

    @Test
    void deveLancarErroAoAtualizarComPlacaDeOutroVeiculo() {
        var existente = VeiculoVO.criar("A", "B", "ABC1234");
        existente.setId(2);

        var atual = VeiculoVO.criar("C", "D", "ABC1234");
        atual.setId(1);

        when(repository.findByIdOp(1)).thenReturn(Optional.of(atual));
        when(repository.findByPlaca("ABC1234")).thenReturn(Optional.of(existente));

        var update = new VeiculoRequest(1, "ONIX", "CHEVROLET", "ABC1234");

        assertThatExceptionOfType(ValidacaoConstraintException.class)
                .isThrownBy(() -> service.atualizarVeiculo(update));
    }

    @Test
    void deveExcluirVeiculoComSucesso() {
        int id = 1;

        when(repository.findByIdOp(id)).thenReturn(Optional.of(veiculo));

        var result = service.deletarVeiculo(id);

        assertThat(result).contains(Constantes.MSG_REGISTRO_EXCLUIDO);

        verify(repository).delete(id);
    }

    @Test
    void deveLancarErroAoExcluirInexistente() {
        when(repository.findByIdOp(1)).thenReturn(Optional.empty());

        assertThatExceptionOfType(GlobalException.class)
                .isThrownBy(() -> service.deletarVeiculo(1));
    }

    @Test
    void deveBuscarPorIdComSucesso() {
        veiculo.setId(1);

        when(repository.findByIdOp(1)).thenReturn(Optional.of(veiculo));
        when(mapper.toResponse(veiculo)).thenReturn(response);

        var result = service.obterVeiculoById(1);

        assertThat(result.getRegistros()).hasSize(1);
        assertThat(result.getMensagem()).isEqualTo(Constantes.MSG_REGISTROS_ENCONTRADOS);

        verify(mapper).toResponse(veiculo);
    }

    @Test
    void deveLancarErroAoBuscarPorIdInexistente() {
        when(repository.findByIdOp(1)).thenReturn(Optional.empty());

        assertThatExceptionOfType(GlobalException.class)
                .isThrownBy(() -> service.obterVeiculoById(1));
    }

    @Test
    void deveListarVeiculosComSucesso() {
        when(repository.findAll(anyInt())).thenReturn(new DataQueryResult<VeiculoVO>(List.of(veiculo), 1L));
        when(mapper.toResponse(veiculo)).thenReturn(response);

        var result = service.obterVeiculos(1);

        assertThat(result.getRegistros()).hasSize(1);
        assertThat(result.getMensagem()).isEqualTo(Constantes.MSG_REGISTROS_ENCONTRADOS);
    }

    @Test
    void deveRetornarListaVazia() {
        when(repository.findAll(anyInt())).thenReturn(new DataQueryResult<VeiculoVO>(List.of(), 1L));

        var result = service.obterVeiculos(1);

        assertThat(result.getRegistros()).isEmpty();
        assertThat(result.getMensagem()).isEqualTo(Constantes.MSG_REGISTROS_NAO_ENCONTRADOS);
    }
}
