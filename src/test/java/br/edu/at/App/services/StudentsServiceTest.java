package br.edu.at.App.services;

import br.edu.at.App.entities.Course;
import br.edu.at.App.entities.Enrollment;
import br.edu.at.App.entities.Student;
import br.edu.at.App.exceptions.CourseNotFoundException;
import br.edu.at.App.exceptions.InvalidCoursesQuantity;
import br.edu.at.App.exceptions.StudentCpfAlreadyExists;
import br.edu.at.App.exceptions.StudentEmailAlreadyExists;
import br.edu.at.App.repositories.CoursesRepository;
import br.edu.at.App.repositories.EnrollmentsRepository;
import br.edu.at.App.repositories.StudentsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudentsServiceTest {
    @Autowired
    private StudentsService studentsService;

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
    void setUp() {
        enrollmentsRepository.deleteAll();
        coursesRepository.deleteAll();
        studentsRepository.deleteAll();

        student = new Student("Joao Test", "123.456.789-00", "joao.test@test.com.br", "12345678901", "Ladeira da gloria, 26");
        studentsRepository.save(student);

        course = new Course( "Desenvolvimento de Serviços com Spring Boot", "QWERTY");
        coursesRepository.save(course);

        enrollment = new Enrollment(student, course);
        enrollment.setGrade(10.0);
        enrollmentsRepository.save(enrollment);
    }

    @AfterEach
    void tearDown() {
        enrollmentsRepository.deleteAll();
        coursesRepository.deleteAll();
        studentsRepository.deleteAll();
    }

    @Test
    public void testGetAll() {
        var result = studentsService.getAll();

        assertEquals(1, result.size());
        assertEquals(student.getId(), result.get(0).getId());
        assertEquals(student.getName(), result.get(0).getName());
        assertEquals(student.getCpf(), result.get(0).getCpf());
        assertEquals(student.getEmail(), result.get(0).getEmail());
        assertEquals(student.getPhone(), result.get(0).getPhone());
        assertEquals(student.getAddress(), result.get(0).getAddress());
    }

    @Test
    public void testCreate() throws CourseNotFoundException, StudentEmailAlreadyExists, InvalidCoursesQuantity, StudentCpfAlreadyExists {
        String name = "Maria Test";
        String cpf = "987.654.321-00";
        String phone = "12345678910";
        String address = "Ladeira da gloria, 27";
        Set<Long> courseIds = Set.of(course.getId());

        assertEquals(1, studentsRepository.count());

        studentsService.create(name, cpf, testEmail, phone, address, courseIds);

        assertEquals(2, studentsRepository.count());
        var createdStudent = studentsRepository.findAll().getLast();
        assertEquals(name, createdStudent.getName());
        assertEquals(cpf, createdStudent.getCpf());
        assertEquals(testEmail, createdStudent.getEmail());
        assertEquals(phone, createdStudent.getPhone());
        assertEquals(address, createdStudent.getAddress());
    }

    @Test
    public void testCreate_whenCourseNotFoundException() {
        String name = "Maria Test";
        String cpf = "987.654.321-00";
        String phone = "12345678910";
        String address = "Ladeira da gloria, 27";
        Set<Long> courseIds = Set.of(999L);

        Exception exception = assertThrows(CourseNotFoundException.class, () -> {
            studentsService.create(name, cpf, testEmail, phone, address, courseIds);
        });

        assertEquals("Um ou mais cursos não foram encontrados.", exception.getMessage());
    }

    @Test
    public void testCreate_whenStudentEmailAlreadyExists() {
        String name = "Maria Test";
        String cpf = "987.654.321-00";
        String phone = "12345678910";
        String address = "Ladeira da gloria, 27";
        Set<Long> courseIds = Set.of(course.getId());

        Exception exception = assertThrows(StudentEmailAlreadyExists.class, () -> {
            studentsService.create(name, cpf, student.getEmail(), phone, address, courseIds);
        });

        assertEquals("O email desse aluno já está em uso.", exception.getMessage());
    }

    @Test
    public void testCreate_whenStudentCpfAlreadyExists() {
        String name = "Maria Test";
        String cpf = student.getCpf();
        String phone = "12345678910";
        String address = "Ladeira da gloria, 27";
        Set<Long> courseIds = Set.of(course.getId());

        Exception exception = assertThrows(StudentCpfAlreadyExists.class, () -> {
            studentsService.create(name, cpf, testEmail, phone, address, courseIds);
        });

        assertEquals("Já existe um aluno cadastrado com este CPF.", exception.getMessage());
    }

    @Test
    public void testCreate_whenInvalidCoursesQuantity() {
        String name = "Maria Test";
        String cpf = "987.654.321-00";
        String phone = "12345678910";
        String address = "Ladeira da gloria, 27";
        Set<Long> courseIds = Set.of();

        Exception exception = assertThrows(InvalidCoursesQuantity.class, () -> {
            studentsService.create(name, cpf, testEmail, phone, address, courseIds);
        });

        assertEquals("O aluno deve estar matriculado em pelo menos um curso.", exception.getMessage());
    }
}