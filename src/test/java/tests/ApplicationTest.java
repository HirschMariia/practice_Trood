package tests;

import dto.ApplicationRequests;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApplicationTest extends BaseTest {
    private static String applicationId;
    private static String accessToken;

    @BeforeAll
    public static void setupToken() {
        accessToken = new BaseTest().loginAccessToken("MYuser@example.com", "MYuser");
       // accessToken = new BaseTest().loginAccessToken("user/test@example.com", "Test123!");
    }

    @Test
    @Order(1)
    public void testCreateApplication() {
        ApplicationRequests request = ApplicationRequests.builder()
                .userId("07381c49-14f1-4568-a609-09a02db7b0d0")
                .projectId("268e57f4-92eb-4c3d-bf25-4838368dbb4b")
                .taskId("919bb2b5-8bc0-4f46-8462-98e74c402091")
                .taskName("Test Task")
                .taskDescription("Test task description")
                .coverLetter("I am a perfect candidate!")
                .applicationStatus("Pending")
                .build();

        Response response = postRequest("/applications", 201, request, accessToken);
        System.out.println("Create Response: " + response.asString());
        assertEquals(201, response.statusCode());
        applicationId = response.jsonPath().getString("id");
        System.out.println("Application ID after creation: " + applicationId);
        assertNotNull(applicationId, "Application ID is null, something went wrong!");
    }


    @Test
    @Order(2)
    public void testGetApplicationById() {
        assertNotNull(applicationId);
        Response response = getRequest("/applications/" + applicationId, 200, accessToken);
        assertEquals(200, response.statusCode());
    }

    @Test
    @Order(3)
    public void testUpdateApplicationStatus() {
        assertNotNull(applicationId);
        ApplicationRequests updateBody = ApplicationRequests.builder()
                .applicationStatus("Accepted")
                .build();
        Response response = patchRequest("/applications/" + applicationId, updateBody, 200, accessToken);
        assertEquals(200, response.statusCode());
    }

    @Test
    @Order(4)
    public void testDeleteApplication() {
        System.out.println("Application ID before delete: " + applicationId);

        assertNotNull(applicationId, "Application ID is null, creation test might have failed!");
        Response response = deleteRequest("/applications/" + applicationId, 200, accessToken);
        System.out.println("Delete response: " + response.asString()); // Вывод ответа от API
        assertEquals(200, response.statusCode());
    }

}
