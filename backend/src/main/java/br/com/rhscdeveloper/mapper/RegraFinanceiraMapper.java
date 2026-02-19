package br.com.rhscdeveloper.mapper;

import org.mapstruct.Mapper;

import br.com.rhscdeveloper.dto.RegraFinanceiraDTO;
import br.com.rhscdeveloper.dto.RegraFinanceiraFiltroDTO;
import br.com.rhscdeveloper.model.RegraFinanceiraVO;

@Mapper(componentModel = "cdi") 
public interface RegraFinanceiraMapper {

	RegraFinanceiraDTO recordToDto(final RegraFinanceiraFiltroDTO rec);
	
	RegraFinanceiraDTO voToDto(final RegraFinanceiraVO entity);

	RegraFinanceiraVO dtoToVo(final RegraFinanceiraDTO dto);

}
