package br.com.rhscdeveloper.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.rhscdeveloper.dto.VeiculoDTO;
import br.com.rhscdeveloper.model.VeiculoVO;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VeiculoRepository implements PanacheRepositoryBase<VeiculoVO, Integer> {

	public VeiculoVO findByPlaca(String placa) {
		return find("placa", placa).firstResult();
	}
	
	public List<VeiculoVO> findByMontadora(String montadora) {
		return find("montadora like ?1", "".concat("%").concat(montadora).concat("%")).list();
	}
	
	// TODO Paginar	
	public List<VeiculoVO> findAll(VeiculoDTO filtro){
		Map<String, Object> parametros = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder("1 = 1");
		
		if(filtro.getId() != null && filtro.getId() != 0) {
			sb.append(" and id =: id");
			parametros.put("id", filtro.getId());
		}
		
		if(filtro.getModelo() != null && !filtro.getModelo().isBlank()) {
			sb.append(" and modelo like :modelo");
			parametros.put("modelo", filtro.getModelo());
		}
		
		if(filtro.getMontadora() != null && !filtro.getMontadora().isBlank()) {
			sb.append(" and montadora like :montadora");
			parametros.put("montadora", filtro.getMontadora());
		}
		
		if(filtro.getPlaca() != null && !filtro.getPlaca().isBlank()) {
			sb.append(" and placa =: placa");
			parametros.put("placa", filtro.getPlaca().toUpperCase());
		}
		
		if(filtro.getDtRegistro() != null) {
			sb.append(" and dtRegistro =: dtRegistro");
			parametros.put("dtRegistro", filtro.getDtRegistro());
		}
		
		PanacheQuery<VeiculoVO> query = find(sb.toString(), parametros);
		
		List<VeiculoVO> veiculos = query.list();
		Collections.sort(veiculos);
		return veiculos;
	}
}
