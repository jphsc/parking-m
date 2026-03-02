package br.com.rhscdeveloper.repository;

import java.util.List;

import br.com.rhscdeveloper.enumerator.Enums.SituacaoMovimento;
import br.com.rhscdeveloper.model.MovimentoFinanceiroVO;
import br.com.rhscdeveloper.model.MovimentoVeiculoVO;
import br.com.rhscdeveloper.util.Constantes;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MovimentoVeiculoRepository implements PanacheRepositoryBase<MovimentoVeiculoVO, Integer> {
	
	@Inject private MovimentoFinanceiroRepository mfRepository;
	
	public MovimentoFinanceiroVO getMovFinanceiro(MovimentoVeiculoVO mvvo) {
		MovimentoFinanceiroVO mf = mfRepository.find("idMovimento = ?1", mvvo).firstResult();
		return mf;
	}
	
	public List<MovimentoVeiculoVO> findMovsAbertos(){
		
		String sb = "from MovimentoVeiculoVO mv where situacao =: situacao";
		return find(sb.toString(), Sort.by("id"), SituacaoMovimento.ABERTO).list();
	}
	
	public List<MovimentoVeiculoVO> findMovsPaginado(Integer pagina){
		return findAll(Sort.by("id")).page(pagina, Constantes.NRO_MAX_REGISTROS_PAGINACAO).list();
	}
}
