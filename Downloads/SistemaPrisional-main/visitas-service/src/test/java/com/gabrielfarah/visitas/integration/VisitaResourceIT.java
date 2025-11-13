package com.gabrielfarah.visitas.integration;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.restassured.http.ContentType;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import com.gabrielfarah.visitas.service.PrisioneiroClient;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@QuarkusTest
class VisitaResourceIT {

    @InjectMock
    @RestClient
    PrisioneiroClient prisioneiroClient;

    @Test
    void testListVisitas() {
        given()
                .when().get("/api/visitas")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    void testCreateVisita() {
        // First create a visitante
        String visitanteBody = """
                {
                    "nome": "Visita Test Visitor",
                    "cpf": "88877766655",
                    "telefone": "(48)91234-5678",
                    "relacaoPrisioneiro": "Amigo",
                    "isAdvogado": false
                }
                """;

        String visitanteId = given()
                .contentType(ContentType.JSON)
                .body(visitanteBody)
                .when().post("/api/visitantes")
                .then()
                .statusCode(201)
                .extract().path("id").toString();

        // Mock prisioneiro service response
        PrisioneiroClient.PrisioneiroDTO prisioneiro = new PrisioneiroClient.PrisioneiroDTO();
        prisioneiro.id = 1L;
        prisioneiro.name = "Test Prisoner";
        when(prisioneiroClient.getPrisioneiro(anyLong())).thenReturn(prisioneiro);

        // Create visita
        String visitaBody = String.format("""
                {
                    "prisioneiroId": 1,
                    "visitanteId": %s,
                    "observacoes": "Test visit"
                }
                """, visitanteId);

        given()
                .contentType(ContentType.JSON)
                .body(visitaBody)
                .when().post("/api/visitas")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("prisioneiroId", is(1))
                .body("status", notNullValue());
    }

    @Test
    void testCreateVisita_MissingPrisioneiroId() {
        String requestBody = """
                {
                    "visitanteId": 1,
                    "observacoes": "Test visit"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("/api/visitas")
                .then()
                .statusCode(400);
    }

    @Test
    void testCreateVisita_MissingVisitanteId() {
        String requestBody = """
                {
                    "prisioneiroId": 1,
                    "observacoes": "Test visit"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("/api/visitas")
                .then()
                .statusCode(400);
    }

    @Test
    void testCreateVisita_InvalidVisitante() {
        String requestBody = """
                {
                    "prisioneiroId": 1,
                    "visitanteId": 99999,
                    "observacoes": "Test visit"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("/api/visitas")
                .then()
                .statusCode(400);
    }

    @Test
    void testCreateVisita_SpecialPrisoner_WithAuthorizedLawyer() {
        // Create lawyer with authorization code
        String lawyerBody = """
                {
                    "nome": "Dr. Authorized Lawyer",
                    "cpf": "22233344455",
                    "telefone": "(48)91234-5678",
                    "relacaoPrisioneiro": "Advogado",
                    "isAdvogado": true,
                    "codigoAutorizacao": 666
                }
                """;

        String lawyerId = given()
                .contentType(ContentType.JSON)
                .body(lawyerBody)
                .when().post("/api/visitantes")
                .then()
                .statusCode(201)
                .extract().path("id").toString();

        // Mock prisioneiro service for special prisoner
        PrisioneiroClient.PrisioneiroDTO prisioneiro = new PrisioneiroClient.PrisioneiroDTO();
        prisioneiro.id = 41L;
        prisioneiro.name = "Velho Viril";
        when(prisioneiroClient.getPrisioneiro(41L)).thenReturn(prisioneiro);

        // Create visita for special prisoner
        String visitaBody = String.format("""
                {
                    "prisioneiroId": 41,
                    "visitanteId": %s,
                    "observacoes": "Authorized lawyer visit"
                }
                """, lawyerId);

        given()
                .contentType(ContentType.JSON)
                .body(visitaBody)
                .when().post("/api/visitas")
                .then()
                .statusCode(201)
                .body("status", is("AUTORIZADA"));
    }

    @Test
    void testCreateVisita_SpecialPrisoner_WithoutAuthorizedLawyer() {
        // Create regular visitor (non-lawyer)
        String visitorBody = """
                {
                    "nome": "Regular Visitor",
                    "cpf": "33344455566",
                    "telefone": "(48)91234-5678",
                    "relacaoPrisioneiro": "Amigo",
                    "isAdvogado": false
                }
                """;

        String visitorId = given()
                .contentType(ContentType.JSON)
                .body(visitorBody)
                .when().post("/api/visitantes")
                .then()
                .statusCode(201)
                .extract().path("id").toString();

        // Create visita for special prisoner
        String visitaBody = String.format("""
                {
                    "prisioneiroId": 41,
                    "visitanteId": %s,
                    "observacoes": "Unauthorized visit"
                }
                """, visitorId);

        given()
                .contentType(ContentType.JSON)
                .body(visitaBody)
                .when().post("/api/visitas")
                .then()
                .statusCode(201)
                .body("status", is("NEGADA"))
                .body("motivoNegacao", notNullValue());
    }

    @Test
    void testGetVisitasByPrisioneiroId() {
        given()
                .when().get("/api/visitas/prisioneiro/1")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    void testGetVisitasByVisitanteId() {
        given()
                .when().get("/api/visitas/visitante/1")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    void testUpdateVisitaStatus() {
        // First create visitante and visita
        String visitanteBody = """
                {
                    "nome": "Status Update Test",
                    "cpf": "44455566677",
                    "telefone": "(48)91234-5678",
                    "relacaoPrisioneiro": "Amigo",
                    "isAdvogado": false
                }
                """;

        String visitanteId = given()
                .contentType(ContentType.JSON)
                .body(visitanteBody)
                .when().post("/api/visitantes")
                .then()
                .statusCode(201)
                .extract().path("id").toString();

        // Mock prisioneiro
        PrisioneiroClient.PrisioneiroDTO prisioneiro = new PrisioneiroClient.PrisioneiroDTO();
        prisioneiro.id = 2L;
        prisioneiro.name = "Test Prisoner 2";
        when(prisioneiroClient.getPrisioneiro(2L)).thenReturn(prisioneiro);

        String visitaBody = String.format("""
                {
                    "prisioneiroId": 2,
                    "visitanteId": %s,
                    "observacoes": "Status test"
                }
                """, visitanteId);

        String visitaId = given()
                .contentType(ContentType.JSON)
                .body(visitaBody)
                .when().post("/api/visitas")
                .then()
                .statusCode(201)
                .extract().path("id").toString();

        // Update status
        String statusUpdateBody = """
                {
                    "status": "NEGADA",
                    "motivoNegacao": "Teste de negação"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(statusUpdateBody)
                .when().put("/api/visitas/" + visitaId + "/status")
                .then()
                .statusCode(200)
                .body("status", is("NEGADA"))
                .body("motivoNegacao", is("Teste de negação"));
    }
}
