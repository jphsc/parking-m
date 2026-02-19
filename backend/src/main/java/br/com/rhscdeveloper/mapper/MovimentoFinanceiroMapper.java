package br.com.rhscdeveloper.mapper;

import org.mapstruct.Mapper;

import br.com.rhscdeveloper.dto.MovimentoFinanceiroDTO;
import br.com.rhscdeveloper.model.MovimentoFinanceiroVO;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface MovimentoFinanceiroMapper {

	@Mapping(target = "idRegra", source = "idRegra")
	@Mapping(target = "idMovimento", source = "idMovimento")
	MovimentoFinanceiroDTO toDto(final MovimentoFinanceiroVO entity);

	@Mapping(target = "regra", ignore = true)
	@Mapping(target = "movimento", ignore = true)
	MovimentoFinanceiroVO toVo(final MovimentoFinanceiroDTO dto);
}
