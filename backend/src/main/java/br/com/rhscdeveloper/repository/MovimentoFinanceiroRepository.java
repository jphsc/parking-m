package br.com.rhscdeveloper.repository;

import br.com.rhscdeveloper.model.MovimentoFinanceiroVOId;
import br.com.rhscdeveloper.model.MovimentoFinanceiroVO;
import br.com.rhscdeveloper.model.MovimentoVeiculoVO;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MovimentoFinanceiroRepository implements PanacheRepositoryBase<MovimentoFinanceiroVO, MovimentoFinanceiroVOId> {

	public MovimentoFinanceiroVO findByMovimentoId(MovimentoVeiculoVO movimento) {
        return find("idMovimento.id = ?1", movimento).firstResult();
    }
}
