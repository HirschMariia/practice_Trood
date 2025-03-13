package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectTest extends BaseTest {
    private static String projectId;
    private static String accessToken;

    @BeforeAll
    public static void setupToken() {
        accessToken = new BaseTest().loginAccessToken("MYuser@example.com", "MYuser");
        projectId = "project-123";
    }

    @Test
    public void getProjectById() {
        assertNotNull(projectId, "Project ID не должен быть null!");
        assertFalse(projectId.isEmpty(), "Project ID не должен быть пустым!");
        Response response = getRequest("/projects/" + projectId, 200, accessToken);
        assertNotNull(response, "Ответ от сервера не должен быть null!");
        assertEquals(200, response.statusCode(), "Статус код должен быть 200!");
        System.out.println("Данные проекта: " + response.body().asString());
    }
}
