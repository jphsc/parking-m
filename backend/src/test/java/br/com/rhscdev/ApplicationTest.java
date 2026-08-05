package br.com.rhscdev;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;

@QuarkusTest
class ApplicationTest {
    @Test
    void testgetVeiculoEndpoint() {
        given()
          .when()
          	.get("/veiculos?pagina=1")
          .then()
          	.contentType(ContentType.JSON)
          	.statusCode(Response.Status.OK.getStatusCode())
            .body("quantidade", greaterThanOrEqualTo(0));
    }

}