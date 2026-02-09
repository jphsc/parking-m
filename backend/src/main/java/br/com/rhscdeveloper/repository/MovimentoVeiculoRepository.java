package br.com.rhscdeveloper.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.rhscdeveloper.dto.MovimentoVeiculoDTO;
import br.com.rhscdeveloper.model.MovimentoFinanceiroVO;
import br.com.rhscdeveloper.model.MovimentoVeiculoVO;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MovimentoVeiculoRepository implements PanacheRepositoryBase<MovimentoVeiculoVO, Integer> {
	
	@Inject MovimentoFinanceiroRepository mfRepository;
	
	public MovimentoFinanceiroVO getMovFinanceiro(MovimentoVeiculoVO mvvo) {
		MovimentoFinanceiroVO mf = mfRepository.find("idMovimento = ?1", mvvo).firstResult();
		return mf;
	}
	
	public List<MovimentoVeiculoVO> findAll(MovimentoVeiculoDTO dto){
		
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder(" from MovimentoVeiculoVO mv where true");

		if(dto.getDtHrEntrada() != null) {
			sb.append(" and dtHrEntrada =: dtHrEntrada");
			params.put("dtHrEntrada", dto.getDtHrEntrada());
		}
		
		if(dto.getSituacao() != null && dto.getSituacao() != 0) {
			sb.append(" and situacao =: situacao");
			params.put("situacao", dto.getSituacao());
		}
		
		if(dto.getPlaca() != null && !dto.getPlaca().equals("")) {
			sb.append(" and mv.veiculo.placa =: placa");
			params.put("placa", dto.getPlaca());
		}
		
		PanacheQuery<MovimentoVeiculoVO> query = find(sb.toString(), params);
		
		return query.list();
	}
}
