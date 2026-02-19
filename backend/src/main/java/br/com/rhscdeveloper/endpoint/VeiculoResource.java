package br.com.rhscdeveloper.endpoint;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.com.rhscdeveloper.dto.VeiculoFiltroDTO;
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

@Tag(name = "Veiculo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/veiculo")
public class VeiculoResource {

	@Inject
	private VeiculoService service;
	
	@Operation(summary = "Cadastra um veículo", description = "Cadastra um novo veículo")
	@POST
	@Path("/cadastrar")
	public Response cadastrarVeiculo(@RequestBody VeiculoFiltroDTO veiculo) {
		return Response.status(Status.CREATED).entity(service.cadastrarVeiculo(veiculo)).build();
	}

	@Operation(summary = "Obtém um veículo", description = "Obtém um veículo pelo identificador do veículo")
	@GET
	@Path("/{idVeiculo}")
	public Response obterVeiculo(@Parameter(description = "Identificador do veículo") @PathParam("idVeiculo") Integer id) {
		return Response.status(Status.OK).entity(service.obterVeiculoById(id)).build();
	}

	@Operation(summary = "Obtém veículos", description = "Obtém todos os veículos, conforme paginação")
	@GET
	@Path("/veiculos")
	public Response obterVeiculos(@Valid @Parameter(description = "Número da página", required = true) @QueryParam(value = "pagina") Integer pagina) {
		return Response.status(Status.OK).entity(service.obterVeiculos(pagina)).build();
	}

	@Operation(summary = "Obter veículos", description = "Obtém os veículos conforme o filtro")
	@POST
	public Response obterVeiculos(VeiculoFiltroDTO filtro) {
		return Response.status(Status.OK).entity(service.obterVeiculosFiltro(filtro)).build();
	}

	@Operation(summary = "Atualizar veículo", description = "Atualiza um veículo")
	@PUT
	@Path("/atualizar")
	public Response atualizarVeiculo(@RequestBody VeiculoFiltroDTO filtro) {
		return Response.status(Status.OK).entity(service.atualizarVeiculo(filtro)).build();
	}

	@Operation(summary = "Exclui um veículo", description = "Remove um veículo sem movimento(s) vinculado(s)")
	@DELETE
	@Path("/{idVeiculo}")
	public Response deletarVeiculo(@Parameter(description = "Identificador do veículo") @PathParam("idVeiculo") Integer id) {
		return Response.status(Status.OK).entity(service.deletarVeiculo(id)).build();
	}
}
