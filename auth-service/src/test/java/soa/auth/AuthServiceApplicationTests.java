package soa.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import soa.auth.dto.AuthResponse;
import soa.auth.dto.LoginRequest;
import soa.auth.dto.RegisterRequest;
import soa.auth.service.AuthService;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthServiceApplicationTests {

    @Autowired
    private AuthService authService;

    @Test
    void contextLoads() {
        assertNotNull(authService);
    }

    @Test
    void testRegisterAndLoginWorkflow() {
        String testUser = "testuser_" + System.currentTimeMillis();
        RegisterRequest registerReq = new RegisterRequest(
                "Test User",
                testUser,
                testUser + "@klu.ac.in",
                "secret123",
                "ROLE_USER"
        );

        AuthResponse regResponse = authService.register(registerReq);
        assertNotNull(regResponse.getToken());
        assertEquals(testUser, regResponse.getUsername());
        assertEquals("ROLE_USER", regResponse.getRole());

        // Test login
        LoginRequest loginReq = new LoginRequest(testUser, "secret123");
        AuthResponse loginResponse = authService.login(loginReq);
        assertNotNull(loginResponse.getToken());

        // Test validation
        Map<String, Object> validation = authService.validateToken(loginResponse.getToken());
        assertTrue((Boolean) validation.get("valid"));
        assertEquals(testUser, validation.get("username"));
    }

    @Test
    void testDuplicateUsernameFails() {
        String uniqueName = "dupuser_" + System.currentTimeMillis();
        RegisterRequest req = new RegisterRequest(
                "Duplicate User",
                uniqueName,
                uniqueName + "@klu.ac.in",
                "password",
                "ROLE_USER"
        );
        authService.register(req);

        // Try registering again with same username
        RegisterRequest dupReq = new RegisterRequest(
                "Duplicate User 2",
                uniqueName,
                "other_" + uniqueName + "@klu.ac.in",
                "password",
                "ROLE_USER"
        );

        assertThrows(IllegalArgumentException.class, () -> authService.register(dupReq));
    }

    @Test
    void testInvalidLoginFails() {
        LoginRequest badLogin = new LoginRequest("nonexistent_user", "wrongpassword");
        assertThrows(IllegalArgumentException.class, () -> authService.login(badLogin));
    }
}
