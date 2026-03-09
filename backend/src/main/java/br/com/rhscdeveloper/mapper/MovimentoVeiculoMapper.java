package br.com.rhscdeveloper.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import br.com.rhscdeveloper.bean.MovimentoVeiculoBean;
import br.com.rhscdeveloper.dto.MovVeiculoRespDTO;
import br.com.rhscdeveloper.model.MovimentoVeiculoVO;

@Mapper(componentModel = "cdi", uses = MovimentoFinanceiroMapper.class)
public interface MovimentoVeiculoMapper {

	@Mapping(target = "idMovimento", source = "id")
	@Mapping(target = "idVeiculo", source = "veiculo.id")
	@Mapping(target = "idRegra", expression = "java(extrairIdRegra(entity))")
	@Mapping(target = "placa", source = "veiculo.placa")
	@Mapping(target = "movimentoFinanceiro", source = "movFinanceiro")
	MovVeiculoRespDTO toDto(final MovimentoVeiculoVO entity);
	
	MovimentoVeiculoBean voToBean(final MovimentoVeiculoVO entity);
	
	MovimentoVeiculoVO beanToDto(final MovimentoVeiculoBean bean);
	
	@Named("extrairIdRegra")
	default Integer extrairIdRegra(MovimentoVeiculoVO entity) {
	    return entity.getMovFinanceiro() != null ? entity.getMovFinanceiro().getIdRegra() : null;
	}
}
