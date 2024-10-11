package com.programmers.shoppingmall;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class RestApiControllerTest {

    @Test
    public void testGetUserList() {
        RestAssured.baseURI = "http://localhost:8080";

        given()
                .when()
                .get("/user")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", greaterThan(0));
    }

    @Test
    public void testGetProductList() {
        given()
                .when()
                .get("/products")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", greaterThan(0));
    }

    @Test
    public void testGetProductById() {
        given()
                .when()
                .get("/product/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", notNullValue());
    }
}
