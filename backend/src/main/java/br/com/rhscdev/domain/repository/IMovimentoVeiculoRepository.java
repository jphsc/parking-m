package br.com.rhscdev.domain.repository;

import java.util.List;
import java.util.Optional;

import br.com.rhscdev.domain.entity.MovimentoVeiculoVO;

public interface IMovimentoVeiculoRepository {

    Optional<MovimentoVeiculoVO> findByIdOp(Integer id);
	List<MovimentoVeiculoVO> findAll(int pagina);
	List<MovimentoVeiculoVO> findBySituacao(int idSituacao, int pagina);
	MovimentoVeiculoVO save(MovimentoVeiculoVO movVeiculo);
}
