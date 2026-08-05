package br.com.rhscdev.domain.repository;

import java.util.Optional;

import br.com.rhscdev.application.dto.response.DataQueryResult;
import br.com.rhscdev.domain.entity.RegraFinanceiraVO;

public interface IRegraFinanceiraRepository {

	Optional<RegraFinanceiraVO> findByIdOp(Integer id);
	DataQueryResult<RegraFinanceiraVO> findAll(Integer pagina, Integer qtdRegistros);
	RegraFinanceiraVO save(RegraFinanceiraVO regra);
	void delete(Integer id);
}
