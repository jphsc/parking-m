package br.com.rhscdev.domain.repository;

import br.com.rhscdev.domain.entity.MovimentoFinanceiroVO;

public interface IMovimentoFinanceiroRepository {

	MovimentoFinanceiroVO save(MovimentoFinanceiroVO movimento);
	
}
