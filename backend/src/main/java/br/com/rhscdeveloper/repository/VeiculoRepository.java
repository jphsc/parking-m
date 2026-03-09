package br.com.rhscdeveloper.repository;

import java.util.List;
import java.util.Optional;

import br.com.rhscdeveloper.model.VeiculoVO;
import br.com.rhscdeveloper.util.Constantes;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VeiculoRepository implements PanacheRepositoryBase<VeiculoVO, Integer> {
	
	public Optional<VeiculoVO> findByPlaca(String placa) {
		return Optional.ofNullable(find("placa", placa).firstResult());
	}
	
	public List<VeiculoVO> findAll(Integer pagina){
		return findAll(Sort.by("id")).page(pagina, Constantes.NRO_MAX_REGISTROS_PAGINACAO).list();
	}
}
