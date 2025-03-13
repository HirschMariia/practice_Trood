package tests;

import dto.FileMetadata;
import dto.RegisterUserRequest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterUserTest extends BaseTest {

    @Test
    public void registerUserSuccessfully() throws Exception {
        String uniqueEmail = "user_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";

        RegisterUserRequest request = RegisterUserRequest.builder()
                .firebaseId("firebase-" + UUID.randomUUID().toString().substring(0, 8))
                .email(uniqueEmail)
                .firstName("Test")
                .lastName("User")
                .jobTitle("QA Engineer")
                .address("123 Main St")
                .phone("+1-555-123-4567")
                .photo("https://example.com/profile.jpg")
                .pitchProduct("We create AI-driven analytics tools.")
                .position("QA Engineer")
                .description("Experienced developer with expertise in AI")
                .skills(List.of("Java", "Spring Boot"))
                .files(Map.of("resume.pdf", new FileMetadata("https://example.com/resume.pdf", "pdf")))
                .rate(100)
                .profileId("profile-" + UUID.randomUUID().toString().substring(0, 8))
                .projectIds(List.of("project-123", "project-456"))
                .password("Test1234!")
                .build();

        Response response = postRequest("/auth/register", 200, request, null);
        int actualStatusCode = response.statusCode();
        assertTrue(actualStatusCode == 200 || actualStatusCode == 201,
                "Статус код должен быть 200 или 201, но получен: " + actualStatusCode);
        String registeredEmail = response.body().jsonPath().getString("user.email");
        assertEquals(uniqueEmail, registeredEmail, "Email в ответе должен совпадать с отправленным!");
        System.out.println("Регистрация прошла успешно для пользователя: " + uniqueEmail);
    }
}
