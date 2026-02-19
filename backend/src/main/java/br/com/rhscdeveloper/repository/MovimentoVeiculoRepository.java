package br.com.rhscdeveloper.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.rhscdeveloper.dto.MovimentoVeiculoFiltroDTO;
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
	
	public List<MovimentoVeiculoVO> findAll(MovimentoVeiculoFiltroDTO dto){
		
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder(" from MovimentoVeiculoVO mv where true");
		Integer pagina = 0;
		
		if(dto.pagina() != null) {
			pagina = dto.pagina() <= 0 ? 0 : dto.pagina() - 1;
		}

		if(dto.idMovimento() != null) {
			sb.append(" and id =: id");
			params.put("id", dto.idMovimento());
		}
		
		if(dto.idVeiculo() != null) {
			sb.append(" and idVeiculo =: idVeiculo");
			params.put("idVeiculo", dto.idVeiculo());
		}
		
		if(dto.idRegra() != null) {
			sb.append(" and idRegra =: idRegra");
			params.put("idRegra", dto.idRegra());
		}
		
		if(dto.tipoMovimento() != null) {
			sb.append(" and tipoMovimento =: tipoMovimento");
			params.put("tipoMovimento", dto.tipoMovimento());
		}
		
		if(dto.dtHrEntrada() != null) {
			sb.append(" and dtHrEntrada =: dtHrEntrada");
			params.put("dtHrEntrada", dto.dtHrEntrada());
		}
		
		if(dto.dtHrSaida() != null) {
			sb.append(" and dtHrSaida =: dtHrSaida");
			params.put("dtHrSaida", dto.dtHrSaida());
		}
		
		if(dto.situacao() != null && dto.situacao() != 0) {
			sb.append(" and situacao =: situacao");
			params.put("situacao", dto.situacao());
		}
		
		if(dto.placa() != null && !dto.placa().isBlank()) {
			sb.append(" and mv.veiculo.placa ilike :placa");
			params.put("placa", dto.placa());
		}
		
		return find(sb.toString(), Sort.by("id"), params).page(pagina, Constantes.NRO_MAX_REGISTROS_PAGINACAO).list();
	}
}
