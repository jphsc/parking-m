package br.com.rhscdeveloper.endpoint;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.com.rhscdeveloper.dto.VeiculoRequestDTO;
import br.com.rhscdeveloper.service.VeiculoService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
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

@Tag(name = "Veiculos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/veiculos")
public class VeiculoResource {

	@Inject
	private VeiculoService service;

	@GET
	@Path("/{id}")
	@Operation(summary = "Obtém um veículo", description = "Obtém um veículo pelo identificador do veículo")
	public Response obterVeiculo(@Parameter(description = "Identificador do veículo") @PathParam("id") Integer id) {
		return Response.status(Status.OK).entity(service.obterVeiculoById(id)).build();
	}

	@GET
	@Operation(summary = "Obtém veículos", description = "Obtém todos os veículos, conforme paginação")
	public Response obterVeiculos(@Parameter(description = "Número da página", required = true) @QueryParam(value = "pagina") Integer pagina) {
		return Response.status(Status.OK).entity(service.obterVeiculos(pagina)).build();
	}
	
	@POST
	@Operation(summary = "Cadastra um veículo", description = "Cadastra um novo veículo")
	public Response cadastrarVeiculo(@RequestBody @Valid VeiculoRequestDTO veiculo) {
		return Response.status(Status.CREATED).entity(service.cadastrarVeiculo(veiculo)).build();
	}

	@PUT
	@Operation(summary = "Atualizar veículo", description = "Atualiza um veículo")
	public Response atualizarVeiculo(@RequestBody @Valid VeiculoRequestDTO filtro) {
		return Response.ok(service.atualizarVeiculo(filtro)).build();
	}

	@DELETE
	@Path("/{id}")
	@Operation(summary = "Exclui um veículo", description = "Remove um veículo sem movimento(s) vinculado(s)")
	public Response deletarVeiculo(@Parameter(description = "Identificador do veículo") @PathParam("id") Integer id) {
		return Response.status(Status.OK).entity(service.deletarVeiculo(id)).build();
	}
}
