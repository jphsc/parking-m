package br.com.rhscdeveloper.util;

import static java.util.Objects.isNull;

import java.util.List;

import br.com.rhscdeveloper.model.BaseIdentificavel;

public abstract class Utils {

	public static Integer getNroPaginaConsulta(Integer nroPagina) {
    	Integer aux = isNull(nroPagina) ? 0 : nroPagina;
    	return aux <= 0 ? 0 : aux - 1;
    }
    
    public static Integer getNroPaginaResp(Integer nroPagina) {
    	Integer aux = isNull(nroPagina) ? 0 : nroPagina;
    	return aux <= 0 ? 1 : aux + 1;
    }
    
	public static <T extends BaseIdentificavel> String getMensagemBuscaRegistro(T objeto) {
		return isNull(objeto) ? Constantes.MSG_REGISTROS_NAO_ENCONTRADOS : Constantes.MSG_REGISTROS_ENCONTRADOS;
	}
    
	public static <T extends BaseIdentificavel> String getMensagemBuscaRegistro(List<T> objeto) {
		return objeto.isEmpty() ? Constantes.MSG_REGISTROS_NAO_ENCONTRADOS : Constantes.MSG_REGISTROS_ENCONTRADOS;
	}
}
