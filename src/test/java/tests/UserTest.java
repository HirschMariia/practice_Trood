package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest extends BaseTest {
    private static String accessToken;

    @BeforeAll
    public static void setupToken() {
        accessToken = new BaseTest().loginAccessToken("tester@example.com", "Tester123!");
    }

    @Test
    @Order(1)
    public void testGetUserProfile() {
        Response response = getRequest("/users/me", 200, accessToken);
        assertEquals(200, response.statusCode());
    }

    @Test
    @Order(2)
    public void testUpdateUserProfile() {
        String requestBody = "{\"first_name\": \"Tester\"}";
        Response response = patchRequest("/users/me", requestBody, 200, accessToken);
        assertEquals(200, response.statusCode());
    }
}
