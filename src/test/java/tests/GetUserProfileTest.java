package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GetUserProfileTest extends BaseTest {
    @Test
    public void getUserProfile() {
        String accessToken = loginAccessToken("MYuser@example.com", "MYuser");
        Response response = getRequest("/users/me", 200, accessToken);
        assertEquals(200, response.statusCode());
        assertFalse(response.body().jsonPath().getString("email").isEmpty());
    }
}
