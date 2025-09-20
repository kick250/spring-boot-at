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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class StudentsServiceUnitaryTest {
    private StudentsService studentsService;
    private StudentsRepository studentsRepository;
    private CoursesRepository coursesRepository;
    private EnrollmentsRepository enrollmentsRepository;

    private String testEmail = "test.student@test.com.br";

    @BeforeEach
    public void setUp() {
        studentsRepository = mock(StudentsRepository.class);
        coursesRepository = mock(CoursesRepository.class);
        enrollmentsRepository = mock(EnrollmentsRepository.class);
        studentsService = new StudentsService(studentsRepository, coursesRepository, enrollmentsRepository);
    }

    @Test
    public void testGetAll() {
        Long id = 9999L;
        String name = "Test Student";
        String cpf = "123.456.789-00";
        String phone = "12345678910";
        String address = "Test Address";
        Long courseId = 8888L;
        String courseName = "Desenvolvimento de Serviços com Spring Boot";
        String courseCode = "QWERTY";

        Student student = mock(Student.class);
        Enrollment enrollment = mock(Enrollment.class);
        when(student.getId()).thenReturn(id);
        when(student.getName()).thenReturn(name);
        when(student.getCpf()).thenReturn(cpf);
        when(student.getEmail()).thenReturn(testEmail);
        when(student.getPhone()).thenReturn(phone);
        when(student.getAddress()).thenReturn(address);
        when(student.getEnrollments()).thenReturn(List.of(enrollment));
        when(enrollment.getCourseId()).thenReturn(courseId);
        when(enrollment.getCourseName()).thenReturn(courseName);
        when(enrollment.getCourseCode()).thenReturn(courseCode);
        when(studentsRepository.findAll()).thenReturn(List.of(student));

        var result = studentsService.getAll();

        verify(studentsRepository, times(1)).findAll();
        assertEquals(1, result.size());
        assertEquals(id, result.get(0).getId());
        assertEquals(name, result.get(0).getName());
        assertEquals(cpf, result.get(0).getCpf());
        assertEquals(testEmail, result.get(0).getEmail());
        assertEquals(phone, result.get(0).getPhone());
        assertEquals(address, result.get(0).getAddress());
        assertEquals(1, result.get(0).getEnrollments().size());
        assertEquals(courseId, result.get(0).getEnrollments().get(0).getCourseId());
        assertEquals(courseName, result.get(0).getEnrollments().get(0).getCourseName());
        assertEquals(courseCode, result.get(0).getEnrollments().get(0).getCourseCode());
    }

    @Test
    public void testCreate() throws CourseNotFoundException, StudentEmailAlreadyExists, InvalidCoursesQuantity, StudentCpfAlreadyExists {
        String name = "Test Student";
        String cpf = "123.456.789-00";
        String phone = "12345678910";
        String address = "Test Address";
        Long courseId = 8888L;

        when(studentsRepository.existsByEmail(testEmail)).thenReturn(false);
        when(coursesRepository.findAllById(Set.of(courseId))).thenReturn(List.of(mock(Course.class)));

        studentsService.create(name, cpf, testEmail, phone, address, Set.of(courseId));

        verify(studentsRepository, times(1)).existsByEmail(testEmail);
        verify(studentsRepository, times(1)).save(any(Student.class));
        verify(enrollmentsRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    public void testCreate_whenCourseNotFound() {
        String name = "Test Student";
        String cpf = "123.456.789-00";
        String phone = "12345678910";
        String address = "Test Address";
        Long courseId = 8888L;

        when(studentsRepository.existsByEmail(testEmail)).thenReturn(false);
        when(coursesRepository.findAllById(Set.of(courseId))).thenReturn(List.of());

        Exception exception = assertThrows(CourseNotFoundException.class, () -> {
            studentsService.create(name, cpf, testEmail, phone, address, Set.of(courseId));
        });

        verify(studentsRepository, times(1)).existsByEmail(testEmail);
        verify(studentsRepository, times(0)).save(any(Student.class));
        verify(enrollmentsRepository, times(0)).save(any(Enrollment.class));
        assertEquals("Um ou mais cursos não foram encontrados.", exception.getMessage());
    }

    @Test
    public void testCreate_whenStudentEmailAlreadyExists() {
        String name = "Test Student";
        String cpf = "123.456.789-00";
        String phone = "12345678910";
        String address = "Test Address";
        Long courseId = 8888L;

        when(studentsRepository.existsByEmail(testEmail)).thenReturn(true);

        Exception exception = assertThrows(StudentEmailAlreadyExists.class, () -> {
            studentsService.create(name, cpf, testEmail, phone, address, Set.of(courseId));
        });

        verify(studentsRepository, times(1)).existsByEmail(testEmail);
        verify(studentsRepository, times(0)).save(any(Student.class));
        verify(enrollmentsRepository, times(0)).save(any(Enrollment.class));
        assertEquals("O email desse aluno já está em uso.", exception.getMessage());
    }

    @Test
    public void testCreate_whenStudentCpfAlreadyExists() {
        String name = "Test Student";
        String cpf = "123.456.789-00";
        String phone = "12345678910";
        String address = "Test Address";
        Long courseId = 8888L;

        when(studentsRepository.existsByEmail(testEmail)).thenReturn(false);
        when(studentsRepository.existsByCpf(cpf)).thenReturn(true);

        Exception exception = assertThrows(StudentCpfAlreadyExists.class, () -> {
            studentsService.create(name, cpf, testEmail, phone, address, Set.of(courseId));
        });

        verify(studentsRepository, times(1)).existsByEmail(testEmail);
        verify(studentsRepository, times(1)).existsByCpf(cpf);
        verify(studentsRepository, times(0)).save(any(Student.class));
        verify(enrollmentsRepository, times(0)).save(any(Enrollment.class));
        assertEquals("Já existe um aluno cadastrado com este CPF.", exception.getMessage());
    }

    @Test
    public void testCreate_whenInvalidCoursesQuantity() {
        String name = "Test Student";
        String cpf = "123.456.789-00";
        String phone = "12345678910";
        String address = "Test Address";

        Exception exception = assertThrows(InvalidCoursesQuantity.class, () -> {
            studentsService.create(name, cpf, testEmail, phone, address, Set.of());
        });

        verify(studentsRepository, times(0)).existsByEmail(anyString());
        verify(studentsRepository, times(0)).save(any(Student.class));
        verify(enrollmentsRepository, times(0)).save(any(Enrollment.class));
        assertEquals("O aluno deve estar matriculado em pelo menos um curso.", exception.getMessage());
    }

}
