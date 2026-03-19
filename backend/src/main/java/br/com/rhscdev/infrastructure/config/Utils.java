package br.com.rhscdev.infrastructure.config;

import static java.util.Objects.isNull;

import java.util.List;

import br.com.rhscdev.domain.entity.BaseIdentificavel;

public abstract class Utils {

	public static int getNroPaginaConsulta(Integer nroPagina) {
    	return nroPagina <= 0 ? 0 : nroPagina - 1;
    }
    
    public static int getNroPaginaResp(Integer nroPagina) {
    	Integer aux = isNull(nroPagina) ? 0 : nroPagina;
    	return aux <= 0 ? 1 : aux + 1;
    }
    
	public static <T extends BaseIdentificavel> String getMensagemBuscaRegistro(List<T> objeto) {
		return objeto.isEmpty() ? Constantes.MSG_REGISTROS_NAO_ENCONTRADOS : Constantes.MSG_REGISTROS_ENCONTRADOS;
	}
}
