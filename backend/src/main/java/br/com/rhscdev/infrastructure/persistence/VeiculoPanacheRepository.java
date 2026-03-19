package br.com.rhscdev.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import br.com.rhscdev.domain.entity.VeiculoVO;
import br.com.rhscdev.domain.repository.IVeiculoRepository;
import br.com.rhscdev.infrastructure.config.Constantes;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class VeiculoPanacheRepository implements IVeiculoRepository, PanacheRepositoryBase<VeiculoVO, Integer> {

	@Override
	public Optional<VeiculoVO> findByIdOp(Integer id) {
		return findByIdOptional(id);
	}

	@Override
	public Optional<VeiculoVO> findByPlaca(String placa) {
		return find("placa", placa).firstResultOptional();
	}

	@Override
	public List<VeiculoVO> findAll(Integer pagina) {
		return findAll(Sort.by("id"))
				.page(pagina, Constantes.NRO_MAX_REGISTROS_PAGINACAO)
				.list();
	}

	@Override
	public VeiculoVO save(VeiculoVO veiculo) {
		
		if (veiculo.getId() == null) {
			persist(veiculo);
		} else {
			veiculo = getEntityManager().merge(veiculo);
		}
		getEntityManager().flush();
		
		return veiculo;
	}

	@Override
	public void delete(Integer id) {
		deleteById(id);
	}
	
}
