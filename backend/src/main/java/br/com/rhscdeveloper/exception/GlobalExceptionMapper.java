package br.com.rhscdeveloper.exception;

import java.util.NoSuchElementException;

import org.hibernate.PropertyValueException;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonParseException;

import br.com.rhscdeveloper.dto.ErroDTO;
import br.com.rhscdeveloper.util.Constantes;
import jakarta.ws.rs.NotAllowedException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

	private static final Logger LOG = Logger.getLogger(GlobalExceptionMapper.class);
	
	@Override
	public Response toResponse(Throwable exception) {
		
		ErroDTO erroDTO;
        Status statusCode = Status.INTERNAL_SERVER_ERROR;
        Integer codigoErro = Constantes.COD_ERRO_INTERNO;
        String mensagem = "Erro interno no servidor";
        
        if (exception instanceof GlobalException globalEx) {
            codigoErro = globalEx.getCodigoErro();
            mensagem = globalEx.getMessage();
            statusCode = mapGlobalExceptionStatus(codigoErro);
            
        } else if (exception instanceof NoSuchElementException) {
            codigoErro = Constantes.COD_ERRO_INEXISTENTE;
            mensagem = exception.getMessage() != null ? exception.getMessage() : "Registro não encontrado";
            statusCode = Status.NOT_FOUND;
            
        } else if (exception instanceof NotFoundException) {
            codigoErro = Constantes.COD_ERRO_INEXISTENTE;
            mensagem = "Recurso não encontrado";
            statusCode = Status.NOT_FOUND;
            
        } else if (exception instanceof NotAllowedException) {
            codigoErro = Constantes.COD_ERRO_VALIDACAO_METHOD;
            mensagem = "Método HTTP não permitido para este recurso";
            statusCode = Status.METHOD_NOT_ALLOWED;
            
        } else if (exception instanceof PropertyValueException) {
            codigoErro = Constantes.COD_ERRO_VALIDACAO_REGISTRO;
            mensagem = "Campo obrigatório não preenchido";
            statusCode = Status.BAD_REQUEST;
            
        } else if (exception instanceof IllegalArgumentException) {
        	LOG.error("IllegalArgumentException interna: ", exception);
            codigoErro = Constantes.COD_ERRO_VALIDACAO_REGISTRO;
            mensagem = exception.getMessage() != null ? exception.getMessage() : "Parâmetro inválido";
            statusCode = Status.BAD_REQUEST;
            
        } else if (exception instanceof NullPointerException) {
            LOG.error("NullPointerException interna: ", exception);
            codigoErro = Constantes.COD_ERRO_VALIDACAO_REGISTRO;
            mensagem = exception.getMessage() != null ? exception.getMessage() :Constantes.MSG_ERRO_CAMPOS;
            statusCode = Status.BAD_REQUEST;
            
        } else if (exception instanceof RegistroNaoEncontradoException) {
            LOG.error("RegistroNaoEncontradoException interna: ", exception);
            codigoErro = Constantes.COD_ERRO_VALIDACAO_REGISTRO;
            mensagem = exception.getMessage();
            statusCode = Status.NOT_FOUND;
            
        } else if (exception instanceof ValidacaoConstraintException e) {
            LOG.error("ValidacaoConstraintException interna: ", exception);
            codigoErro = Constantes.COD_ERRO_VALIDACAO_REGISTRO;
            mensagem = e.getMessage();
            statusCode = Status.CONFLICT;
            
        } else if (exception instanceof ProcessingException) {
            
        	Throwable causa = exception.getCause();
            if (causa instanceof JsonParseException) {
                LOG.error("Erro de parsing JSON: ", causa);
                codigoErro = Constantes.COD_ERRO_VALIDACAO_METHOD;
                mensagem = "Erro no formato JSON do corpo da requisição. Verifique a sintaxe.";
                statusCode = Status.BAD_REQUEST;
                
            } else {
                LOG.error("ProcessingException: ", exception);
                codigoErro = Constantes.COD_ERRO_VALIDACAO_METHOD;
                mensagem = "Erro no processamento da requisição";
                statusCode = Status.BAD_REQUEST;
            }
        } else {
            LOG.error("Exceção não tratada: ", exception);
            codigoErro = Constantes.COD_ERRO_INTERNO;
            mensagem = "Ocorreu um erro inesperado. Tente novamente mais tarde.";
            statusCode = Status.INTERNAL_SERVER_ERROR;
        } 
        
        erroDTO = new ErroDTO(codigoErro, mensagem);
        
        return Response.status(statusCode)
                .entity(erroDTO)
                .build();
    }

	private Status mapGlobalExceptionStatus(Integer codigoErro) {
        switch (codigoErro) {
            case Constantes.COD_ERRO_INEXISTENTE:
                return Status.NOT_FOUND;

            case Constantes.COD_ERRO_VALIDACAO_REGISTRO:
                return Status.BAD_REQUEST;
                
            case Constantes.COD_ERRO_VALIDACAO_METHOD:
                return Status.METHOD_NOT_ALLOWED;
                
            case Constantes.COD_ERRO_INTERNO:
            default:
                return Status.INTERNAL_SERVER_ERROR;
        }
    }
}
