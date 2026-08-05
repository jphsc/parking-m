package br.com.rhscdev.integration.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import br.com.rhscdev.application.dto.request.VeiculoRequest;
import br.com.rhscdev.infrastructure.config.Constantes;
import br.com.rhscdev.infrastructure.persistence.VeiculoPanacheRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTest
public class VeiculoResourceTest {
	
	@Inject private VeiculoPanacheRepository repository;
	
	@AfterEach
	@TestTransaction
	void clean() {
	    repository.deleteAll();
	}

	@Test
	void criarVeiculoComSucesso() {
		
		VeiculoRequest req = new VeiculoRequest(null, "ex2", "Geely", "oto8226");
		
		given()
			.contentType(ContentType.JSON)
			.body(req)
		.when()
			.post("/veiculos")
		.then()
	      	.contentType(ContentType.JSON)
	      	.statusCode(Response.Status.CREATED.getStatusCode())
			.body("registros[0].placa", equalTo("OTO8226"))
			.body("registros.size()", equalTo(1))
			.body("mensagem", equalTo(Constantes.MSG_REGISTRO_CADASTRADO));
		
		assertNotNull(repository.findByPlaca(req.placa()));
	}

	@Test
	@TestTransaction
	void erroCriarVeiculoPlacaDuplicada() {
		
		VeiculoRequest req = new VeiculoRequest(null, "ex2", "Geely", "oto8227");
		
		given()
			.contentType(ContentType.JSON)
			.body(req)
		.when()
			.post("/veiculos")
		.then()
	      	.statusCode(Response.Status.CREATED.getStatusCode());
		
		
		given()
			.contentType(ContentType.JSON)
			.body(req)
		.when()
			.post("/veiculos")
		.then()
	      	.statusCode(Response.Status.CONFLICT.getStatusCode())
	      	.body("mensagem", equalTo("A placa informada já existe no sistema. Verifique se a placa está correta"));
		
	}

    @Test
    void testgetVeiculoEndpoint() {
        given()
          .when()
          	.get("/veiculos?pagina=1")
          .then()
          	.contentType(ContentType.JSON)
          	.statusCode(Response.Status.OK.getStatusCode());
    }
}
