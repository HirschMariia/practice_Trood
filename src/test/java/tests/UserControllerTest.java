package tests;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@Nested
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest extends BaseTest {
    private static String accessToken;
    private static String userId;
    private static final String LAST_USER_ID = "df4a75f9-74f8-4ee1-9f32-2240305eac90";
    private static final String LAST_PROJECT_ID = "project-123";
    private static final String BASE_URL = "https://troodtth-back.onrender.com";

    @BeforeAll
    public static void setup() {
        accessToken = new BaseTest().loginAccessToken("MYuser@example.com", "MYuser");
    }

    @Test
    @Order(1)
    public void testGetUserById_Valid() {
        Response response = getRequest("/users/me", 200, accessToken);
        assertEquals(200, response.statusCode());
        userId = response.body().jsonPath().getString("id");
        assertNotNull(userId, "Get User ID");
    }

    @Test
    @Order(2)
    public void testGetUserById_Invalid() {
        Response response = getRequest("/users/invalid-id", 400, accessToken);
        assertEquals(400, response.statusCode());
    }

    @Test
    @Order(3)
    public void testUpdateUserById() {
        String userId = "df4a75f9-74f8-4ee1-9f32-2240305eac90";
        String accessToken = loginAccessToken("MYuser@example.com", "MYuser");

        String requestBody = "{"
                + "\"firebase_id\": \"firebase-xyz-123\","
                + "\"email\": \"MYuser@example.com\","
                + "\"first_name\": \"UpdatedName\","
                + "\"last_name\": \"Doe\","
                + "\"phone\": \"+1-555-987-6543\","
                + "\"job_title\": \"QA Engineer\""
                + "}";

        Response response = putRequest("/users/" + userId, requestBody, 200, accessToken);
        assertEquals(200, response.statusCode());

        Response updatedResponse = getRequest("/users/" + userId, 200, accessToken);
        assertEquals("UpdatedName", updatedResponse.body().jsonPath().getString("first_name"));
    }


    @Test
    @Order(4)
    public void testUpdateUserDetails_Invalid() {
        String requestBody = "{\"email\": \"invalid-email\"}";
        Response response = putRequest("/users/" + userId, requestBody, 400, accessToken);
        assertEquals(400, response.statusCode());
    }

    @Test
    @Order(5)
    public void testPartialUpdateUserProfile() throws InterruptedException {
        String accessToken = loginAccessToken("MYuser@example.com", "MYuser");

        String requestBody = "{ \"job_title\": \"QA Engineer\" }";

        Response response = patchRequest("/users/me", requestBody, 200, accessToken);
        assertEquals(200, response.statusCode());

        Thread.sleep(2000);

        Response updatedResponse = getRequest("/users/me", 200, accessToken);
        assertEquals("QA Engineer", updatedResponse.body().jsonPath().getString("job_title"));
    }


    @Test
    @Order(6)
    public void testPartialUpdateUserDetails_Invalid() {
        String requestBody = "{\"rate\": \"invalid\"}";
        Response response = patchRequest("/users/" + userId, requestBody, 400, accessToken);
        assertEquals(400, response.statusCode());
    }

    @Test
    @Order(7)
    public void testGetUsersWithSharedProjects() {
        RestAssured.baseURI = BASE_URL;

        String freshAccessToken = loginAccessToken("MYuser@example.com", "MYuser");

        Response response = given()
                .header("Authorization", "Bearer " + freshAccessToken)
                .header("Accept", "application/json")
                .queryParam("lastUserId", LAST_USER_ID)
                .queryParam("lastProjectId", LAST_PROJECT_ID)
                .queryParam("size", 10)
                .when()
                .get("/users/people")
                .then()
                .extract().response();

        System.out.println("Статус-код: " + response.getStatusCode());
        System.out.println("Ответ API: " + response.getBody().asString());

        assertEquals(200, response.getStatusCode(), "Expected status 200, but received: " + response.getStatusCode());

        Map<String, Object> responseBody = response.jsonPath().getMap("$");

        assertNotNull(responseBody.get("items"), "Field 'items' is missing in the API response.");

        Object itemsObject = responseBody.get("items");

        if (itemsObject instanceof Map) {
            Map<?, ?> itemsMap = (Map<?, ?>) itemsObject;
            assertTrue(itemsMap.isEmpty(), "API returned an object, but it is not empty: " + itemsMap);
            System.out.println("API returned an empty 'items' object, no data available.");
        } else if (itemsObject instanceof List) {
            // API returned a list of users
            List<?> itemsList = (List<?>) itemsObject;
            assertFalse(itemsList.isEmpty(), "The list of users should not be empty.");
            System.out.println("API returned a list of users: " + itemsList.size() + " entries.");
        } else {
            fail("The 'items' field has an unknown type: " + itemsObject.getClass().getSimpleName());
        }

    }
}





