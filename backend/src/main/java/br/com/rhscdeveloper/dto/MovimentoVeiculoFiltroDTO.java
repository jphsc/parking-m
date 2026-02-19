package br.com.rhscdeveloper.dto;

import java.time.LocalDateTime;

public record MovimentoVeiculoFiltroDTO(
		Integer idMovimento, Integer idVeiculo, Integer idRegra, String placa, Integer tipoMovimento, LocalDateTime dtHrEntrada,
		LocalDateTime dtHrSaida, Integer situacao, Integer pagina) {

}
