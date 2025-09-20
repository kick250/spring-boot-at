package br.edu.at.App.services;

import br.edu.at.App.entities.Course;
import br.edu.at.App.entities.Enrollment;
import br.edu.at.App.entities.Student;
import br.edu.at.App.exceptions.CourseNotFoundException;
import br.edu.at.App.exceptions.StudentNotFoundException;
import br.edu.at.App.repositories.CoursesRepository;
import br.edu.at.App.repositories.EnrollmentsRepository;
import br.edu.at.App.repositories.StudentsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CoursesServiceTest {
    @Autowired
    private CoursesService coursesService;

    @Autowired
    private StudentsRepository studentsRepository;
    @Autowired
    private CoursesRepository coursesRepository;
    @Autowired
    private EnrollmentsRepository enrollmentsRepository;

    private Course course;
    private Enrollment enrollment;

    @BeforeEach
    public void setup() {
        enrollmentsRepository.deleteAll();
        coursesRepository.deleteAll();
        studentsRepository.deleteAll();

        course = new Course( "Desenvolvimento de Serviços com Spring Boot", "QWERTY");
        coursesRepository.save(course);
    }

    @AfterEach
    public void tearDown() {
        enrollmentsRepository.deleteAll();
        coursesRepository.deleteAll();
        studentsRepository.deleteAll();
    }

    @Test
    public void testGetAll() {
        var result = coursesService.getAll();

        assertEquals(1, result.size());
        assertEquals(course.getId(), result.get(0).getId());
        assertEquals(course.getName(), result.get(0).getName());
        assertEquals(course.getCode(), result.get(0).getCode());
    }

    @Test
    public void testGetById() throws CourseNotFoundException {
        var result = coursesService.getById(course.getId());

        assertEquals(course.getId(), result.getId());
        assertEquals(course.getName(), result.getName());
        assertEquals(course.getCode(), result.getCode());
    }

    @Test
    public void testGetById_whenCourseNotFoundException() {
        Exception exception = assertThrows(CourseNotFoundException.class, () -> {
            coursesService.getById(999L);
        });

        assertEquals("Um ou mais cursos não foram encontrados.", exception.getMessage());
    }

    @Test
    public void testCreate() {
        assertEquals(1, coursesRepository.count());

        coursesService.create("Curso Teste", "ASDFGH");

        assertEquals(2, coursesRepository.count());
    }

    @Test
    public void testAddStudent() throws CourseNotFoundException, StudentNotFoundException {
        Student student = new Student("Igor Test", "123.456.789-11", "igor.test@test.com.br", "12345678911", "Ladeira da gloria, 28");
        studentsRepository.save(student);

        assertEquals(0, enrollmentsRepository.count());

        coursesService.addStudent(course.getId(), student.getId());

        assertEquals(1, enrollmentsRepository.count());
    }

    @Test
    public void testAddStudent_whenCourseNotFoundException() {
        Student student = new Student("Igor Test", "123.456.789-11", "igor.test@test.com.br", "12345678911", "Ladeira da gloria, 28");
        studentsRepository.save(student);

        Exception exception = assertThrows(CourseNotFoundException.class, () -> {
            coursesService.addStudent(999L, student.getId());
        });

        assertEquals("Um ou mais cursos não foram encontrados.", exception.getMessage());
    }

    @Test
    public void testAddStudent_whenStudentNotFoundException() {
        Exception exception = assertThrows(StudentNotFoundException.class, () -> {
            coursesService.addStudent(course.getId(), 999L);
        });

        assertEquals("Aluno não encontrado.", exception.getMessage());
    }
}