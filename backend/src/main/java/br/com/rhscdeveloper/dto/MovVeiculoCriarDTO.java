package br.com.rhscdeveloper.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.rhscdeveloper.enumerator.Enums.TipoMovimento;
import br.com.rhscdeveloper.util.Constantes;
import br.com.rhscdeveloper.validation.Enumerador;
import jakarta.validation.constraints.NotNull;

public record MovVeiculoCriarDTO(
		@NotNull(message = Constantes.MSG_SEM_ID_VEICULO)
		Integer idVeiculo,

		Integer idRegra,
		
		@NotNull
		@Enumerador(tipo = TipoMovimento.class)
		Integer tipoMovimento,
		
		@NotNull
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
		LocalDateTime dtHrEntrada
	) 
{
	
}
