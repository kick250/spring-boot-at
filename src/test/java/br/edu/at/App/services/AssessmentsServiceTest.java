package br.edu.at.App.services;

import br.edu.at.App.entities.Course;
import br.edu.at.App.entities.Enrollment;
import br.edu.at.App.entities.Student;
import br.edu.at.App.exceptions.EnrollmentNotFoundException;
import br.edu.at.App.exceptions.InvalidGradeException;
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
class AssessmentsServiceTest {
    @Autowired
    private AssessmentsService assessmentsService;

    @Autowired
    private StudentsRepository studentsRepository;
    @Autowired
    private CoursesRepository coursesRepository;
    @Autowired
    private EnrollmentsRepository enrollmentsRepository;

    private Student student;
    private Course course;
    private Enrollment enrollment;

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
        enrollmentsRepository.save(enrollment);
    }

    @AfterEach
    void tearDown() {
        enrollmentsRepository.deleteAll();
        coursesRepository.deleteAll();
        studentsRepository.deleteAll();
    }

    @Test
    public void testCreateAssessment() throws InvalidGradeException, EnrollmentNotFoundException {
        double grade = 8.5;

        assertNull(enrollment.getGrade());

        assessmentsService.createAssessment(student.getId(), course.getId(), grade);

        enrollment = enrollmentsRepository.findByStudentIdAndCourseId(student.getId(), course.getId()).orElseThrow();

        assertNotNull(enrollment);
        assertEquals(grade, enrollment.getGrade());
        assertEquals(student.getId(), enrollment.getStudent().getId());
        assertEquals(course.getId(), enrollment.getCourse().getId());
    }

    @Test
    public void testCreateAssessment_whenInvalidGrade() {
        double invalidGrade = 11.0;

        Exception exception = assertThrows(InvalidGradeException.class, () -> {
            assessmentsService.createAssessment(student.getId(), course.getId(), invalidGrade);
        });

        assertEquals("Nota inválida.", exception.getMessage());
    }

    @Test
    public void testCreateAssessment_whenNegativeGrade() {
        double invalidGrade = -1.0;

        Exception exception = assertThrows(InvalidGradeException.class, () -> {
            assessmentsService.createAssessment(student.getId(), course.getId(), invalidGrade);
        });

        assertEquals("Nota inválida.", exception.getMessage());
    }

    @Test
    public void testCreateAssessment_whenEnrollmentNotFound() {
        double grade = 7.0;
        Long nonExistentStudentId = 999L;
        Long nonExistentCourseId = 888L;

        Exception exception = assertThrows(EnrollmentNotFoundException.class, () -> {
            assessmentsService.createAssessment(nonExistentStudentId, nonExistentCourseId, grade);
        });

        assertEquals("Matrícula não encontrada.", exception.getMessage());
    }
}