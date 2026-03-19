package br.com.rhscdev.application.mapper;

import org.mapstruct.Mapper;

import br.com.rhscdev.application.dto.response.RegraFinanceiraResponse;
import br.com.rhscdev.domain.entity.RegraFinanceiraVO;

@Mapper(componentModel = "cdi") 
public interface RegraFinanceiraMapper {
	
	RegraFinanceiraResponse toResponse(final RegraFinanceiraVO entity);
	
}
