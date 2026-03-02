package br.com.rhscdeveloper.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.rhscdeveloper.dto.VeiculoRequestDTO;
import br.com.rhscdeveloper.dto.VeiculoResponseDTO;
import br.com.rhscdeveloper.model.VeiculoVO;
import br.com.rhscdeveloper.util.Utils;

@Mapper(componentModel = "cdi") 
public interface VeiculoMapper {

	VeiculoResponseDTO voToDto(final VeiculoVO entity);
	
	@Mapping(target = "dtRegistro", ignore = true)
	@Mapping(target = "versao", ignore = true)
	VeiculoResponseDTO recordToDto(VeiculoRequestDTO vo);
	
	default void updateVoFromDto(VeiculoResponseDTO dto, VeiculoVO vo) {
		Utils.updateVoToDto(vo, dto);
	}
}
