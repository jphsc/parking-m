package br.com.rhscdev.domain.repository;

import java.util.List;
import java.util.Optional;

import br.com.rhscdev.domain.entity.VeiculoVO;

public interface IVeiculoRepository {

	Optional<VeiculoVO> findByIdOp(Integer id);
	Optional<VeiculoVO> findByPlaca(String placa);
	List<VeiculoVO> findAll(Integer pagina);
	VeiculoVO save(VeiculoVO veiculo);
	void delete(Integer id);
}
