package br.com.rhscdev.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import br.com.rhscdev.domain.entity.MovimentoVeiculoVO;
import br.com.rhscdev.domain.repository.IMovimentoVeiculoRepository;
import br.com.rhscdev.infrastructure.config.Constantes;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MovVeiculoPanacheRepository implements IMovimentoVeiculoRepository, PanacheRepositoryBase<MovimentoVeiculoVO, Integer> {

	@Override
	public Optional<MovimentoVeiculoVO> findByIdOp(Integer id) {
		return findByIdOptional(id);
	}

	@Override
	public List<MovimentoVeiculoVO> findAll(int pagina) {
		return findAll(Sort.by("id"))
				.page(pagina, Constantes.NRO_MAX_REGISTROS_PAGINACAO)
				.list();
	}

	@Override
	public List<MovimentoVeiculoVO> findBySituacao(int idSituacao, int pagina) {
		String query = "from MovimentoVeiculoVO where situacao = ?1";
		
		return find(query, Sort.by("id"), idSituacao)
				.page(pagina, Constantes.NRO_MAX_REGISTROS_PAGINACAO)
				.list();
	}

	@Override
	public MovimentoVeiculoVO save(MovimentoVeiculoVO movimento) {
		
		if (movimento.getId() == null) {
			persist(movimento);
		} else {
			movimento = getEntityManager().merge(movimento);
		}
		getEntityManager().flush();
		
		return movimento;
	}
}
