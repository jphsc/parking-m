package br.com.rhscdev.infrastructure.persistence;

import br.com.rhscdev.domain.entity.MovimentoFinanceiroVO;
import br.com.rhscdev.domain.entity.MovimentoFinanceiroVOId;
import br.com.rhscdev.domain.entity.MovimentoVeiculoVO;
import br.com.rhscdev.domain.repository.IMovimentoFinanceiroRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MovFinanceiroPanacheRepository implements IMovimentoFinanceiroRepository, 
	PanacheRepositoryBase<MovimentoFinanceiroVO, MovimentoFinanceiroVOId> {

	public MovimentoFinanceiroVO findByMovimentoId(MovimentoVeiculoVO movimento) {
        return find("idMovimento.id = ?1", movimento).firstResult();
    }

	@Override
	public MovimentoFinanceiroVO save(MovimentoFinanceiroVO movimento) {
		
		if (movimento.getId() == null) {
			persist(movimento);
		} else {
			movimento = getEntityManager().merge(movimento);
		}
		getEntityManager().flush();
		
		return movimento;
	}
}
