package br.com.rhscdev.application.dto.response;

import java.util.List;

import br.com.rhscdev.domain.entity.BaseVO;

public record DataQueryResult<T extends BaseVO>(List<T> registros, Long quantidade){

}
