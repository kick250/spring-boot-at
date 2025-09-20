package br.edu.at.App.controllers;

import br.edu.at.App.entities.Course;
import br.edu.at.App.entities.Enrollment;
import br.edu.at.App.entities.Student;
import br.edu.at.App.entities.Teacher;
import br.edu.at.App.repositories.CoursesRepository;
import br.edu.at.App.repositories.EnrollmentsRepository;
import br.edu.at.App.repositories.StudentsRepository;
import br.edu.at.App.repositories.TeachersRepository;
import br.edu.at.App.requests.StudentCreateRequest;
import br.edu.at.App.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StudentsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private TeachersRepository teachersRepository;
    @Autowired
    private TokenService tokenService;
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
    private String jwtToken;

    @BeforeEach
    public void setup() {
        teachersRepository.deleteAll();
        enrollmentsRepository.deleteAll();
        coursesRepository.deleteAll();
        studentsRepository.deleteAll();

        Teacher teacher = new Teacher("Teacher Name", "teacher.name@test.com.br", "123456");
        teachersRepository.save(teacher);
        jwtToken = tokenService.generateToken(teacher);

        student = new Student("Joao Test", "123.456.789-00", "joao.test@test.com.br", "12345678901", "Ladeira da gloria, 26");
        studentsRepository.save(student);

        course = new Course( "Desenvolvimento de Serviços com Spring Boot", "QWERTY");
        coursesRepository.save(course);

        enrollment = new Enrollment(student, course);
        enrollment.setGrade(10.0);
        enrollmentsRepository.save(enrollment);
    }

    @AfterEach
    public void tearDown() {
        teachersRepository.deleteAll();
        enrollmentsRepository.deleteAll();
        coursesRepository.deleteAll();
        studentsRepository.deleteAll();
    }

    @Test
    public void testGetAll_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/students"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get("/students")
                         .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(student.getId()))
                .andExpect(jsonPath("$[0].name").value(student.getName()))
                .andExpect(jsonPath("$[0].cpf").value(student.getCpf()))
                .andExpect(jsonPath("$[0].email").value(student.getEmail()))
                .andExpect(jsonPath("$[0].phone").value(student.getPhone()))
                .andExpect(jsonPath("$[0].address").value(student.getAddress()))
                .andExpect(jsonPath("$[0].courses[0].id").value(course.getId()))
                .andExpect(jsonPath("$[0].courses[0].name").value(course.getName()))
                .andExpect(jsonPath("$[0].courses[0].code").value(course.getCode()))
                .andExpect(jsonPath("$[0].courses[0].grade").value(enrollment.getGrade()));
    }

    @Test
    public void testGetById_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/students/{id}", student.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testCreate() throws Exception {
        String name = "Maria Test";
        String cpf = "987.654.321-00";
        String phone = "12345678910";
        String address = "Ladeira da gloria, 27";
        List<Long> courseIds = List.of(course.getId());
        StudentCreateRequest request = new StudentCreateRequest(name, cpf, testEmail, phone, address, courseIds);

        assertEquals(1, studentsRepository.count());

        mockMvc.perform(post("/students")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isCreated());

        assertEquals(2, studentsRepository.count());
        Student createdStudent = studentsRepository.findAllWithEnrollments().getLast();
        assertEquals(name, createdStudent.getName());
        assertEquals(cpf, createdStudent.getCpf());
        assertEquals(testEmail, createdStudent.getEmail());
        assertEquals(phone, createdStudent.getPhone());
        assertEquals(address, createdStudent.getAddress());
        assertEquals(1, createdStudent.getEnrollments().size());
        assertEquals(course.getId(), createdStudent.getEnrollments().get(0).getCourse().getId());
    }

    @Test
    public void testCreate_whenInvalidName() throws Exception {
        String name = "";
        String cpf = "987.654.321-00";
        String phone = "12345678910";
        String address = "Ladeira da gloria, 27";
        List<Long> courseIds = List.of(course.getId());
        StudentCreateRequest request = new StudentCreateRequest(name, cpf, testEmail, phone, address, courseIds);

        assertEquals(1, studentsRepository.count());

        mockMvc.perform(post("/students")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isBadRequest());

        assertEquals(1, studentsRepository.count());
    }

    @Test
    public void testCreate_whenInvalidCpf() throws Exception {
        String name = "Maria Test";
        String cpf = "987.654.321-XX";
        String phone = "12345678910";
        String address = "Ladeira da gloria, 27";
        List<Long> courseIds = List.of(course.getId());
        StudentCreateRequest request = new StudentCreateRequest(name, cpf, testEmail, phone, address, courseIds);

        assertEquals(1, studentsRepository.count());

        mockMvc.perform(post("/students")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isBadRequest());

        assertEquals(1, studentsRepository.count());
    }

    @Test
    public void testCreate_whenInvalidEmail() throws Exception {
        String name = "Maria Test";
        String cpf = "987.654.321-00";
        String phone = "12345678910";
        String address = "Ladeira da gloria, 27";
        List<Long> courseIds = List.of(course.getId());
        StudentCreateRequest request = new StudentCreateRequest(name, cpf, "invalid-email", phone, address, courseIds);

        assertEquals(1, studentsRepository.count());

        mockMvc.perform(post("/students")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isBadRequest());

        assertEquals(1, studentsRepository.count());
    }

    @Test
    public void testCreate_whenDuplicateEmail() throws Exception {
        String name = "Maria Test";
        String cpf = "987.654.321-00";
        String phone = "12345678910";
        String address = "Ladeira da gloria, 27";
        List<Long> courseIds = List.of(course.getId());
        StudentCreateRequest request = new StudentCreateRequest(name, cpf, student.getEmail(), phone, address, courseIds);

        assertEquals(1, studentsRepository.count());

        mockMvc.perform(post("/students")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isBadRequest());

        assertEquals(1, studentsRepository.count());
    }

    @Test
    public void testCreate_whenDuplicateCpf() throws Exception {
        String name = "Maria Test";
        String cpf = student.getCpf();
        String phone = "12345678910";
        String address = "Ladeira da gloria, 27";
        List<Long> courseIds = List.of(course.getId());
        StudentCreateRequest request = new StudentCreateRequest(name, cpf, testEmail, phone, address, courseIds);

        assertEquals(1, studentsRepository.count());

        mockMvc.perform(post("/students")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isBadRequest());

        assertEquals(1, studentsRepository.count());
    }

    @Test
    public void testCreate_whenInvalidPhone() throws Exception {
        String name = "Maria Test";
        String cpf = "987.654.321-00";
        String phone = "12345";
        String address = "Ladeira da gloria, 27";
        List<Long> courseIds = List.of(course.getId());
        StudentCreateRequest request = new StudentCreateRequest(name, cpf, testEmail, phone, address, courseIds);

        assertEquals(1, studentsRepository.count());

        mockMvc.perform(post("/students")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isBadRequest());

        assertEquals(1, studentsRepository.count());
    }

    @Test
    public void testCreate_whenInvalidAddress() throws Exception {
        String name = "Maria Test";
        String cpf = "987.654.321-00";
        String phone = "12345678910";
        String address = "";
        List<Long> courseIds = List.of(course.getId());
        StudentCreateRequest request = new StudentCreateRequest(name, cpf, testEmail, phone, address, courseIds);

        assertEquals(1, studentsRepository.count());

        mockMvc.perform(post("/students")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isBadRequest());

        assertEquals(1, studentsRepository.count());
    }

    @Test
    public void testCreate_whenInvalidCourseIds() throws Exception {
        String name = "Maria Test";
        String cpf = "987.654.321-00";
        String phone = "12345678910";
        String address = "Ladeira da gloria, 27";
        List<Long> courseIds = List.of();
        StudentCreateRequest request = new StudentCreateRequest(name, cpf, testEmail, phone, address, courseIds);

        assertEquals(1, studentsRepository.count());

        mockMvc.perform(post("/students")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isBadRequest());

        assertEquals(1, studentsRepository.count());
    }

    @Test
    public void testCreate_whenInvalidCourseDoesNotExist() throws Exception {
        String name = "Maria Test";
        String cpf = "987.654.321-00";
        String phone = "12345678910";
        String address = "Ladeira da gloria, 27";
        List<Long> courseIds = List.of(9999L);
        StudentCreateRequest request = new StudentCreateRequest(name, cpf, testEmail, phone, address, courseIds);

        assertEquals(1, studentsRepository.count());

        mockMvc.perform(post("/students")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isBadRequest());

        assertEquals(1, studentsRepository.count());
    }
}