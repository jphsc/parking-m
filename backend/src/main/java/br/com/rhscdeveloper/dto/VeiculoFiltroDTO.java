package br.com.rhscdeveloper.dto;

import java.time.LocalDate;

public record VeiculoFiltroDTO(Integer id, String modelo, String montadora, LocalDate dtRegistro, String placa, Integer pagina) {
	
}
