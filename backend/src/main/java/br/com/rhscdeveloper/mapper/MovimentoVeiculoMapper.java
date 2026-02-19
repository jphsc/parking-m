package br.com.rhscdeveloper.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import br.com.rhscdeveloper.dto.MovimentoVeiculoDTO;
import br.com.rhscdeveloper.model.MovimentoVeiculoVO;
import br.com.rhscdeveloper.model.VeiculoVO;

@Mapper(componentModel = "cdi", uses = {MovimentoFinanceiroMapper.class})
public interface MovimentoVeiculoMapper {

	@Mapping(target = "idMovimento", source = "id")
	@Mapping(target = "idVeiculo", source = "veiculo.id")
	@Mapping(target = "idRegra", expression = "java(extrairIdRegra(entity))")
	@Mapping(target = "placa", source = "veiculo.placa")
	@Mapping(target = "movimentoFinanceiro", source = "movFinanceiro")
	MovimentoVeiculoDTO toDto(final MovimentoVeiculoVO entity);

	@Mapping(target = "id", source = "idMovimento")
	@Mapping(target = "veiculo", source = "idVeiculo", qualifiedByName = "mapVeiculo")
	MovimentoVeiculoVO toVo(final MovimentoVeiculoDTO dto);
	
	@Named("mapVeiculo")
    default VeiculoVO mapVeiculo(Integer idVeiculo) {
        if (idVeiculo == null) {
            return null;
        }
        VeiculoVO veiculo = new VeiculoVO();
        veiculo.setId(idVeiculo);
        return veiculo;
    }
    
	default Integer extrairIdRegra(MovimentoVeiculoVO entity) {
	    return entity.getMovFinanceiro() != null ? entity.getMovFinanceiro().getIdRegra() : null;
	}
}
