package br.com.rhscdeveloper.endpoint;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.com.rhscdeveloper.dto.RegraFinancReqDTO;
import br.com.rhscdeveloper.service.RegraFinanceiraService;
import br.com.rhscdeveloper.validation.OperacaoValidadorGroup.OnCreate;
import br.com.rhscdeveloper.validation.OperacaoValidadorGroup.OnUpdate;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Tag(name = "Regra financeira")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/regras-financeiras")
public class RegraFinanceiraResource {

	@Inject
	private RegraFinanceiraService service;
	
	@POST
	@Operation(summary = "Cadastrar regra financeira", description = "Cadastrar uma nova regra financeira")
	public Response cadastrarRegraFinanceira(@RequestBody @Valid @ConvertGroup(from = Default.class, to = OnCreate.class) RegraFinancReqDTO request) {
		return Response.status(Status.CREATED).entity(service.cadastrarRegraFinanceira(request)).build();
	}

	@GET
	@Operation(summary = "Obtém uma regra financeira", description = "Obtém uma regra financeira pelo id da regra")
	@Path("/{id}")
	public Response obterRegraFinanceira(@Parameter(description = "Identificador da regra") @PathParam("id") Integer id) {
		return Response.status(Status.OK).entity(service.obterRegraFinanceiraById(id)).build();
	}

	@GET
	@Operation(summary = "Obtém regras financeiras", description = "Obtém regras financeiras conforme paginação")
	public Response obterRegraFinanceiras(@Parameter(description = "Número da página", required = true) @QueryParam(value = "pagina") Integer pagina) {
		return Response.status(Status.OK).entity(service.obterRegrasFinanceiras(pagina)).build();
	}

	@PUT
	@Operation(summary = "Atualiza regra financeira", description = "Atualiza uma regra financeira")
	public Response atualizarRegraFinanceira(@RequestBody @Valid @ConvertGroup(from = Default.class, to = OnUpdate.class) RegraFinancReqDTO request) {
		return Response.status(Status.OK).entity(service.atualizarRegraFinanceira(request)).build();
	}

	@DELETE
	@Path("/{id}")
	@Operation(summary = "Exclui uma regra financeira", description = "Remove uma regra financeira sem movimento(s) vinculado(s)")
	public Response deletarRegraFinanceira(@PathParam("id") Integer id) {
		return Response.status(Status.OK).entity(service.deletarRegraFinanceira(id)).build();
	}
}
