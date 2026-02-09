package br.com.rhscdeveloper.repository;

import br.com.rhscdeveloper.model.MovimentoFinanceiroId;
import br.com.rhscdeveloper.model.MovimentoFinanceiroVO;
import br.com.rhscdeveloper.model.MovimentoVeiculoVO;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MovimentoFinanceiroRepository implements PanacheRepositoryBase<MovimentoFinanceiroVO, MovimentoFinanceiroId> {

	public MovimentoFinanceiroVO findByMovimentoId(MovimentoVeiculoVO movimento) {
        return find("idMovimento.id = ?1", movimento).firstResult();
    }
}
