package br.com.rhscdeveloper.mapper;

import org.mapstruct.Mapper;

import br.com.rhscdeveloper.dto.VeiculoDTO;
import br.com.rhscdeveloper.model.VeiculoVO;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi") 
public interface VeiculoMapper {

	@Mapping(target = "pagina", ignore = true)
	VeiculoDTO toDto(final VeiculoVO entity);

	@Mapping(target = "movimentos", ignore = true)
	VeiculoVO toVo(final VeiculoDTO dto);

}
