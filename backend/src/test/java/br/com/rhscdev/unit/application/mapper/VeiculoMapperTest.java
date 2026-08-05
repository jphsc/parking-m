package br.com.rhscdev.unit.application.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import br.com.rhscdev.application.mapper.VeiculoMapper;
import br.com.rhscdev.application.mapper.VeiculoMapperImpl;
import br.com.rhscdev.domain.entity.VeiculoVO;

class VeiculoMapperTest {

    private final VeiculoMapper mapper = new VeiculoMapperImpl();

    @Test
    void deveMapearVeiculo() {
        VeiculoVO entity = VeiculoVO.criar("HB20", "HYUNDAI", "ABC1234");
        entity.setId(1);

        var response = mapper.toResponse(entity);

        assertThat(response.getId()).isEqualTo(1);
        assertThat(response.getModelo()).isEqualTo("HB20");
    }
}