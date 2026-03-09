package br.com.rhscdeveloper.mapper;

import org.mapstruct.Mapper;

import br.com.rhscdeveloper.dto.MovFinanceiroRespDTO;
import br.com.rhscdeveloper.model.MovimentoFinanceiroVO;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface MovimentoFinanceiroMapper {

	@Mapping(target = "idRegra", source = "idRegra")
	@Mapping(target = "idMovimento", source = "idMovimento")
	MovFinanceiroRespDTO toDto(final MovimentoFinanceiroVO entity);
}
