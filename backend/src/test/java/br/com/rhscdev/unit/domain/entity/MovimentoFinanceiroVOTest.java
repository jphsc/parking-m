package br.com.rhscdev.unit.domain.entity;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import br.com.rhscdev.domain.entity.*;
import br.com.rhscdev.domain.enumerator.Enums.TipoMovimento;
import br.com.rhscdev.domain.exception.DomainException;

class MovimentoFinanceiroVOTest {

    @Test
    void deveCriarMovimentoFinanceiro() {
        RegraFinanceiraVO regra = new RegraFinanceiraVO();
        regra.setId(1);
        regra.setValor(BigDecimal.TEN);
        regra.setTipoMovimento(TipoMovimento.FINAL_SEMANA.getId());

        VeiculoVO veiculo = VeiculoVO.criar("HB20", "HYUNDAI", "ABC1234");

        MovimentoVeiculoVO mov = MovimentoVeiculoVO.criar(veiculo, TipoMovimento.FINAL_SEMANA.getId(), LocalDateTime.now().minusHours(2), null, 1);
        MovimentoFinanceiroVO mf = MovimentoFinanceiroVO.criar(regra, mov, 1);

        assertThat(mf.getIdRegra()).isEqualTo(1);
        assertThat(mf.getMovimento()).isEqualTo(mov);
    }

    @Test
    void deveLancarErroQuandoRegraNula() {
        assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() -> MovimentoFinanceiroVO.criar(null, new MovimentoVeiculoVO(), 1));
    }

    @Test
    void deveLancarErroQuandoMovimentoNulo() {
        assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() -> MovimentoFinanceiroVO.criar(new RegraFinanceiraVO(), null, 1));
    }
}