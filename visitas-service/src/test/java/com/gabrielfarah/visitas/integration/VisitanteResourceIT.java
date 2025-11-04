package com.gabrielfarah.visitas.integration;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class VisitanteResourceIT {

    @Test
    void testListVisitantes() {
        given()
                .when().get("/api/visitantes")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    void testCreateVisitante() {
        String requestBody = """
                {
                    "nome": "João Integration Test",
                    "cpf": "11122233344",
                    "telefone": "(48)91234-5678",
                    "relacaoPrisioneiro": "Amigo",
                    "isAdvogado": false
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("/api/visitantes")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("nome", is("João Integration Test"))
                .body("cpf", is("11122233344"));
    }

    @Test
    void testCreateVisitante_MissingName() {
        String requestBody = """
                {
                    "cpf": "99988877766",
                    "telefone": "(48)91234-5678"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("/api/visitantes")
                .then()
                .statusCode(400);
    }

    @Test
    void testCreateVisitante_MissingCPF() {
        String requestBody = """
                {
                    "nome": "Test Without CPF",
                    "telefone": "(48)91234-5678"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("/api/visitantes")
                .then()
                .statusCode(400);
    }

    @Test
    void testCreateVisitante_DuplicateCPF() {
        String requestBody = """
                {
                    "nome": "First Person",
                    "cpf": "55544433322",
                    "telefone": "(48)91234-5678",
                    "relacaoPrisioneiro": "Amigo",
                    "isAdvogado": false
                }
                """;

        // Create first visitante
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("/api/visitantes")
                .then()
                .statusCode(201);

        // Try to create duplicate - should fail
        String duplicateBody = """
                {
                    "nome": "Second Person",
                    "cpf": "55544433322",
                    "telefone": "(48)99999-9999",
                    "relacaoPrisioneiro": "Família",
                    "isAdvogado": false
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(duplicateBody)
                .when().post("/api/visitantes")
                .then()
                .statusCode(409);
    }

    @Test
    void testGetVisitanteById_NotFound() {
        given()
                .when().get("/api/visitantes/99999")
                .then()
                .statusCode(404);
    }

    @Test
    void testUpdateVisitante() {
        // First create a visitante
        String createBody = """
                {
                    "nome": "Update Test",
                    "cpf": "77788899900",
                    "telefone": "(48)91234-5678",
                    "relacaoPrisioneiro": "Amigo",
                    "isAdvogado": false
                }
                """;

        String location = given()
                .contentType(ContentType.JSON)
                .body(createBody)
                .when().post("/api/visitantes")
                .then()
                .statusCode(201)
                .extract().path("id").toString();

        // Then update it
        String updateBody = """
                {
                    "nome": "Updated Name",
                    "telefone": "(48)99999-9999"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(updateBody)
                .when().put("/api/visitantes/" + location)
                .then()
                .statusCode(200)
                .body("nome", is("Updated Name"))
                .body("telefone", is("(48)99999-9999"));
    }

    @Test
    void testDeleteVisitante() {
        // First create a visitante
        String createBody = """
                {
                    "nome": "Delete Test",
                    "cpf": "66655544433",
                    "telefone": "(48)91234-5678",
                    "relacaoPrisioneiro": "Amigo",
                    "isAdvogado": false
                }
                """;

        String id = given()
                .contentType(ContentType.JSON)
                .body(createBody)
                .when().post("/api/visitantes")
                .then()
                .statusCode(201)
                .extract().path("id").toString();

        // Then delete it
        given()
                .when().delete("/api/visitantes/" + id)
                .then()
                .statusCode(204);

        // Verify it's deleted
        given()
                .when().get("/api/visitantes/" + id)
                .then()
                .statusCode(404);
    }
}
