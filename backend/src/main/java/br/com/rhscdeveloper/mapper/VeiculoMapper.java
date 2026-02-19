package br.com.rhscdeveloper.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.rhscdeveloper.dto.VeiculoDTO;
import br.com.rhscdeveloper.dto.VeiculoFiltroDTO;
import br.com.rhscdeveloper.model.VeiculoVO;

@Mapper(componentModel = "cdi") 
public interface VeiculoMapper {

	VeiculoDTO voToDto(final VeiculoVO entity);
	
	@Mapping(target = "versao", ignore = true)
	VeiculoDTO recordToDto(VeiculoFiltroDTO vo);
	
	@Mapping(target = "dtRegistro", ignore = true)
	@Mapping(target = "versao", ignore = true)
	void updateVoFromDto(VeiculoDTO dto, @MappingTarget VeiculoVO vo);
}
