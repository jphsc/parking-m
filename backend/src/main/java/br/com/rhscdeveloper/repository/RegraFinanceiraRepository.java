package br.com.rhscdeveloper.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.rhscdeveloper.dto.RegraFinanceiraDTO;
import br.com.rhscdeveloper.model.RegraFinanceiraVO;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RegraFinanceiraRepository implements PanacheRepositoryBase<RegraFinanceiraVO, Integer> {

	public List<RegraFinanceiraVO> findByDescricao(String descricao) {
		return find("descricao like ?1", "".concat("%").concat(descricao).concat("%")).list();
	}
	
	// TODO Paginar	
	public List<RegraFinanceiraVO> findAll(RegraFinanceiraDTO filtro){
		Map<String, Object> parametros = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder("1 = 1");
		
		if(filtro.getDescricao() != null && !filtro.getDescricao().isBlank()) {
			sb.append(" and descricao =: descricao");
			parametros.put("descricao", filtro.getDescricao());
		}
		
		if(filtro.getValor() != null && filtro.getValor() > 0) {
			sb.append(" and valor =: valor");
			parametros.put("valor", filtro.getValor());
		}
		
		if(filtro.getTipoCobranca() != null && filtro.getTipoCobranca() > 0) {
			sb.append(" and tipoCobranca =: tipoCobranca");
			parametros.put("tipoCobranca", filtro.getTipoCobranca());
		}
		
		if(filtro.getTipoMovimento() != null && filtro.getTipoMovimento() > 0) {
			sb.append(" and tipoMovimento =: tipoMovimento");
			parametros.put("tipoMovimento", filtro.getTipoMovimento());
		}
		
		if(filtro.getDtFimValidade() != null) {
			sb.append(" and dtFimValidade =: dtFimValidade");
			parametros.put("dtFimValidade", filtro.getDtFimValidade());
		}
		
		if(filtro.getSituacao() != null && filtro.getSituacao() != 0) {
			sb.append(" and situacao =: situacao");
			parametros.put("situacao", filtro.getSituacao());
		}
		
		PanacheQuery<RegraFinanceiraVO> query = find(sb.toString(), parametros);
		
		return query.list();
	}
}
