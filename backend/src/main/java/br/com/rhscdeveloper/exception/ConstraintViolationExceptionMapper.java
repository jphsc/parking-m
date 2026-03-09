package br.com.rhscdeveloper.exception;

import org.jboss.logging.Logger;

import br.com.rhscdeveloper.dto.ErroDTO;
import br.com.rhscdeveloper.util.Constantes;
import jakarta.annotation.Priority;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.USER)
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

	private static final Logger LOG = Logger.getLogger(ConstraintViolationExceptionMapper.class);
	
	@Override
    public Response toResponse(ConstraintViolationException exception) {
        LOG.info("Erro de validação: ", exception);
        
        System.out.println(exception.getConstraintViolations());
        
        String mensagem = exception.getConstraintViolations()
        		.stream()
        		.findFirst()
        		.map(violation -> String.format("%s: %s", getFieldName(violation.getPropertyPath().toString()), violation.getMessage()))
        		.orElse(Constantes.MSG_ERRO_GENERICO);
        
        ErroDTO erroDTO = new ErroDTO(Constantes.COD_ERRO_VALIDACAO_REGISTRO, mensagem);
        
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(erroDTO)
                .build();
    }
    
    private String getFieldName(String path) {
        String[] props = path.split("\\.");
        return props.length > 0 ? props[props.length - 1] : path;
    }
}
