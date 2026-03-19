package br.com.rhscdev.application.dto.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.rhscdev.infrastructure.config.Constantes;
import jakarta.validation.constraints.NotNull;

public record MovimentoVeiculoEncerrar(
		@NotNull(message = Constantes.MSG_REG_FIN_SEM_ID_MOV)
		Integer idMovimento,
		
		Integer idRegra,
		
		@NotNull
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
		LocalDateTime dtHrSaida
) {

}
