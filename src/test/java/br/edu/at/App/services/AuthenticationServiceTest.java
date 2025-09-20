package br.edu.at.App.services;

import br.edu.at.App.entities.Teacher;
import br.edu.at.App.repositories.TeachersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthenticationServiceTest {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private TeachersRepository teachersRepository;

    private Teacher teacher;

    @BeforeEach
    public void setup() {
        teachersRepository.deleteAll();

        teacher = new Teacher("Teacher Name", "teacher.name@test.com.br", "123456");
        teachersRepository.save(this.teacher);
    }

    @Test
    public void testLoadUserByUsername() {
        var loadedTeacher = authenticationService.loadUserByUsername(teacher.getUsername());

        assertNotNull(loadedTeacher);
        assertEquals(teacher.getId(), ((Teacher) loadedTeacher).getId());
        assertEquals(teacher.getUsername(), loadedTeacher.getUsername());
    }

    @Test
    public void testLoadUserByUsername_NotFound() {
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            authenticationService.loadUserByUsername("not.found@test.com.br");
        });

        assertEquals("Professor não encontrado.", exception.getMessage());
    }
}