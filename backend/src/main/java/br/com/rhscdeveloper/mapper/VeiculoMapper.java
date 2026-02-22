package br.com.rhscdeveloper.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.rhscdeveloper.dto.VeiculoResponseDTO;
import br.com.rhscdeveloper.dto.VeiculoRequestDTO;
import br.com.rhscdeveloper.model.VeiculoVO;

@Mapper(componentModel = "cdi") 
public interface VeiculoMapper {

	VeiculoResponseDTO voToDto(final VeiculoVO entity);
	
	VeiculoResponseDTO recordToDto(VeiculoRequestDTO vo);
	
	@Mapping(target = "dtRegistro", ignore = true)
	@Mapping(target = "versao", ignore = true)
	void updateVoFromDto(VeiculoResponseDTO dto, @MappingTarget VeiculoVO vo);
}
