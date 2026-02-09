package br.com.rhscdeveloper.util;

import br.com.rhscdeveloper.dto.ErroDTO;
import jakarta.transaction.RollbackException;

public class GeracaoException {

	public static ErroDTO mensagemExceptionGenerica(Throwable e) {
		
		ErroDTO dto = new ErroDTO();
		
		if(e.getCause() instanceof RollbackException) {
			dto.setCodigo(Constantes.COD_ERRO_VALIDACAO_REGISTRO);
			dto.setMensagem(Constantes.MSG_ERRO_CAMPOS);
			return dto;
		}
		dto.setCodigo(999);
		dto.setMensagem(e.getMessage());
		return dto;
	}
}
