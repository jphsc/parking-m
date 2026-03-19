package br.com.rhscdev.application.mapper;

import org.mapstruct.Mapper;

import br.com.rhscdev.application.dto.response.MovimentoFinanceiroResponse;
import br.com.rhscdev.domain.entity.MovimentoFinanceiroVO;

import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface MovimentoFinanceiroMapper {

	@Mapping(target = "idRegra", source = "idRegra")
	@Mapping(target = "idMovimento", source = "idMovimento")
	MovimentoFinanceiroResponse toResponse(final MovimentoFinanceiroVO entity);
}
