package br.com.rhscdev.unit.application.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import br.com.rhscdev.application.mapper.RegraFinanceiraMapper;
import br.com.rhscdev.domain.entity.RegraFinanceiraVO;

class RegraFinanceiraMapperTest {

    private final RegraFinanceiraMapper mapper = Mappers.getMapper(RegraFinanceiraMapper.class);

    @Test
    void deveMapearRegraFinanceira() {
        RegraFinanceiraVO entity = RegraFinanceiraVO.criar(
                "REGRA",
                BigDecimal.TEN,
                1,
                1,
                LocalDate.now(),
                null,
                1
        );
        entity.setId(1);

        var response = mapper.toResponse(entity);

        assertThat(response.getId()).isEqualTo(1);
        assertThat(response.getDescricao()).isEqualTo("REGRA");
    }
}