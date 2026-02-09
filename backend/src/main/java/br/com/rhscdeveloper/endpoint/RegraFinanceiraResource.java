package br.com.rhscdeveloper.endpoint;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.com.rhscdeveloper.dto.RegraFinanceiraDTO;
import br.com.rhscdeveloper.service.RegraFinanceiraService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Tag(name = "regra financeira")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/regra-financeira")
public class RegraFinanceiraResource {

	@Inject
	private RegraFinanceiraService service;
	
	@Operation(summary = "Cadastrar regra financeira", description = "Cadastrar uma nova regra financeira")
	@POST
	@Path("/cadastrar")
	public Response cadastrarRegraFinanceira(@RequestBody RegraFinanceiraDTO RegraFinanceira) {
		return Response.status(Status.CREATED).entity(service.cadastrarRegraFinanceira(RegraFinanceira)).build();
	}

	@Operation(summary = "Obtém uma regra financeira", description = "Obtém uma regra financeira pelo id da regra")
	@GET
	@Path("/{idRegra}")
	public Response obterRegraFinanceira(@Parameter(description = "Identificador da regra") @PathParam("idRegra") Integer id) {
		return Response.status(Status.OK).entity(service.obterRegraFinanceiraById(id)).build();
	}

	// TODO Implementar paginação
	@Operation(summary = "Obtém regras financeiras", description = "Obtém regras financeiras conforme paginação")
	@GET
	@Path("/regras")
	public Response obterRegraFinanceiras() {
		return Response.status(Status.OK).entity(service.obterRegrasFinanceiras()).build();
	}

	@Operation(summary = "Obtém regras financeiras por filtro", description = "Obtém regras financeiras por filtro")
	@POST
	public Response obterRegraFinanceiras(RegraFinanceiraDTO filtro) {
		return Response.status(Status.OK).entity(service.obterRegraFinanceiraFiltro(filtro)).build();
	}

	@Operation(summary = "Atualiza regra financeira", description = "Atualiza uma regra financeira")
	@PUT
	@Path("/atualizar")
	public Response atualizarRegraFinanceira(@RequestBody RegraFinanceiraDTO filtro) {
		return Response.status(Status.OK).entity(service.atualizarRegraFinanceira(filtro)).build();
	}

	@Operation(summary = "Exclui uma regra financeira", description = "Remove uma regra financeira sem movimento(s) vinculado(s)")
	@DELETE
	@Path("/{idRegra}")
	public Response deletarRegraFinanceira(@PathParam("idRegra") Integer id) {
		return Response.status(Status.OK).entity(service.deletarRegraFinanceira(id)).build();
	}
}
