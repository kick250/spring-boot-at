package br.edu.at.App.controllers;

import br.edu.at.App.exceptions.EnrollmentNotFoundException;
import br.edu.at.App.exceptions.InvalidGradeException;
import br.edu.at.App.requests.AssessmentRequest;
import br.edu.at.App.services.AssessmentsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AssessmentsControllerUnitaryTest {
    private AssessmentsController assessmentsController;

    private AssessmentsService assessmentsService;

    @BeforeEach
    public void setUp() {
        assessmentsService = org.mockito.Mockito.mock(AssessmentsService.class);
        assessmentsController = new AssessmentsController(assessmentsService);
    }

    @Test
    public void testCreateAssessment() throws EnrollmentNotFoundException, InvalidGradeException {
        Long studentId = 1L;
        Long courseId = 1L;
        double grade = 9.5;

        var request = mock(AssessmentRequest.class);
        when(request.studentId()).thenReturn(studentId);
        when(request.courseId()).thenReturn(courseId);
        when(request.grade()).thenReturn(grade);

        var response = assessmentsController.createAssessment(request);

        verify(assessmentsService, times(1)).createAssessment(studentId, courseId, grade);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Nota lançada com sucesso.", response.getBody());
    }

    @Test
    public void testCreateAssessment_whenInvalidGrade() throws EnrollmentNotFoundException, InvalidGradeException {
        Long studentId = 1L;
        Long courseId = 1L;
        double grade = 15.0;

        var request = mock(AssessmentRequest.class);
        when(request.studentId()).thenReturn(studentId);
        when(request.courseId()).thenReturn(courseId);
        when(request.grade()).thenReturn(grade);

        doThrow(new InvalidGradeException()).when(assessmentsService).createAssessment(studentId, courseId, grade);

        var response = assessmentsController.createAssessment(request);

        verify(assessmentsService, times(1)).createAssessment(studentId, courseId, grade);
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Nota inválida.", response.getBody());
    }

    @Test
    public void testCreateAssessment_whenNegativeGrade() throws EnrollmentNotFoundException, InvalidGradeException {
        Long studentId = 1L;
        Long courseId = 1L;
        double grade = -3.0;

        var request = mock(AssessmentRequest.class);
        when(request.studentId()).thenReturn(studentId);
        when(request.courseId()).thenReturn(courseId);
        when(request.grade()).thenReturn(grade);

        doThrow(new InvalidGradeException()).when(assessmentsService).createAssessment(studentId, courseId, grade);

        var response = assessmentsController.createAssessment(request);

        verify(assessmentsService, times(1)).createAssessment(studentId, courseId, grade);
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Nota inválida.", response.getBody());
    }

    @Test
    public void testCreateAssessment_whenEnrollmentNotFound() throws EnrollmentNotFoundException, InvalidGradeException {
        Long studentId = 1L;
        Long courseId = 1L;
        double grade = 9.5;

        var request = mock(AssessmentRequest.class);
        when(request.studentId()).thenReturn(studentId);
        when(request.courseId()).thenReturn(courseId);
        when(request.grade()).thenReturn(grade);

        doThrow(new EnrollmentNotFoundException()).when(assessmentsService).createAssessment(studentId, courseId, grade);

        var response = assessmentsController.createAssessment(request);

        verify(assessmentsService, times(1)).createAssessment(studentId, courseId, grade);
        assertEquals(404, response.getStatusCode().value());
        assertEquals("Matrícula não encontrada.", response.getBody());
    }
}
