package br.edu.at.App.services;

import br.edu.at.App.entities.Teacher;
import br.edu.at.App.repositories.TeachersRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TokenServiceTest {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private TeachersRepository teachersRepository;

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teachersRepository.deleteAll();

        teacher = new Teacher("Teacher Name", "teacher.name@test.com.br", "123456");
        teachersRepository.save(teacher);
    }

    @AfterEach
    void tearDown() {
        teachersRepository.deleteAll();
    }

    @Test
    void testGenerateAndDecodeToken() {
        String token = tokenService.generateToken(teacher);
        assertNotNull(token);

        var decodedJWT = tokenService.decodeToken(token);
        assertNotNull(decodedJWT);
        assertEquals(teacher.getUsername(), decodedJWT.getSubject());
    }
}