package br.com.rhscdev.unit.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.rhscdev.domain.entity.BaseIdentificavel;
import br.com.rhscdev.infrastructure.config.Constantes;
import br.com.rhscdev.infrastructure.config.Utils;

public class UtilTest {

	@Test
    @DisplayName("Deve retornar 0 quando pagina for null")
    void getNroPaginaConsultaNull() {
        int result = Utils.getNroPaginaConsulta(null);

        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("Deve retornar 0 quando pagina for menor ou igual a zero")
    void getNroPaginaConsultaMenorOuIgualZero() {
        assertThat(Utils.getNroPaginaConsulta(0)).isEqualTo(0);
        assertThat(Utils.getNroPaginaConsulta(-1)).isEqualTo(0);
    }

    @Test
    @DisplayName("Deve retornar pagina - 1 quando pagina for maior que zero")
    void getNroPaginaConsultaMaiorZero() {
        int result = Utils.getNroPaginaConsulta(3);

        assertThat(result).isEqualTo(2);
    }

    @Test
    @DisplayName("Deve retornar 1 quando pagina resposta for null")
    void getNroPaginaRespNull() {
        int result = Utils.getNroPaginaResposta(null);

        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("Deve retornar 1 quando pagina resposta for menor ou igual a zero")
    void getNroPaginaRespMenorOuIgualZero() {
        assertThat(Utils.getNroPaginaResposta(0)).isEqualTo(1);
        assertThat(Utils.getNroPaginaResposta(-5)).isEqualTo(1);
    }

    @Test
    @DisplayName("Deve retornar pagina + 1 quando pagina for maior que zero")
    void getNroPaginaRespMaiorZero() {
        int result = Utils.getNroPaginaResposta(2);

        assertThat(result).isEqualTo(3);
    }

    @Test
    @DisplayName("Deve retornar mensagem de nao encontrados quando lista vazia")
    void getMensagemListaVazia() {
        String result = Utils.getMensagemBuscaRegistro(List.of());

        assertThat(result).isEqualTo(Constantes.MSG_REGISTROS_NAO_ENCONTRADOS);
    }

    @Test
    @DisplayName("Deve retornar mensagem de encontrados quando lista possui elementos")
    void getMensagemListaComElementos() {
        BaseIdentificavel obj = mock(BaseIdentificavel.class);

        String result = Utils.getMensagemBuscaRegistro(List.of(obj));

        assertThat(result).isEqualTo(Constantes.MSG_REGISTROS_ENCONTRADOS);
    }
}
