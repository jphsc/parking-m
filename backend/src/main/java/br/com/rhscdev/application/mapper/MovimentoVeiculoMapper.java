package br.com.rhscdev.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import br.com.rhscdev.application.dto.response.MovimentoVeiculoResponse;
import br.com.rhscdev.domain.entity.MovimentoVeiculoVO;

@Mapper(componentModel = "cdi", uses = MovimentoFinanceiroMapper.class)
public interface MovimentoVeiculoMapper {

	@Mapping(target = "idMovimento", source = "id")
	@Mapping(target = "idVeiculo", source = "veiculo.id")
	@Mapping(target = "idRegra", expression = "java(extrairIdRegra(entity))")
	@Mapping(target = "placa", source = "veiculo.placa")
	@Mapping(target = "movimentoFinanceiro", source = "movFinanceiro")
	MovimentoVeiculoResponse toResponse(final MovimentoVeiculoVO entity);
	
	@Named("extrairIdRegra")
	default Integer extrairIdRegra(MovimentoVeiculoVO entity) {
	    return entity.getMovFinanceiro() != null ? entity.getMovFinanceiro().getIdRegra() : null;
	}
}
