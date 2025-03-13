package tests;

import dto.LoginUserRequest;
import dto.RefreshTokenRequest;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TokenRefreshTest extends BaseTest {
    private static String accessToken;
    private static String refreshToken;

    @BeforeAll
    public static void setupTokens() {
        // Логинимся и получаем access и refresh токен
        Response loginResponse = postRequest("/auth/login", 200,
                new LoginUserRequest("qa.test@example.com", "Tester123!"), null);

        assertNotNull(loginResponse, "Ответ на логин не должен быть null!");

        accessToken = loginResponse.body().jsonPath().getString("accessToken");
        refreshToken = loginResponse.body().jsonPath().getString("refreshToken");

        assertNotNull(accessToken, "Access token не должен быть null!");
        assertNotNull(refreshToken, "Refresh token не должен быть null!");
    }

    @Test
    public void testValidTokenRefresh() {
        RefreshTokenRequest requestBody = new RefreshTokenRequest(refreshToken);
        Response response = postRequest("/auth/refresh", 200, requestBody, null);

        assertNotNull(response, "not null!");
        String newAccessToken = response.body().jsonPath().getString("access_token");
        assertNotNull(newAccessToken, "new access token not null!");
        assertFalse(newAccessToken.isEmpty(), "new access token not empty!");
        System.out.println("New Access Token: " + newAccessToken);
    }
}
