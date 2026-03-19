package br.com.rhscdev.application.dto.request;

import br.com.rhscdev.infrastructure.config.Constantes;
import br.com.rhscdev.interfaces.validation.Placa;
import br.com.rhscdev.interfaces.validation.OperacaoValidadorGroup.OnCreate;
import br.com.rhscdev.interfaces.validation.OperacaoValidadorGroup.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VeiculoRequest(
		@NotNull(groups = OnUpdate.class, message = Constantes.MSG_SEM_ID_REGISTRO)
		Integer id,
		
		@NotBlank(groups = OnCreate.class, message = Constantes.MSG_SEM_MODELO)
		String modelo,
		
		@NotBlank(groups = OnCreate.class, message = Constantes.MSG_SEM_MONTADORA)
		String montadora,

		@Placa(groups = {OnCreate.class, OnUpdate.class}, message = Constantes.MSG_ERRO_TAMANHO_PLACA)
		String placa
) {
	public VeiculoRequest {
        modelo = toUpper(modelo);
        montadora = toUpper(montadora);
        placa = toUpper(placa);
    }

    private static String toUpper(String value) {
        return value == null ? null : value.toUpperCase();
    }
}
