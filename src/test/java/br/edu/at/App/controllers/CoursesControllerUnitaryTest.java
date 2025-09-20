package br.edu.at.App.controllers;

import br.edu.at.App.entities.Course;
import br.edu.at.App.entities.Enrollment;
import br.edu.at.App.entities.Student;
import br.edu.at.App.exceptions.CourseNotFoundException;
import br.edu.at.App.exceptions.StudentNotFoundException;
import br.edu.at.App.requests.AddStudentRequest;
import br.edu.at.App.requests.CourseCreateRequest;
import br.edu.at.App.services.CoursesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CoursesControllerUnitaryTest {
    private CoursesController coursesController;
    private CoursesService coursesService;

    private String testEmail = "test.student@test.com.br";

    @BeforeEach
    public void setUp() {
        coursesService = mock(CoursesService.class);
        coursesController = new CoursesController(coursesService);
    }

    @Test
    public void testGetAll() {
        Long courseId = 1L;
        String courseName = "Course 1";
        String courseCode = "C1";
        Long id = 9999L;
        String name = "Test Student";
        String cpf = "123.456.789-00";
        String phone = "12345678910";
        String address = "Test Address";

        Course course = mock(Course.class);
        Student student = mock(Student.class);

        when(course.getId()).thenReturn(courseId);
        when(course.getName()).thenReturn(courseName);
        when(course.getCode()).thenReturn(courseCode);
        when(course.getStudents()).thenReturn(List.of(student));
        when(student.getId()).thenReturn(id);
        when(student.getName()).thenReturn(name);
        when(student.getCpf()).thenReturn(cpf);
        when(student.getEmail()).thenReturn(testEmail);
        when(student.getPhone()).thenReturn(phone);
        when(student.getAddress()).thenReturn(address);
        when(coursesService.getAll()).thenReturn(List.of(course));

        var response = coursesController.getAll();

        verify(coursesService, times(1)).getAll();
        verify(course, times(1)).getStudents();
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(courseId, response.getBody().get(0).id());
        assertEquals(courseName, response.getBody().get(0).name());
        assertEquals(courseCode, response.getBody().get(0).code());
        assertNotNull(response.getBody().get(0).students());
        assertEquals(1, response.getBody().get(0).students().size());
        assertEquals(id, response.getBody().get(0).students().get(0).id());
        assertEquals(name, response.getBody().get(0).students().get(0).name());
        assertEquals(cpf, response.getBody().get(0).students().get(0).cpf());
        assertEquals(testEmail, response.getBody().get(0).students().get(0).email());
        assertEquals(phone, response.getBody().get(0).students().get(0).phone());
        assertEquals(address, response.getBody().get(0).students().get(0).address());
    }

    @Test
    public void testGetApprovedStudents() throws CourseNotFoundException {
        Long courseId = 1L;
        String courseName = "Course 1";
        String courseCode = "C1";
        Long id = 9999L;
        String name = "Test Student";
        String cpf = "123.456.789-00";
        String phone = "12345678910";
        String address = "Test Address";

        Course course = mock(Course.class);
        Student student = mock(Student.class);

        when(course.getId()).thenReturn(courseId);
        when(course.getName()).thenReturn(courseName);
        when(course.getCode()).thenReturn(courseCode);
        when(course.getApprovedStudents()).thenReturn(List.of(student));
        when(student.getId()).thenReturn(id);
        when(student.getName()).thenReturn(name);
        when(student.getCpf()).thenReturn(cpf);
        when(student.getEmail()).thenReturn(testEmail);
        when(student.getPhone()).thenReturn(phone);
        when(student.getAddress()).thenReturn(address);
        when(coursesService.getById(courseId)).thenReturn(course);

        var response = coursesController.getApprovedStudents(courseId);

        verify(coursesService, times(1)).getById(courseId);
        verify(course, times(1)).getApprovedStudents();
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(courseId, response.getBody().id());
        assertEquals(courseName, response.getBody().name());
        assertEquals(courseCode, response.getBody().code());
        assertNotNull(response.getBody().students());
        assertEquals(1, response.getBody().students().size());
        assertEquals(id, response.getBody().students().get(0).id());
        assertEquals(name, response.getBody().students().get(0).name());
        assertEquals(cpf, response.getBody().students().get(0).cpf());
        assertEquals(testEmail, response.getBody().students().get(0).email());
        assertEquals(phone, response.getBody().students().get(0).phone());
        assertEquals(address, response.getBody().students().get(0).address());
    }

    @Test
    public void testGetFailedStudents() throws CourseNotFoundException {
        Long courseId = 1L;
        String courseName = "Course 1";
        String courseCode = "C1";
        Long id = 9999L;
        String name = "Test Student";
        String cpf = "123.456.789-00";
        String phone = "12345678910";
        String address = "Test Address";

        Course course = mock(Course.class);
        Student student = mock(Student.class);

        when(course.getId()).thenReturn(courseId);
        when(course.getName()).thenReturn(courseName);
        when(course.getCode()).thenReturn(courseCode);
        when(course.getFailedStudents()).thenReturn(List.of(student));
        when(student.getId()).thenReturn(id);
        when(student.getName()).thenReturn(name);
        when(student.getCpf()).thenReturn(cpf);
        when(student.getEmail()).thenReturn(testEmail);
        when(student.getPhone()).thenReturn(phone);
        when(student.getAddress()).thenReturn(address);
        when(coursesService.getById(courseId)).thenReturn(course);

        var response = coursesController.getFailedStudents(courseId);

        verify(coursesService, times(1)).getById(courseId);
        verify(course, times(1)).getFailedStudents();
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(courseId, response.getBody().id());
        assertEquals(courseName, response.getBody().name());
        assertEquals(courseCode, response.getBody().code());
        assertNotNull(response.getBody().students());
        assertEquals(1, response.getBody().students().size());
        assertEquals(id, response.getBody().students().get(0).id());
        assertEquals(name, response.getBody().students().get(0).name());
        assertEquals(cpf, response.getBody().students().get(0).cpf());
        assertEquals(testEmail, response.getBody().students().get(0).email());
        assertEquals(phone, response.getBody().students().get(0).phone());
        assertEquals(address, response.getBody().students().get(0).address());
    }

    @Test
    public void testCreate() {
        String courseName = "Course 1";
        String courseCode = "C1";

        var request = mock(CourseCreateRequest.class);
        when(request.name()).thenReturn(courseName);
        when(request.code()).thenReturn(courseCode);

        var response = coursesController.create(request);

        verify(coursesService, times(1)).create(courseName, courseCode);
        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Curso criado com sucesso.", response.getBody());
    }

    @Test
    public void testAddStudent() throws CourseNotFoundException, StudentNotFoundException {
        Long courseId = 1L;
        Long studentId = 9999L;

        var request = mock(AddStudentRequest.class);
        when(request.studentId()).thenReturn(studentId);

        var response = coursesController.addStudent(courseId, request);

        verify(coursesService, times(1)).addStudent(courseId, studentId);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Aluno adicionado ao curso com sucesso.", response.getBody());
    }

    @Test
    public void testAddStudent_whenCourseNotFound() throws CourseNotFoundException, StudentNotFoundException {
        Long courseId = 1L;
        Long studentId = 9999L;

        var request = mock(AddStudentRequest.class);
        when(request.studentId()).thenReturn(studentId);
        doThrow(new CourseNotFoundException()).when(coursesService).addStudent(courseId, studentId);

        var response = coursesController.addStudent(courseId, request);

        verify(coursesService, times(1)).addStudent(courseId, studentId);
        assertEquals(404, response.getStatusCode().value());
        assertEquals("Um ou mais cursos não foram encontrados.", response.getBody());
    }

    @Test
    public void testAddStudent_whenStudentNotFound() throws CourseNotFoundException, StudentNotFoundException {
        Long courseId = 1L;
        Long studentId = 9999L;

        var request = mock(AddStudentRequest.class);
        when(request.studentId()).thenReturn(studentId);
        doThrow(new StudentNotFoundException()).when(coursesService).addStudent(courseId, studentId);

        var response = coursesController.addStudent(courseId, request);

        verify(coursesService, times(1)).addStudent(courseId, studentId);
        assertEquals(404, response.getStatusCode().value());
        assertEquals("Aluno não encontrado.", response.getBody());
    }
}
