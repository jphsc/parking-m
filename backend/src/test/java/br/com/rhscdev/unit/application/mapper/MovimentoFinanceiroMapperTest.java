package br.com.rhscdev.unit.application.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import br.com.rhscdev.application.mapper.MovimentoFinanceiroMapper;
import br.com.rhscdev.domain.entity.MovimentoFinanceiroVO;
import br.com.rhscdev.domain.entity.MovimentoVeiculoVO;
import br.com.rhscdev.domain.entity.RegraFinanceiraVO;
import br.com.rhscdev.domain.enumerator.Enums.TipoMovimento;

class MovimentoFinanceiroMapperTest {

    private final MovimentoFinanceiroMapper mapper = Mappers.getMapper(MovimentoFinanceiroMapper.class);

    @Test
    void deveMapearMovimentoFinanceiro() {
        RegraFinanceiraVO regra = new RegraFinanceiraVO();
        regra.setId(10);
        regra.setValor(BigDecimal.TEN);

        MovimentoVeiculoVO movimento = new MovimentoVeiculoVO();
        movimento.setId(20);
        movimento.setTipoMovimento(TipoMovimento.DIA.getId());
        movimento.setDtHrEntrada(LocalDateTime.of(2026, 3, 29, 12, 30));

        MovimentoFinanceiroVO entity = MovimentoFinanceiroVO.criar(regra, movimento, 1);

        var response = mapper.toResponse(entity);

        assertThat(response.getIdRegra()).isEqualTo(10);
        assertThat(response.getIdMovimento()).isEqualTo(20);
    }
}