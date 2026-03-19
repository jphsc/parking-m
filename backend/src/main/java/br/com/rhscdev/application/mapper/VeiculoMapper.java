package br.com.rhscdev.application.mapper;

import org.mapstruct.Mapper;

import br.com.rhscdev.application.dto.response.VeiculoResponse;
import br.com.rhscdev.domain.entity.VeiculoVO;

@Mapper(componentModel = "cdi") 
public interface VeiculoMapper {

	VeiculoResponse toResponse(final VeiculoVO entity);
}
