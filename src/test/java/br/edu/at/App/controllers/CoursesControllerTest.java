package br.edu.at.App.controllers;

import br.edu.at.App.entities.Course;
import br.edu.at.App.entities.Enrollment;
import br.edu.at.App.entities.Student;
import br.edu.at.App.entities.Teacher;
import br.edu.at.App.repositories.CoursesRepository;
import br.edu.at.App.repositories.EnrollmentsRepository;
import br.edu.at.App.repositories.StudentsRepository;
import br.edu.at.App.repositories.TeachersRepository;
import br.edu.at.App.requests.AddStudentRequest;
import br.edu.at.App.requests.CourseCreateRequest;
import br.edu.at.App.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CoursesControllerTest {
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

    private Student student1;
    private Student student2;
    private Course course;
    private Enrollment enrollment;
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


        student1 = new Student("Joao Test", "123.456.789-00", "joao.test@test.com.br", "12345678901", "Ladeira da gloria, 26");
        studentsRepository.save(student1);
        student2 = new Student("Maria Test", "987.654.321-00", "maria.test@test.com.br", "12345678910", "Ladeira da gloria, 27");
        studentsRepository.save(student2);

        course = new Course( "Desenvolvimento de Serviços com Spring Boot", "QWERTY");
        coursesRepository.save(course);

        enrollment = new Enrollment(student1, course);
        enrollment.setGrade(10.0);
        enrollmentsRepository.save(enrollment);
        enrollment = new Enrollment(student2, course);
        enrollment.setGrade(5.0);
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
        mockMvc.perform(get("/courses"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetAll() throws Exception {
        course = coursesRepository.findByIdWithEnrollments(course.getId()).orElseThrow();

        mockMvc.perform(get("/courses")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(course.getId()))
                .andExpect(jsonPath("$[0].name").value(course.getName()))
                .andExpect(jsonPath("$[0].code").value(course.getCode()))
                .andExpect(jsonPath("$[0].students.length()").value(2))
                .andExpect(jsonPath("$[0].students[0].id").value(course.getStudents().get(0).getId()))
                .andExpect(jsonPath("$[0].students[0].name").value(course.getStudents().get(0).getName()))
                .andExpect(jsonPath("$[0].students[0].cpf").value(course.getStudents().get(0).getCpf()))
                .andExpect(jsonPath("$[0].students[0].email").value(course.getStudents().get(0).getEmail()))
                .andExpect(jsonPath("$[0].students[0].phone").value(course.getStudents().get(0).getPhone()))
                .andExpect(jsonPath("$[0].students[0].address").value(course.getStudents().get(0).getAddress()))
                .andExpect(jsonPath("$[0].students[1].id").value(course.getStudents().get(1).getId()))
                .andExpect(jsonPath("$[0].students[1].name").value(course.getStudents().get(1).getName()))
                .andExpect(jsonPath("$[0].students[1].cpf").value(course.getStudents().get(1).getCpf()))
                .andExpect(jsonPath("$[0].students[1].email").value(course.getStudents().get(1).getEmail()))
                .andExpect(jsonPath("$[0].students[1].phone").value(course.getStudents().get(1).getPhone()))
                .andExpect(jsonPath("$[0].students[1].address").value(course.getStudents().get(1).getAddress()));
    }

    @Test
    public void testGetApprovedStudents_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/courses/" + course.getId() + "/approved_students"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetApprovedStudents() throws Exception {
        course = coursesRepository.findByIdWithEnrollments(course.getId()).orElseThrow();

        mockMvc.perform(get("/courses/" + course.getId() + "/approved_students")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(course.getId()))
                .andExpect(jsonPath("$.name").value(course.getName()))
                .andExpect(jsonPath("$.code").value(course.getCode()))
                .andExpect(jsonPath("$.students.length()").value(1))
                .andExpect(jsonPath("$.students[0].id").value(student1.getId()))
                .andExpect(jsonPath("$.students[0].name").value(student1.getName()))
                .andExpect(jsonPath("$.students[0].cpf").value(student1.getCpf()))
                .andExpect(jsonPath("$.students[0].email").value(student1.getEmail()))
                .andExpect(jsonPath("$.students[0].phone").value(student1.getPhone()))
                .andExpect(jsonPath("$.students[0].address").value(student1.getAddress()));
    }

    @Test
    public void testGetFailedStudents_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/courses/" + course.getId() + "/failed_students"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetFailedStudents() throws Exception {
        course = coursesRepository.findByIdWithEnrollments(course.getId()).orElseThrow();

        mockMvc.perform(get("/courses/" + course.getId() + "/failed_students")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(course.getId()))
                .andExpect(jsonPath("$.name").value(course.getName()))
                .andExpect(jsonPath("$.code").value(course.getCode()))
                .andExpect(jsonPath("$.students.length()").value(1))
                .andExpect(jsonPath("$.students[0].id").value(student2.getId()))
                .andExpect(jsonPath("$.students[0].name").value(student2.getName()))
                .andExpect(jsonPath("$.students[0].cpf").value(student2.getCpf()))
                .andExpect(jsonPath("$.students[0].email").value(student2.getEmail()))
                .andExpect(jsonPath("$.students[0].phone").value(student2.getPhone()))
                .andExpect(jsonPath("$.students[0].address").value(student2.getAddress()));
    }

    @Test
    public void testGetApprovedStudents_CourseNotFound() throws Exception {
        mockMvc.perform(get("/courses/9999/approved_students")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetFailedStudents_CourseNotFound() throws Exception {
        mockMvc.perform(get("/courses/9999/failed_students")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreate_whenNotAuthenticated() throws Exception {
        String courseName = "Course Test";
        String courseCode = "ASDFGH";
        CourseCreateRequest request = new CourseCreateRequest(courseName, courseCode);

        assertEquals(1, coursesRepository.count());

        mockMvc.perform(post("/courses")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

        assertEquals(1, coursesRepository.count());
    }

    @Test
    public void testCreate() throws Exception {
        String courseName = "Course Test";
        String courseCode = "ASDFGH";
        CourseCreateRequest request = new CourseCreateRequest(courseName, courseCode);

        assertEquals(1, coursesRepository.count());

        mockMvc.perform(post("/courses")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isCreated());

        assertEquals(2, coursesRepository.count());

        Course createdCourse = coursesRepository.findAll().getLast();
        assertEquals(courseName, createdCourse.getName());
        assertEquals(courseCode, createdCourse.getCode());
    }

    @Test
    public void testCreate_whenInvalidName() throws Exception {
        String courseName = "";
        String courseCode = "ASDFGH";
        CourseCreateRequest request = new CourseCreateRequest(courseName, courseCode);

        assertEquals(1, coursesRepository.count());

        mockMvc.perform(post("/courses")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isBadRequest());

        assertEquals(1, coursesRepository.count());
    }

    @Test
    public void testCreate_whenInvalidCode() throws Exception {
        String courseName = "Course Test";
        String courseCode = "";
        CourseCreateRequest request = new CourseCreateRequest(courseName, courseCode);

        assertEquals(1, coursesRepository.count());

        mockMvc.perform(post("/courses")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isBadRequest());

        assertEquals(1, coursesRepository.count());
    }

    @Test
    public void testAddStudent() throws Exception {
        student1 = new Student("Igor Test", "123.456.789-11", "igor.test@test.com.br", "12345678911", "Ladeira da gloria, 28");
        studentsRepository.save(student1);
        var request = new AddStudentRequest(student1.getId());

        assertEquals(2, enrollmentsRepository.count());

        mockMvc.perform(post("/courses/" + course.getId() + "/add_student")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());

        assertEquals(3, enrollmentsRepository.count());
        var enrollment = enrollmentsRepository.findByStudentIdAndCourseId(student1.getId(), course.getId()).orElseThrow();
        assertNotNull(enrollment);
        assertEquals(student1.getId(), enrollment.getStudent().getId());
        assertEquals(course.getId(), enrollment.getCourse().getId());
    }

    @Test
    public void testAddStudent_whenCourseNotFound() throws Exception {
        var request = new AddStudentRequest(student1.getId());

        assertEquals(2, enrollmentsRepository.count());

        mockMvc.perform(post("/courses/9999/add_student")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());

        assertEquals(2, enrollmentsRepository.count());
    }

    @Test
    public void testAddStudent_whenStudentNotFound() throws Exception {
        var request = new AddStudentRequest(9999L);

        assertEquals(2, enrollmentsRepository.count());

        mockMvc.perform(post("/courses/" + course.getId() + "/add_student")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());

        assertEquals(2, enrollmentsRepository.count());
    }
}