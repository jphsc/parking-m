package br.com.rhscdeveloper.endpoint;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.com.rhscdeveloper.dto.MovimentoVeiculoDTO;
import br.com.rhscdeveloper.service.MovimentoVeiculoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Tag(name = "movimento veículo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/movimento-veiculo")
public class MovimentoVeiculoResource {

	@Inject
	private MovimentoVeiculoService service;
	
	@Operation(summary = "Cadastrar movimento de veículo", description = "Cadastrar um movimento de veículo")
	@POST
	@Path("/cadastrar")
	public Response cadastarMovVeiculo(@RequestBody MovimentoVeiculoDTO dto) {
		return Response.status(Status.OK).entity(service.criarMovimentoVeiculo(dto)).build();
	}
	
	@Operation(summary = "Obtém um movimento de veículo", description = "Obtém um movimento de veículo pelo id do movimento")
	@GET
	@Path("/{id}")
	public Response obterMovVeiculo(@PathParam("id") Integer id) {
		return Response.status(Status.OK).entity(service.obterMovVeiculoById(id)).build();
	}

	@Operation(summary = "Obtém movimento de veículo", description = "Obtém movimentos de veículo")
	@GET
	@Path("/movimentos")
	public Response obterMovVeiculos() {
		return Response.status(Status.OK).entity(service.obterMovsVeiculo()).build();
	}

	@Operation(summary = "Obtém movimentos de veículo", description = "Obtém movimentos de veículo conforme filtro")
	@POST
	public Response obterMovVeiculos(@RequestBody MovimentoVeiculoDTO dto) {
		return Response.status(Status.OK).entity(service.obterMovsVeiculoAbertos(dto)).build();
	}

	@Operation(summary = "Encerrar movimento de veículo", description = "Encerra um movimento de veículo")
	@PUT
	public Response encerrarMovVeiculo(@RequestBody MovimentoVeiculoDTO dto) {
		return Response.status(Status.OK).entity(service.encerrarMovVeiculo(dto)).build();
	}
}
