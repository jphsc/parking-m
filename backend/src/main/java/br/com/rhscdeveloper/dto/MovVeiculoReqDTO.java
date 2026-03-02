package br.com.rhscdeveloper.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.rhscdeveloper.enumerator.Enums.SituacaoMovimento;
import br.com.rhscdeveloper.enumerator.Enums.TipoMovimento;
import br.com.rhscdeveloper.validation.Enumerador;
import br.com.rhscdeveloper.validation.Placa;

public record MovVeiculoReqDTO(
		Integer idMovimento,
		Integer idVeiculo,
		Integer idRegra,
		@Placa
		String placa,
		@Enumerador(tipo = TipoMovimento.class)
		Integer tipoMovimento,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm[:ss][.SSS]")
		LocalDateTime dtHrEntrada,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm[:ss][.SSS]")
		LocalDateTime dtHrSaida,
		@Enumerador(tipo = SituacaoMovimento.class)
		Integer situacao
) {

}
