package br.edu.at.App.services;

import br.edu.at.App.entities.Enrollment;
import br.edu.at.App.exceptions.EnrollmentNotFoundException;
import br.edu.at.App.exceptions.InvalidGradeException;
import br.edu.at.App.repositories.EnrollmentsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AssessmentsServiceUnitaryTest {
    private AssessmentsService assessmentsService;

    private EnrollmentsRepository enrollmentsRepository;

    @BeforeEach
    public void setup() {
        enrollmentsRepository = org.mockito.Mockito.mock(EnrollmentsRepository.class);
        assessmentsService = new AssessmentsService(enrollmentsRepository);
    }

    @Test
    public void testCreateAssessment() throws InvalidGradeException, EnrollmentNotFoundException {
        Long studentId = 1L;
        Long courseId = 1L;
        double grade = 8.5;

        Enrollment enrollment = mock(Enrollment.class);
        when(enrollmentsRepository.findByStudentIdAndCourseId(studentId, courseId)).thenReturn(Optional.of(enrollment));

        assessmentsService.createAssessment(studentId, courseId, grade);

        verify(enrollment).setGrade(grade);
        verify(enrollmentsRepository).save(enrollment);
    }

    @Test
    public void testCreateAssessment_whenInvalidGrade() {
        Long studentId = 1L;
        Long courseId = 1L;
        double invalidGrade = 11.0;

        Exception exception = assertThrows(InvalidGradeException.class, () -> {
            assessmentsService.createAssessment(studentId, courseId, invalidGrade);
        });

        assertEquals("Nota inválida.", exception.getMessage());
        verify(enrollmentsRepository, times(0)).findByStudentIdAndCourseId(anyLong(), anyLong());
        verify(enrollmentsRepository, times(0)).save(any(Enrollment.class));
    }

    @Test
    public void testCreateAssessment_whenNegativeGrade() {
        Long studentId = 1L;
        Long courseId = 1L;
        double invalidGrade = -1.0;

        Exception exception = assertThrows(InvalidGradeException.class, () -> {
            assessmentsService.createAssessment(studentId, courseId, invalidGrade);
        });

        assertEquals("Nota inválida.", exception.getMessage());
        verify(enrollmentsRepository, times(0)).findByStudentIdAndCourseId(anyLong(), anyLong());
        verify(enrollmentsRepository, times(0)).save(any(Enrollment.class));
    }

    @Test
    public void testCreateAssessment_whenEnrollmentNotFound() {
        Long studentId = 1L;
        Long courseId = 1L;
        double grade = 8.5;

        when(enrollmentsRepository.findByStudentIdAndCourseId(studentId, courseId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EnrollmentNotFoundException.class, () -> {
            assessmentsService.createAssessment(studentId, courseId, grade);
        });

        assertEquals("Matrícula não encontrada.", exception.getMessage());
        verify(enrollmentsRepository).findByStudentIdAndCourseId(studentId, courseId);
        verify(enrollmentsRepository, times(0)).save(any(Enrollment.class));
    }
}
