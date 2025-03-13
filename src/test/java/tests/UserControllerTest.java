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
    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNWXVzZXJAZXhhbXBsZS5jb20iLCJyb2xlcyI6WyJVU0VSIl0sInR5cGUiOiJhY2Nlc3MiLCJpYXQiOjE3NDE4ODM4MzUsImV4cCI6MTc0MTg4NDczNX0.47TCu8hNX0ZXm94yjSj1r0rj_xjUY9TSsoGJlQ35ggQ";

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
        assertNotNull(userId, "User ID должен быть получен");
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

        // Проверяем, что данные реально обновились
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

        // Даем API время обработать изменение
        Thread.sleep(2000); // Ждем 2 секунды

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

        // Получаем свежий токен перед тестом
        String freshAccessToken = loginAccessToken("MYuser@example.com", "MYuser");

        // Отправляем запрос к API
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

        // Логируем статус-код и ответ API
        System.out.println("Статус-код: " + response.getStatusCode());
        System.out.println("Ответ API: " + response.getBody().asString());

        // Проверяем статус-код
        assertEquals(200, response.getStatusCode(), "Ожидался статус 200, но получен: " + response.getStatusCode());

        // Получаем тело ответа
        Map<String, Object> responseBody = response.jsonPath().getMap("$");

        // Проверяем наличие ключа "items"
        assertNotNull(responseBody.get("items"), "Поле 'items' отсутствует в ответе API");

        // Определяем тип `items` и обрабатываем соответствующе
        Object itemsObject = responseBody.get("items");

        if (itemsObject instanceof Map) {
            // API вернул пустой объект `{}`, значит, нет данных
            Map<?, ?> itemsMap = (Map<?, ?>) itemsObject;
            assertTrue(itemsMap.isEmpty(), "API вернул объект, но он не пустой: " + itemsMap);
            System.out.println("API вернул пустой объект 'items', данных нет.");
        } else if (itemsObject instanceof List) {
            // API вернул список пользователей
            List<?> itemsList = (List<?>) itemsObject;
            assertFalse(itemsList.isEmpty(), "Список пользователей не должен быть пустым.");
            System.out.println("API вернул список пользователей: " + itemsList.size() + " записей.");
        } else {
            fail("Поле 'items' имеет неизвестный тип: " + itemsObject.getClass().getSimpleName());
        }
    }




}





