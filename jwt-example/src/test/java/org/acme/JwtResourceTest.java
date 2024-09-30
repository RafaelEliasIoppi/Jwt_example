package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
public class JwtResourceTest {

    @Test
    public void testGetPrivateKey() {
        RestAssured.given()
            .accept(ContentType.TEXT)
            .when().get("/jwt")
            .then()
            .statusCode(200)
            .body(containsString("Private Key Loaded Successfully"));
    }

    @Test
    public void testGetToken() {
        RestAssured.given()
            .accept(ContentType.TEXT)
            .when().get("/jwt/token")
            .then()
            .statusCode(200)
            .body(containsString("Generated JWT:"));
    }
}

