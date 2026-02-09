package br.com.rhscdeveloper.mapper;

import org.mapstruct.Mapper;

import br.com.rhscdeveloper.dto.RegraFinanceiraDTO;
import br.com.rhscdeveloper.model.RegraFinanceiraVO;

@Mapper(componentModel = "cdi") 
public interface RegraFinanceiraMapper {

	RegraFinanceiraDTO toDto(final RegraFinanceiraVO entity);

	RegraFinanceiraVO toVo(final RegraFinanceiraDTO dto);

}
