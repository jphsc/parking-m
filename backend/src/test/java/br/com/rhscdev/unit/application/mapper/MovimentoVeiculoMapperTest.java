package br.com.rhscdev.unit.application.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import br.com.rhscdev.application.mapper.MovimentoFinanceiroMapper;
import br.com.rhscdev.application.mapper.MovimentoVeiculoMapper;
import br.com.rhscdev.application.mapper.MovimentoVeiculoMapperImpl;
import br.com.rhscdev.domain.entity.MovimentoFinanceiroVO;
import br.com.rhscdev.domain.entity.MovimentoVeiculoVO;
import br.com.rhscdev.domain.entity.RegraFinanceiraVO;
import br.com.rhscdev.domain.entity.VeiculoVO;
import br.com.rhscdev.domain.enumerator.Enums.TipoMovimento;

class MovimentoVeiculoMapperTest {

	private final MovimentoFinanceiroMapper financeiroMapper = Mappers.getMapper(MovimentoFinanceiroMapper.class);
    private final MovimentoVeiculoMapper mapper = new MovimentoVeiculoMapperImpl(financeiroMapper);

    @Test
    void deveMapearSemFinanceiro() {
        VeiculoVO veiculo = VeiculoVO.criar("HB20", "HYUNDAI", "ABC1234");
        veiculo.setId(1);

        MovimentoVeiculoVO entity = MovimentoVeiculoVO.criar(veiculo, TipoMovimento.DIA.getId(), null, null, 1);
        entity.setId(2);

        var response = mapper.toResponse(entity);

        assertThat(response.getIdMovimento()).isEqualTo(2);
        assertThat(response.getIdVeiculo()).isEqualTo(1);
        assertThat(response.getPlaca()).isEqualTo("ABC1234");
        assertThat(response.getIdRegra()).isNull();
    }

    @Test
    void deveMapearComFinanceiro() {
        VeiculoVO veiculo = VeiculoVO.criar("HB20", "HYUNDAI", "ABC1234");
        veiculo.setId(1);

        MovimentoVeiculoVO movVeiculo = MovimentoVeiculoVO.criar(veiculo, TipoMovimento.FINAL_SEMANA.getId(), LocalDateTime.of(2026, 3, 29, 12, 30), null, 1);
        movVeiculo.setId(2);

        RegraFinanceiraVO regra = new RegraFinanceiraVO();
        regra.setId(99);
        regra.setValor(BigDecimal.TEN);

        MovimentoFinanceiroVO financeiro = MovimentoFinanceiroVO.criar(regra, movVeiculo, 1);
        movVeiculo.vincularFinanceiro(financeiro);

        var response = mapper.toResponse(movVeiculo);

        assertThat(response.getIdRegra()).isEqualTo(99);
    }
}