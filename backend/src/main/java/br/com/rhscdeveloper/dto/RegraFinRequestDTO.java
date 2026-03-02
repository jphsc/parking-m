package br.com.rhscdeveloper.dto;

import java.time.LocalDate;

import br.com.rhscdeveloper.enumerator.Enums.Situacao;
import br.com.rhscdeveloper.enumerator.Enums.TipoCobranca;
import br.com.rhscdeveloper.enumerator.Enums.TipoMovimento;
import br.com.rhscdeveloper.validation.Enumerador;

public record RegraFinRequestDTO(
		Integer id, 
		String descricao, 
		Double valor,
		@Enumerador(tipo = TipoCobranca.class)
		Integer tipoCobranca,
		@Enumerador(tipo = TipoMovimento.class)
		Integer tipoMovimento,
		LocalDate dtInicioValidade, 
		LocalDate dtFimValidade, 
		@Enumerador(tipo = Situacao.class)
		Integer situacao
	) {
	
}
