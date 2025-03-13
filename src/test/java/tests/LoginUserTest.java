package tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginUserTest extends BaseTest {
    @Test
    public void testLogin() {
        String token = loginAccessToken("MYuser@example.com", "MYuser");
        System.out.println("Полученный токен: " + token);
        assertNotNull(token);
    }
    @Test
    public void successLoginUser() {
        String email = "MYuser@example.com";
        String password = "MYuser";
        String token = loginAccessToken(email, password);
        assertFalse(token.isEmpty());
    }
}

