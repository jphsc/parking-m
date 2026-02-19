package br.com.rhscdeveloper.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.rhscdeveloper.dto.VeiculoFiltroDTO;
import br.com.rhscdeveloper.model.VeiculoVO;
import br.com.rhscdeveloper.util.Constantes;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VeiculoRepository implements PanacheRepositoryBase<VeiculoVO, Integer> {

	public VeiculoVO findByPlaca(String placa) {
		return find("placa", placa.toUpperCase()).firstResult();
	}
	
	public List<VeiculoVO> findAll(Integer pagina){
		return findAll(Sort.by("id")).page(pagina, Constantes.NRO_MAX_REGISTROS_PAGINACAO).list();
	}
	
	public List<VeiculoVO> findAll(VeiculoFiltroDTO filtro){
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
		
		if(filtro.modelo() != null && !filtro.modelo().isBlank()) {
			sb.append(" and modelo ilike :modelo");
			parametros.put("modelo", filtro.modelo().toUpperCase());
		}
		
		if(filtro.montadora() != null && !filtro.montadora().isBlank()) {
			sb.append(" and montadora ilike :montadora");
			parametros.put("montadora", filtro.montadora().toUpperCase());
		}
		
		if(filtro.placa() != null && !filtro.placa().isBlank()) {
			sb.append(" and placa =: placa");
			parametros.put("placa", filtro.placa().toUpperCase());
		}
		
		if(filtro.dtRegistro() != null) {
			sb.append(" and dtRegistro =: dtRegistro");
			parametros.put("dtRegistro", filtro.dtRegistro());
		}

		return find(sb.toString(), Sort.by("id"), parametros).page(pagina, Constantes.NRO_MAX_REGISTROS_PAGINACAO).list();
	}
}
