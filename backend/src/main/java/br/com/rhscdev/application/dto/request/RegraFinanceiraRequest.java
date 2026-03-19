package br.com.rhscdev.application.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.rhscdev.domain.enumerator.Enums.Situacao;
import br.com.rhscdev.domain.enumerator.Enums.TipoCobranca;
import br.com.rhscdev.domain.enumerator.Enums.TipoMovimento;
import br.com.rhscdev.infrastructure.config.Constantes;
import br.com.rhscdev.interfaces.validation.Enumerador;
import br.com.rhscdev.interfaces.validation.OperacaoValidadorGroup.OnCreate;
import br.com.rhscdev.interfaces.validation.OperacaoValidadorGroup.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegraFinanceiraRequest(
		
		@NotNull(groups = OnUpdate.class, message = Constantes.MSG_SEM_ID_REGISTRO)
		Integer id,
		
		@NotBlank(groups = OnCreate.class)
		String descricao,
		
		@NotNull(groups = OnCreate.class)
		BigDecimal valor,
		
		@NotNull(groups = OnCreate.class)
		@Enumerador(tipo = TipoCobranca.class, groups = {OnCreate.class, OnUpdate.class})
		Integer tipoCobranca,
		
		@NotNull(groups = OnCreate.class)
		@Enumerador(tipo = TipoMovimento.class, groups = {OnCreate.class, OnUpdate.class})
		Integer tipoMovimento,
		
		@NotNull(groups = OnCreate.class)
		LocalDate dtInicioValidade,
		
		LocalDate dtFimValidade,
		
		@Enumerador(tipo = Situacao.class, groups = {OnCreate.class, OnUpdate.class})
		Integer situacao
) {
	public RegraFinanceiraRequest{
		descricao = toUpper(descricao);
	}

    private static String toUpper(String value) {
        return value == null ? null : value.toUpperCase();
    }
}
