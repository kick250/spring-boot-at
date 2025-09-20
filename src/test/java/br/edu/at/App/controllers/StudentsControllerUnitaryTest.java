package br.edu.at.App.controllers;

import br.edu.at.App.entities.Enrollment;
import br.edu.at.App.entities.Student;
import br.edu.at.App.exceptions.CourseNotFoundException;
import br.edu.at.App.exceptions.InvalidCoursesQuantity;
import br.edu.at.App.exceptions.StudentCpfAlreadyExists;
import br.edu.at.App.exceptions.StudentEmailAlreadyExists;
import br.edu.at.App.requests.StudentCreateRequest;
import br.edu.at.App.services.StudentsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

public class StudentsControllerUnitaryTest {
    private StudentsController studentsController;
    private StudentsService studentsService;

    private String testEmail = "test.student@test.com.br";

    @BeforeEach
    public void setUp() {
        studentsService = mock(StudentsService.class);
        studentsController = new StudentsController(studentsService);
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
        when(studentsService.getAll()).thenReturn(List.of(student));

        var response = studentsController.getAll();

        verify(studentsService, times(1)).getAll();
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(id, response.getBody().get(0).id());
        assertEquals(name, response.getBody().get(0).name());
        assertEquals(cpf, response.getBody().get(0).cpf());
        assertEquals(testEmail, response.getBody().get(0).email());
        assertEquals(phone, response.getBody().get(0).phone());
        assertEquals(address, response.getBody().get(0).address());
        assertEquals(1, response.getBody().get(0).courses().size());
        assertEquals(courseId, response.getBody().get(0).courses().get(0).id());
        assertEquals(courseName, response.getBody().get(0).courses().get(0).name());
        assertEquals(courseCode, response.getBody().get(0).courses().get(0).code());
    }

    @Test
    public void testCreate() throws CourseNotFoundException, StudentEmailAlreadyExists, InvalidCoursesQuantity, StudentCpfAlreadyExists {
        String name = "Test Student";
        String cpf = "123.456.789-00";
        String phone = "12345678910";
        String address = "Test Address";
        Set<Long> courseIds = Set.of(1L, 2L);

        StudentCreateRequest request = mock(StudentCreateRequest.class);
        when(request.name()).thenReturn(name);
        when(request.cpf()).thenReturn(cpf);
        when(request.email()).thenReturn(testEmail);
        when(request.phone()).thenReturn(phone);
        when(request.address()).thenReturn(address);
        when(request.getUniqueCourseIds()).thenReturn(courseIds);

        var response = studentsController.create(request);

        verify(studentsService, times(1)).create(name, cpf, testEmail, phone, address, courseIds);
        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Aluno criado com sucesso.", response.getBody());
    }

    @Test
    public void testCreate_whenCourseNotFoundException() throws CourseNotFoundException, StudentEmailAlreadyExists, InvalidCoursesQuantity, StudentCpfAlreadyExists {
        String name = "Test Student";
        String cpf = "123.456.789-00";
        String phone = "12345678910";
        String address = "Test Address";
        Set<Long> courseIds = Set.of(1L, 2L);

        StudentCreateRequest request = mock(StudentCreateRequest.class);
        when(request.name()).thenReturn(name);
        when(request.cpf()).thenReturn(cpf);
        when(request.email()).thenReturn(testEmail);
        when(request.phone()).thenReturn(phone);
        when(request.address()).thenReturn(address);
        when(request.getUniqueCourseIds()).thenReturn(courseIds);

        doThrow(new CourseNotFoundException()).when(studentsService).create(name, cpf, testEmail, phone, address, courseIds);

        var response = studentsController.create(request);

        verify(studentsService, times(1)).create(name, cpf, testEmail, phone, address, courseIds);
        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Um ou mais cursos não foram encontrados.", response.getBody());
    }

    @Test
    public void testCreate_whenStudentEmailAlreadyExists() throws CourseNotFoundException, StudentEmailAlreadyExists, InvalidCoursesQuantity, StudentCpfAlreadyExists {
        String name = "Test Student";
        String cpf = "123.456.789-00";
        String phone = "12345678910";
        String address = "Test Address";
        Set<Long> courseIds = Set.of(1L, 2L);

        StudentCreateRequest request = mock(StudentCreateRequest.class);
        when(request.name()).thenReturn(name);
        when(request.cpf()).thenReturn(cpf);
        when(request.email()).thenReturn(testEmail);
        when(request.phone()).thenReturn(phone);
        when(request.address()).thenReturn(address);
        when(request.getUniqueCourseIds()).thenReturn(courseIds);

        doThrow(new StudentEmailAlreadyExists()).when(studentsService).create(name, cpf, testEmail, phone, address, courseIds);

        var response = studentsController.create(request);

        verify(studentsService, times(1)).create(name, cpf, testEmail, phone, address, courseIds);
        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("O email desse aluno já está em uso.", response.getBody());
    }

    @Test
    public void testCreate_whenStudentCpfAlreadyExists() throws CourseNotFoundException, StudentEmailAlreadyExists, InvalidCoursesQuantity, StudentCpfAlreadyExists {
        String name = "Test Student";
        String cpf = "123.456.789-00";
        String phone = "12345678910";
        String address = "Test Address";
        Set<Long> courseIds = Set.of(1L, 2L);

        StudentCreateRequest request = mock(StudentCreateRequest.class);
        when(request.name()).thenReturn(name);
        when(request.cpf()).thenReturn(cpf);
        when(request.email()).thenReturn(testEmail);
        when(request.phone()).thenReturn(phone);
        when(request.address()).thenReturn(address);
        when(request.getUniqueCourseIds()).thenReturn(courseIds);

        doThrow(new StudentCpfAlreadyExists()).when(studentsService).create(name, cpf, testEmail, phone, address, courseIds);

        var response = studentsController.create(request);

        verify(studentsService, times(1)).create(name, cpf, testEmail, phone, address, courseIds);
        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Já existe um aluno cadastrado com este CPF.", response.getBody());
    }

    @Test
    public void testCreate_whenInvalidCoursesQuantity() throws CourseNotFoundException, StudentEmailAlreadyExists, InvalidCoursesQuantity, StudentCpfAlreadyExists {
        String name = "Test Student";
        String cpf = "123.456.789-00";
        String phone = "12345678910";
        String address = "Test Address";
        Set<Long> courseIds = Set.of(1L, 2L);

        StudentCreateRequest request = mock(StudentCreateRequest.class);
        when(request.name()).thenReturn(name);
        when(request.cpf()).thenReturn(cpf);
        when(request.email()).thenReturn(testEmail);
        when(request.phone()).thenReturn(phone);
        when(request.address()).thenReturn(address);
        when(request.getUniqueCourseIds()).thenReturn(courseIds);

        doThrow(new InvalidCoursesQuantity()).when(studentsService).create(name, cpf, testEmail, phone, address, courseIds);

        var response = studentsController.create(request);

        verify(studentsService, times(1)).create(name, cpf, testEmail, phone, address, courseIds);
        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("O aluno deve estar matriculado em pelo menos um curso.", response.getBody());
    }
}
