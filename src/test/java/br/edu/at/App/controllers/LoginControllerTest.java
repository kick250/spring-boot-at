package br.edu.at.App.controllers;

import br.edu.at.App.entities.Teacher;
import br.edu.at.App.repositories.TeachersRepository;
import br.edu.at.App.requests.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private TeachersRepository teachersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private Teacher teacher;
    private final String password = "12345678";

    @BeforeEach
    void setUp() {
        teachersRepository.deleteAll();

        teacher = new Teacher("Teacher Name", "teacher.name@test.com.br", passwordEncoder.encode(password));
        teachersRepository.save(teacher);
    }

    @AfterEach
    void tearDown() {
        teachersRepository.deleteAll();
    }

    @Test
    public void testLogin() throws Exception {
        var loginRequest = new LoginRequest(teacher.getUsername(), password);

        mockMvc.perform(post("/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    public void testLogin_whenInvalidCredentials() throws Exception {
        var loginRequest = new LoginRequest(teacher.getUsername(), "wrongpassword");

        mockMvc.perform(post("/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}