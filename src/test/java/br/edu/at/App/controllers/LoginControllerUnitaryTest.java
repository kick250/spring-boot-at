package br.edu.at.App.controllers;

import br.edu.at.App.entities.Teacher;
import br.edu.at.App.requests.LoginRequest;
import br.edu.at.App.services.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class LoginControllerUnitaryTest {
    private LoginController loginController;

    private AuthenticationManager authenticationManager;
    private TokenService tokenService;

    @BeforeEach
    public void setUp() {
        authenticationManager = mock(AuthenticationManager.class);
        tokenService = mock(TokenService.class);
        loginController = new LoginController(authenticationManager, tokenService);
    }

    @Test
    public void testLogin() {
        String username = "testuser@test.com.br";
        String password = "test123";
        Authentication authentication = mock(Authentication.class);
        Teacher teacher = mock(Teacher.class);

        var request = mock(LoginRequest.class);
        when(request.username()).thenReturn(username);
        when(request.password()).thenReturn(password);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(teacher);
        when(tokenService.generateToken(teacher)).thenReturn("mocked-jwt-token");

        var response = loginController.login(request);

        verify(authenticationManager, times(1)).authenticate(any());
        verify(tokenService, times(1)).generateToken(teacher);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testLogin_whenInvalidCredentials() {
        String username = "testuser@test.com.br";
        String password = "wrongpassword";
        var request = mock(LoginRequest.class);

        when(request.username()).thenReturn(username);
        when(request.password()).thenReturn(password);
        doThrow(AccountExpiredException.class).when(authenticationManager).authenticate(any());

        var response = loginController.login(request);

        verify(authenticationManager, times(1)).authenticate(any());
        verify(tokenService, times(0)).generateToken(any());
        assertEquals(401, response.getStatusCode().value());
    }
}
