package br.com.rhscdev.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import br.com.rhscdev.domain.entity.RegraFinanceiraVO;
import br.com.rhscdev.domain.repository.IRegraFinanceiraRepository;
import br.com.rhscdev.infrastructure.config.Constantes;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RegraFinancPanacheRepository implements IRegraFinanceiraRepository, PanacheRepositoryBase<RegraFinanceiraVO, Integer> {
	
	@Override
	public Optional<RegraFinanceiraVO> findByIdOp(Integer id) {
		return findByIdOptional(id);
	}

	public List<RegraFinanceiraVO> findAll(Integer pagina){
		return findAll(Sort.by("id"))
				.page(pagina, Constantes.NRO_MAX_REGISTROS_PAGINACAO)
				.list();
	}

	@Override
	public RegraFinanceiraVO save(RegraFinanceiraVO regra) {
		
		if (regra.getId() == null) {
			persist(regra);
		} else {
			regra = getEntityManager().merge(regra);
		}
		getEntityManager().flush();
		
		return regra;
	}

	@Override
	public void delete(Integer id) {
		deleteById(id);
	}
}
