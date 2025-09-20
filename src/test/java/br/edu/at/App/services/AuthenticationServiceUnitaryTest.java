package br.edu.at.App.services;

import br.edu.at.App.entities.Teacher;
import br.edu.at.App.repositories.TeachersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationServiceUnitaryTest {
    private AuthenticationService authenticationService;

    private TeachersRepository teachersRepository;

    @BeforeEach
    public void setup() {
        teachersRepository = mock(TeachersRepository.class);
        authenticationService = new AuthenticationService(teachersRepository);
    }

    @Test
    public void testLoadUserByUsername() {
        String username = "teacher.name@test.com.br";
        Teacher teacher = mock(Teacher.class);

        when(teachersRepository.findByEmail(username)).thenReturn(Optional.of(teacher));

        var loadedUser = authenticationService.loadUserByUsername(username);

        verify(teachersRepository, times(1)).findByEmail(username);
        assertEquals(teacher, loadedUser);
    }

    @Test
    public void testLoadUserByUsername_whenNotFound() {
        String username = "not.found@test.com.br";

        when(teachersRepository.findByEmail(username)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            authenticationService.loadUserByUsername(username);
        });

        verify(teachersRepository, times(1)).findByEmail(username);
        assertEquals("Professor não encontrado.", exception.getMessage());
    }
}
