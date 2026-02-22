package br.com.rhscdeveloper.dto;

import br.com.rhscdeveloper.util.Constantes;
import br.com.rhscdeveloper.validation.Placa;
import jakarta.validation.constraints.NotBlank;

public record VeiculoRequestDTO(
		Integer id,
		String modelo,
		String montadora,
		@NotBlank
		@Placa(message = Constantes.MSG_ERRO_TAMANHO_PLACA)
		String placa) 
{
}
