package br.com.rhscdeveloper.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.rhscdeveloper.dto.RegraFinanceiraFiltroDTO;
import br.com.rhscdeveloper.model.RegraFinanceiraVO;
import br.com.rhscdeveloper.util.Constantes;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RegraFinanceiraRepository implements PanacheRepositoryBase<RegraFinanceiraVO, Integer> {
	
	public List<RegraFinanceiraVO> findAll(Integer pagina){
		return findAll(Sort.by("id")).page(pagina, Constantes.NRO_MAX_REGISTROS_PAGINACAO).list();
	}
	
	public List<RegraFinanceiraVO> findAll(RegraFinanceiraFiltroDTO filtro){
		Map<String, Object> parametros = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder("1 = 1");
		Integer pagina = 0;
		
		if(filtro.pagina() != null) {
			pagina = filtro.pagina() <= 0 ? 0 : filtro.pagina() - 1;
		}
		
		if(filtro.id() != null && filtro.id() > 0) {
			sb.append(" and id =: id");
			parametros.put("id", filtro.id());
		}
		
		if(filtro.descricao() != null && !filtro.descricao().isBlank()) {
			sb.append(" and descricao =: descricao");
			parametros.put("descricao", filtro.descricao().toUpperCase());
		}
		
		if(filtro.valor() != null && filtro.valor() > 0) {
			sb.append(" and valor =: valor");
			parametros.put("valor", filtro.valor());
		}
		
		if(filtro.tipoCobranca() != null) {
			sb.append(" and tipoCobranca =: tipoCobranca");
			parametros.put("tipoCobranca", filtro.tipoCobranca());
		}
		
		if(filtro.tipoMovimento() != null) {
			sb.append(" and tipoMovimento =: tipoMovimento");
			parametros.put("tipoMovimento", filtro.tipoMovimento());
		}
		
		if(filtro.dtInicioValidade() != null) {
			sb.append(" and dtInicioValidade =: dtInicioValidade");
			parametros.put("dtInicioValidade", filtro.dtInicioValidade());
		}
		
		if(filtro.dtFimValidade() != null) {
			sb.append(" and dtFimValidade =: dtFimValidade");
			parametros.put("dtFimValidade", filtro.dtFimValidade());
		}
		
		if(filtro.situacao() != null) {
			sb.append(" and situacao =: situacao");
			parametros.put("situacao", filtro.situacao());
		}
		
		return find(sb.toString(), Sort.by("id"), parametros).page(pagina, Constantes.NRO_MAX_REGISTROS_PAGINACAO).list();
	}
}
