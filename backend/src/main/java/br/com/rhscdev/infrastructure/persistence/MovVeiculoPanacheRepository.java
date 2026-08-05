package br.com.rhscdev.infrastructure.persistence;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import br.com.rhscdev.application.dto.response.DataQueryResult;
import br.com.rhscdev.domain.entity.MovimentoVeiculoVO;
import br.com.rhscdev.domain.repository.IMovimentoVeiculoRepository;
import br.com.rhscdev.infrastructure.config.Constantes;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
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
	public DataQueryResult<MovimentoVeiculoVO> findAll(int pagina) {
		PanacheQuery<MovimentoVeiculoVO> dataDb = findAll(Sort.by("id"));
		List<MovimentoVeiculoVO> movs = dataDb.page(pagina, Constantes.NRO_MAX_REGISTROS_PAGINACAO).list();
		Long qtd = dataDb.count();
		
		return new DataQueryResult<MovimentoVeiculoVO>(movs, qtd);
	}

	@Override
	public DataQueryResult<MovimentoVeiculoVO> findBySituacao(int idSituacao, int pagina, Integer qtdRegistros) {
		int qtdReg = Objects.isNull(qtdRegistros) || qtdRegistros <= 0 || qtdRegistros > Constantes.NRO_MAX_REGISTROS_PAGINACAO ? Constantes.NRO_MAX_REGISTROS_PAGINACAO : qtdRegistros;

		PanacheQuery<MovimentoVeiculoVO> dataDb = find("from MovimentoVeiculoVO where situacao = ?1", Sort.by("id"), idSituacao);
		List<MovimentoVeiculoVO> registros = dataDb.page(pagina, qtdReg).list();
		Long qtd = dataDb.count();
		
		return new DataQueryResult<MovimentoVeiculoVO>(registros, qtd);
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
