package br.com.rhscdev.unit.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.rhscdev.domain.entity.MovimentoFinanceiroVO;
import br.com.rhscdev.domain.entity.MovimentoVeiculoVO;
import br.com.rhscdev.domain.entity.RegraFinanceiraVO;
import br.com.rhscdev.domain.entity.VeiculoVO;
import br.com.rhscdev.domain.enumerator.Enums.TipoMovimento;
import br.com.rhscdev.domain.exception.DomainException;

@ExtendWith(MockitoExtension.class)
class MovimentoVeiculoVOTest {

    @Test
    void deveVincularFinanceiro() {
//        VeiculoVO veiculo = VeiculoVO.criar("HB20", "HYUNDAI", "ABC1234");
        VeiculoVO veiculo = mock(VeiculoVO.class);

        MovimentoVeiculoVO mov = MovimentoVeiculoVO.criar(veiculo, TipoMovimento.DIA.getId(), LocalDateTime.now(), null, 1);

        RegraFinanceiraVO regra = new RegraFinanceiraVO();
        regra.setId(1);
        regra.setValor(BigDecimal.ONE);

        MovimentoFinanceiroVO financeiro = MovimentoFinanceiroVO.criar(regra, mov, 1);

        mov.vincularFinanceiro(financeiro);

        assertThat(mov.getMovFinanceiro()).isEqualTo(financeiro);
    }

    @Test
    void deveLancarErroAoVincularNulo() {
        MovimentoVeiculoVO mov = new MovimentoVeiculoVO();

        assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() -> mov.vincularFinanceiro(null));
    }

    @Test
    void deveEncerrarMovimento() {
        MovimentoVeiculoVO mov = new MovimentoVeiculoVO();
        mov.setSituacao(1);

        mov.encerrar(LocalDateTime.now());

        assertThat(mov.getDtHrSaida()).isNotNull();
    }
}