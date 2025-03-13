package tests;

import dto.LoginUserRequest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class BaseTest {
    final static String BASE_URI = "https://troodtth-back.onrender.com";
    static RequestSpecification specification = new RequestSpecBuilder()
            .setBaseUri(BASE_URI)
            .setContentType(ContentType.JSON)
            .build();

    public static Response getRequest(String endpoint, Integer expectedStatusCode, String accessToken) {
        return given()
                .spec(specification)
                .header("Authorization", accessToken != null ? "Bearer " + accessToken : "")
                .when()
                .log().all()
                .get(endpoint)
                .then()
                .log().all()
                .statusCode(expectedStatusCode)
                .extract().response();
    }

    public static Response postRequest(String endpoint, Integer expectedStatusCode, Object body, String accessToken) {
        return given()
                .spec(specification)
                .header("Authorization", accessToken != null ? "Bearer " + accessToken : "")
                .body(body)
                .when()
                .log().all()
                .post(endpoint)
                .then()
                .log().all()
                .statusCode(expectedStatusCode)
                .extract().response();
    }

    public static Response patchRequest(String endpoint, Object body, Integer expectedStatusCode, String accessToken) {
        return given()
                .spec(specification)
                .header("Authorization", accessToken != null ? "Bearer " + accessToken : "")
                .body(body)
                .when()
                .log().all()
                .patch(endpoint)
                .then()
                .log().all()
                .statusCode(expectedStatusCode)
                .extract().response();
    }

    public static Response deleteRequest(String endpoint, Integer expectedStatusCode, String accessToken) {
        return given()
                .spec(specification)
                .header("Authorization", accessToken != null ? "Bearer " + accessToken : "")
                .when()
                .log().all()
                .delete(endpoint)
                .then()
                .log().all()
                .statusCode(expectedStatusCode)
                .extract().response();
    }

    public Response postImageRequest(String endpoint, File file, int expectedStatusCode, String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .contentType("multipart/form-data")
                .multiPart("file", file)
                .when()
                .post(endpoint)
                .then()
                .statusCode(expectedStatusCode)
                .extract()
                .response();
    }

    public String loginAccessToken(String email, String password) {
        LoginUserRequest requestBody = new LoginUserRequest(email, password);
        Response response = postRequest("/auth/login", 200, requestBody, null);
        String token = response.body().jsonPath().getString("accessToken");
        assertFalse(token.isEmpty(), "Access token не должен быть пустым!");
        return token;
    }

    public static Response putRequest(String endpoint, Object body, Integer expectedStatusCode, String accessToken) {
        return given()
                .spec(specification)
                .header("Authorization", accessToken != null ? "Bearer " + accessToken : "")
                .body(body)
                .contentType("application/json")
                .when()
                .log().all()
                .put(endpoint)
                .then()
                .log().all()
                .statusCode(expectedStatusCode)
                .extract().response();
    }
}
