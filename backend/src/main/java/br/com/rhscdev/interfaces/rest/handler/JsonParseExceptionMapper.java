package br.com.rhscdev.interfaces.rest.handler;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonParseException;

import br.com.rhscdev.infrastructure.config.Constantes;
import br.com.rhscdev.interfaces.rest.dto.ErroResponse;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.USER)
public class JsonParseExceptionMapper implements ExceptionMapper<JsonParseException> {

	private static final Logger LOG = Logger.getLogger(GlobalExceptionMapper.class);
	
	@Override
	public Response toResponse(JsonParseException exception) {
		
		LOG.warn(exception.getMessage());
		ErroResponse resp = new ErroResponse(Constantes.COD_VALIDACAO_METHOD, Constantes.MSG_REQUEST_INVALIDA);
		
		return Response.status(Status.BAD_REQUEST).entity(resp).build();
	}
}
