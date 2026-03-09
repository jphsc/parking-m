package br.com.rhscdeveloper.dto;

import java.time.LocalDate;

import br.com.rhscdeveloper.enumerator.Enums.Situacao;
import br.com.rhscdeveloper.enumerator.Enums.TipoCobranca;
import br.com.rhscdeveloper.enumerator.Enums.TipoMovimento;
import br.com.rhscdeveloper.util.Constantes;
import br.com.rhscdeveloper.validation.Enumerador;
import br.com.rhscdeveloper.validation.OperacaoValidadorGroup.OnCreate;
import br.com.rhscdeveloper.validation.OperacaoValidadorGroup.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegraFinancReqDTO(
		
		@NotNull(groups = OnUpdate.class, message = Constantes.MSG_SEM_ID_REGISTRO)
		Integer id,
		
		@NotBlank(groups = OnCreate.class)
		String descricao,
		
		@NotNull(groups = OnCreate.class)
		Double valor,
		
		@NotNull(groups = OnCreate.class)
		@Enumerador(tipo = TipoCobranca.class, groups = {OnCreate.class, OnUpdate.class})
		Integer tipoCobranca,
		
		@NotNull(groups = OnCreate.class)
		@Enumerador(tipo = TipoMovimento.class, groups = {OnCreate.class, OnUpdate.class})
		Integer tipoMovimento,
		
		@NotNull(groups = OnCreate.class)
		LocalDate dtInicioValidade,
		
		@NotNull(groups = OnUpdate.class)
		LocalDate dtFimValidade,
		
		@Enumerador(tipo = Situacao.class, groups = {OnCreate.class, OnUpdate.class})
		Integer situacao
	) 
{
	public RegraFinancReqDTO{
		descricao = toUpper(descricao);
	}

    private static String toUpper(String value) {
        return value == null ? null : value.toUpperCase();
    }
}
