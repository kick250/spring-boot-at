package br.edu.at.App.controllers;

import br.edu.at.App.entities.Course;
import br.edu.at.App.entities.Enrollment;
import br.edu.at.App.entities.Student;
import br.edu.at.App.repositories.CoursesRepository;
import br.edu.at.App.repositories.EnrollmentsRepository;
import br.edu.at.App.repositories.StudentsRepository;
import br.edu.at.App.requests.AssessmentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AssessmentsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private StudentsRepository studentsRepository;
    @Autowired
    private CoursesRepository coursesRepository;
    @Autowired
    private EnrollmentsRepository enrollmentsRepository;

    private Student student;
    private Course course;
    private Enrollment enrollment;
    private String testEmail = "maria.test@test.com.br";

    @BeforeEach
    public void setup() {
        enrollmentsRepository.deleteAll();
        coursesRepository.deleteAll();
        studentsRepository.deleteAll();

        student = new Student("Joao Test", "123.456.789-00", "joao.test@test.com.br", "12345678901", "Ladeira da gloria, 26");
        studentsRepository.save(student);

        course = new Course( "Desenvolvimento de Serviços com Spring Boot", "QWERTY");
        coursesRepository.save(course);

        enrollment = new Enrollment(student, course);
        enrollmentsRepository.save(enrollment);
    }

    @AfterEach
    public void tearDown() {
        enrollmentsRepository.deleteAll();
        coursesRepository.deleteAll();
        studentsRepository.deleteAll();
    }

    @Test
    public void testCreateAssessment() throws Exception {
        Double grade = 8.5;
        var request = new AssessmentRequest(student.getId(), course.getId(), grade);
        var requestJson = objectMapper.writeValueAsString(request);

        assertEquals(null, enrollment.getGrade());

        mockMvc.perform(post("/assessments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        enrollment = enrollmentsRepository.findById(enrollment.getId()).orElseThrow();
        assertEquals(grade, enrollment.getGrade());
    }

    @Test
    public void testCreateAssessment_whenGradeInvalid() throws Exception {
        var request = new AssessmentRequest(student.getId(), course.getId(), 11.0);

        mockMvc.perform(post("/assessments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateAssessment_whenGradeNegative() throws Exception {
        var request = new AssessmentRequest(student.getId(), course.getId(), -1.0);

        mockMvc.perform(post("/assessments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateAssessment_whenEnrollmentNotFound() throws Exception {
        var request = new AssessmentRequest(999L, 999L, 8.5);

        mockMvc.perform(post("/assessments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}