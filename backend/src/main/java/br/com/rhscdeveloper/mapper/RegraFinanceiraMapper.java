package br.com.rhscdeveloper.mapper;

import org.mapstruct.Mapper;

import br.com.rhscdeveloper.dto.RegraFinRequestDTO;
import br.com.rhscdeveloper.dto.RegraFinResponseDTO;
import br.com.rhscdeveloper.model.RegraFinanceiraVO;
import br.com.rhscdeveloper.util.Utils;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi") 
public interface RegraFinanceiraMapper {

	@Mapping(target = "versao", ignore = true)
	RegraFinResponseDTO recordToDto(final RegraFinRequestDTO rec);
	
	RegraFinResponseDTO voToDto(final RegraFinanceiraVO entity);

	RegraFinanceiraVO dtoToVo(final RegraFinResponseDTO dto);
	
	default void updateVoFromDto(RegraFinResponseDTO dto, RegraFinanceiraVO vo) {
		Utils.updateVoToDto(vo, dto);
	}
}
