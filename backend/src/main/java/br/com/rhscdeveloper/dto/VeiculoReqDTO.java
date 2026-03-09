package br.com.rhscdeveloper.dto;

import br.com.rhscdeveloper.util.Constantes;
import br.com.rhscdeveloper.validation.Placa;
import br.com.rhscdeveloper.validation.OperacaoValidadorGroup.OnCreate;
import br.com.rhscdeveloper.validation.OperacaoValidadorGroup.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VeiculoReqDTO(
		@NotNull(groups = OnUpdate.class, message = Constantes.MSG_SEM_ID_REGISTRO)
		Integer id,
		
		@NotBlank(groups = OnCreate.class, message = Constantes.MSG_SEM_MODELO)
		String modelo,
		
		@NotBlank(groups = OnCreate.class, message = Constantes.MSG_SEM_MONTADORA)
		String montadora,

		@Placa(groups = {OnCreate.class, OnUpdate.class}, message = Constantes.MSG_ERRO_TAMANHO_PLACA)
		String placa) 
{
	public VeiculoReqDTO {
        modelo = toUpper(modelo);
        montadora = toUpper(montadora);
        placa = toUpper(placa);
    }

    private static String toUpper(String value) {
        return value == null ? null : value.toUpperCase();
    }
}
