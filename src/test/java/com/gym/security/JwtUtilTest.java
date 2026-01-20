package com.gym.security;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil buildJwtUtil() {
        return new JwtUtil(
                "my-test-secret-key-my-test-secret-key",
                60000L,       // 1 min
                3600000L   // 1 h
        );
    }

    @Test
    void shouldGenerateValidAccessToken() {
        JwtUtil jwtUtil = buildJwtUtil();

        String token = jwtUtil.generateAccessToken(
                "admin@gym.com",
                "ADMIN",
                "gym_wod"
        );

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void shouldExtractClaimsCorrectly() {
        JwtUtil jwtUtil = buildJwtUtil();

        String token = jwtUtil.generateAccessToken(
                "admin@gym.com",
                "ADMIN",
                "gym_wod"
        );

        assertEquals("admin@gym.com", jwtUtil.extractEmail(token));
        assertEquals("ADMIN", jwtUtil.extractRole(token));
        assertEquals("gym_wod", jwtUtil.extractTenant(token));
    }

    @Test
    void tokenShouldBeValid() {
        JwtUtil jwtUtil = buildJwtUtil();

        String token = jwtUtil.generateAccessToken(
                "admin@gym.com",
                "ADMIN",
                "gym_wod"
        );

        assertTrue(jwtUtil.isTokenValid(token, "admin@gym.com"));
    }
}
