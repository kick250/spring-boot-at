package br.edu.at.App.services;

import br.edu.at.App.entities.Teacher;
import br.edu.at.App.exceptions.InvalidTokenException;
import br.edu.at.App.infra.TimeConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;

import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TokenServiceUnitaryTest {
    private TokenService tokenService;

    private Environment env;
    private TimeConfig timeConfig;

    @BeforeEach
    public void setUp() {
        env = mock(Environment.class);
        timeConfig = mock(TimeConfig.class);

        when(env.getProperty("spring.application.name")).thenReturn("TestApp");
        when(env.getProperty("spring.application.security.token.secret")).thenReturn("123456789");

        tokenService = new TokenService(env, timeConfig);
    }

    @Test
    public void testGenerateTokenAndDecodeToken() {
        String username = "teacher.name@test.com.br";
        when(timeConfig.zoneOffset()).thenReturn(ZoneOffset.of("-03:00"));

        var teacher = mock(Teacher.class);
        when(teacher.getUsername()).thenReturn(username);

        String token = tokenService.generateToken(teacher);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        var decodedJWT = tokenService.decodeToken(token);
        assertEquals(username, decodedJWT.getSubject());
    }

    @Test
    public void testGenerateTokenAndDecodeToken_whenTokenIsExpired() {
        String username = "teacher.name@test.com.br";
        when(timeConfig.zoneOffset()).thenReturn(ZoneOffset.UTC);

        var teacher = mock(Teacher.class);
        when(teacher.getUsername()).thenReturn(username);

        String token = tokenService.generateToken(teacher);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        Exception exception = assertThrows(InvalidTokenException.class, () -> {
            tokenService.decodeToken(token);
        });

        assertEquals("Ocorreu um erro ao gerar um token.", exception.getMessage());
    }
}
