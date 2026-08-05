package br.com.rhscdev.domain.repository;

import java.util.Optional;

import br.com.rhscdev.application.dto.response.DataQueryResult;
import br.com.rhscdev.domain.entity.MovimentoVeiculoVO;

public interface IMovimentoVeiculoRepository {

    Optional<MovimentoVeiculoVO> findByIdOp(Integer id);
	DataQueryResult<MovimentoVeiculoVO> findAll(int pagina);
	DataQueryResult<MovimentoVeiculoVO> findBySituacao(int idSituacao, int pagina, Integer qtdRegistros);
	MovimentoVeiculoVO save(MovimentoVeiculoVO movVeiculo);
}
