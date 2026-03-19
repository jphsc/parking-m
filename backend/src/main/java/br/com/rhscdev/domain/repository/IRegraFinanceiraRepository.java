package br.com.rhscdev.domain.repository;

import java.util.List;
import java.util.Optional;

import br.com.rhscdev.domain.entity.RegraFinanceiraVO;

public interface IRegraFinanceiraRepository {

	Optional<RegraFinanceiraVO> findByIdOp(Integer id);
	List<RegraFinanceiraVO> findAll(Integer pagina);
	RegraFinanceiraVO save(RegraFinanceiraVO regra);
	void delete(Integer id);
}
