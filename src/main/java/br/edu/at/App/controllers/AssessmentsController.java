package br.edu.at.App.controllers;

import br.edu.at.App.exceptions.EnrollmentNotFoundException;
import br.edu.at.App.requests.AssessmentRequest;
import br.edu.at.App.services.AssessmentsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assessments")
@AllArgsConstructor
public class AssessmentsController {
    private final AssessmentsService assessmentsService;

    @PostMapping
    public ResponseEntity<String> createAssessment(@Valid @RequestBody AssessmentRequest request) {
        try {
            assessmentsService.createAssessment(request.studentId(), request.courseId(), request.grade());
            return ResponseEntity.ok("Nota lançada com sucesso.");
        } catch (EnrollmentNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
