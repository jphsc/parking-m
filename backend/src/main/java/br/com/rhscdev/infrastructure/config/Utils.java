package br.com.rhscdev.infrastructure.config;

import java.util.List;
import java.util.Objects;

import br.com.rhscdev.domain.entity.BaseIdentificavel;

public abstract class Utils {

	public static int getNroPaginaConsulta(Integer nroPagina) {
		int aux = nroPagina == null ? 0 : nroPagina;
    	return aux <= 0 ? 0 : aux - 1;
    }
    
    public static int getNroPaginaResposta(Integer nroPagina) {
    	Integer aux = Objects.isNull(nroPagina) ? 0 : nroPagina;
    	return aux <= 0 ? 1 : aux + 1;
    }
    
	public static <T extends BaseIdentificavel> String getMensagemBuscaRegistro(List<T> dto) {
		return dto.isEmpty() ? Constantes.MSG_REGISTROS_NAO_ENCONTRADOS : Constantes.MSG_REGISTROS_ENCONTRADOS;
	}
}
