package me.guilherme.quarkussocial.rest;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import me.guilherme.quarkussocial.rest.dto.CreateUserRequest;
import me.guilherme.quarkussocial.rest.dto.ResponseError;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import java.net.URL;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserResourceTest {

    @TestHTTPResource("/users")
    URL apiurl;

    @Test
    @DisplayName("should create an user successfully")
    @Order(1)
    void createUserTest() {
        var user = new CreateUserRequest();
        user.setName("Fulano");
        user.setAge(30);

        var response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(apiurl)
                .then()
                .extract().response();

        assertEquals(201, response.getStatusCode());
        assertNotNull(response.jsonPath().getString("id"));
    }


    @Test
    @DisplayName("Should return error when json is not valid")
    @Order(2)
    void createUserValidationErrorTest() {
        var user = new CreateUserRequest();
        user.setAge(null);
        user.setName(null);

        var response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(apiurl)
                .then()
                .extract().response();

        assertEquals(ResponseError.UNPROCESSABLE_ENTITY_STATUS, response.getStatusCode());
        assertEquals("Validation Error", response.jsonPath().getString("message"));

        List<Map<String, String>> errors = response.jsonPath().getList("errors");
        assertNotNull(errors.get(0).get("message"));
        assertNotNull(errors.get(1).get("message"));
    }


    @Test
    @DisplayName("Should list all users")
    @Order(3)
    void listAllUsersTest(){

        given()
                .contentType(ContentType.JSON)
                .when()
                .get(apiurl)
                .then()
                .statusCode(200)
                .body("size()", Matchers.is(1));

    }

}