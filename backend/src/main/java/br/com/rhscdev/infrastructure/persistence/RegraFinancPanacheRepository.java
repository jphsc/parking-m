package br.com.rhscdev.infrastructure.persistence;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import br.com.rhscdev.application.dto.response.DataQueryResult;
import br.com.rhscdev.domain.entity.RegraFinanceiraVO;
import br.com.rhscdev.domain.repository.IRegraFinanceiraRepository;
import br.com.rhscdev.infrastructure.config.Constantes;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RegraFinancPanacheRepository implements IRegraFinanceiraRepository, PanacheRepositoryBase<RegraFinanceiraVO, Integer> {
	
	@Override
	public Optional<RegraFinanceiraVO> findByIdOp(Integer id) {
		return findByIdOptional(id);
	}

	@Override
	public DataQueryResult<RegraFinanceiraVO> findAll(Integer pagina, Integer qtdRegistros){

		int qtdReg = Objects.isNull(qtdRegistros) || qtdRegistros > Constantes.NRO_MAX_REGISTROS_PAGINACAO ? Constantes.NRO_MAX_REGISTROS_PAGINACAO : qtdRegistros;
		PanacheQuery<RegraFinanceiraVO> dataDb = findAll(Sort.by("id"));
		List<RegraFinanceiraVO> registros = dataDb.page(pagina, qtdReg).list();
		Long qtd = dataDb.count();
		
		return new DataQueryResult<RegraFinanceiraVO>(registros, qtd);
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
