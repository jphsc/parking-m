package br.com.rhscdev.application.dto.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.rhscdev.domain.enumerator.Enums.TipoMovimento;
import br.com.rhscdev.infrastructure.config.Constantes;
import br.com.rhscdev.interfaces.validation.Enumerador;
import jakarta.validation.constraints.NotNull;

public record MovimentoVeiculoCriar(
		@NotNull(message = Constantes.MSG_SEM_ID_VEICULO)
		Integer idVeiculo,

		Integer idRegra,
		
		@NotNull
		@Enumerador(tipo = TipoMovimento.class)
		Integer tipoMovimento,
		
		@NotNull
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
		LocalDateTime dtHrEntrada
) {
	
}
