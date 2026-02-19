package br.com.rhscdeveloper.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record RegraFinanceiraFiltroDTO(Integer id, String descricao, Double valor, Integer tipoCobranca, Integer tipoMovimento,
		LocalDate dtInicioValidade, LocalDate dtFimValidade, Integer situacao, LocalDateTime versao, Integer pagina) {
	
}
