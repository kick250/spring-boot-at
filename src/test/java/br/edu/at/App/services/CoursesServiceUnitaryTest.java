package br.edu.at.App.services;

import br.edu.at.App.entities.Course;
import br.edu.at.App.entities.Enrollment;
import br.edu.at.App.entities.Student;
import br.edu.at.App.exceptions.CourseNotFoundException;
import br.edu.at.App.repositories.CoursesRepository;
import br.edu.at.App.repositories.EnrollmentsRepository;
import br.edu.at.App.repositories.StudentsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CoursesServiceUnitaryTest {
    private CoursesService coursesService;

    private CoursesRepository coursesRepository;
    private StudentsRepository studentsRepository;
    private EnrollmentsRepository enrollmentsRepository;

    @BeforeEach
    public void setUp() {
        coursesRepository = mock(CoursesRepository.class);
        studentsRepository = mock(StudentsRepository.class);
        enrollmentsRepository = mock(EnrollmentsRepository.class);
        coursesService = new CoursesService(coursesRepository, studentsRepository, enrollmentsRepository);
    }

    @Test
    public void testGetAll() {
        coursesService.getAll();

        verify(coursesRepository).findAll();
    }

    @Test
    public void testGetById() throws Exception {
        Long courseId = 1L;
        Course course = mock(Course.class);
        when(coursesRepository.findById(courseId)).thenReturn(Optional.of(course));

        coursesService.getById(courseId);

        verify(coursesRepository).findById(courseId);
    }

    @Test
    public void testGetById_whenNotFound() {
        Long courseId = 1L;

        when(coursesRepository.findById(courseId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CourseNotFoundException.class, () -> coursesService.getById(courseId));

        verify(coursesRepository).findById(courseId);
        assertEquals("Um ou mais cursos não foram encontrados.", exception.getMessage());
    }

    @Test
    public void testCreate() {
        String name = "Course Name";
        String code = "CSE101";

        coursesService.create(name, code);

        verify(coursesRepository).save(any(Course.class));
    }

    @Test
    public void testAddStudent() throws Exception {
        Long courseId = 1L;
        Long studentId = 1L;
        Course course = mock(Course.class);
        when(coursesRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(studentsRepository.findById(studentId)).thenReturn(Optional.of(mock(Student.class)));

        coursesService.addStudent(courseId, studentId);

        verify(coursesRepository).findById(courseId);
        verify(studentsRepository).findById(studentId);
        verify(enrollmentsRepository).save(any(Enrollment.class));
    }

    @Test
    public void testAddStudent_whenCourseNotFound() {
        Long courseId = 1L;
        Long studentId = 1L;

        when(coursesRepository.findById(courseId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CourseNotFoundException.class, () -> coursesService.addStudent(courseId, studentId));

        verify(coursesRepository).findById(courseId);
        verify(studentsRepository, times(0)).findById(studentId);
        verify(enrollmentsRepository, times(0)).save(any(Enrollment.class));
        assertEquals("Um ou mais cursos não foram encontrados.", exception.getMessage());
    }

    @Test
    public void testAddStudent_whenStudentNotFound() {
        Long courseId = 1L;
        Long studentId = 1L;
        Course course = mock(Course.class);
        when(coursesRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(studentsRepository.findById(studentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> coursesService.addStudent(courseId, studentId));

        verify(coursesRepository).findById(courseId);
        verify(studentsRepository).findById(studentId);
        verify(enrollmentsRepository, times(0)).save(any(Enrollment.class));
        assertEquals("Aluno não encontrado.", exception.getMessage());
    }
}
