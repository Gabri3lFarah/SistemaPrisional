package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
// removed unused import (test asserts only status code now)

@QuarkusTest
class GreetingResourceTest {
    @Test
        void testPrisonersEndpoint() {
                given()
                    .when().get("/prisoners/list")
                    .then()
                         .statusCode(200);
        }

}