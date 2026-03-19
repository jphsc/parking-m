package br.com.rhscdev.domain.exception;

import br.com.rhscdev.infrastructure.config.Constantes;

public class EntityNotFoundException extends DomainException {

	private static final long serialVersionUID = 1L;

	public EntityNotFoundException(String entity, Object id) {
        super(Constantes.COD_INEXISTENTE, String.format(Constantes.REG_ID_NAO_ENCONTRADO, entity, id));
    }
    
    public EntityNotFoundException(String message) {
        super(Constantes.COD_INEXISTENTE, message);
    }
	
}
