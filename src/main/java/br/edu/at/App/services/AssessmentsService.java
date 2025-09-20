package br.edu.at.App.services;

import br.edu.at.App.entities.Enrollment;
import br.edu.at.App.exceptions.EnrollmentNotFoundException;
import br.edu.at.App.exceptions.InvalidGradeException;
import br.edu.at.App.repositories.EnrollmentsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AssessmentsService {
    private final EnrollmentsRepository enrollmentsRepository;

    public void createAssessment(Long studentId, Long courseId, double grade) throws EnrollmentNotFoundException, InvalidGradeException {
        if (grade < 0.0 || grade > 10.0) throw new InvalidGradeException();

        Enrollment enrollment = enrollmentsRepository.findByStudentIdAndCourseId(studentId, courseId).orElseThrow(EnrollmentNotFoundException::new);
        enrollment.setGrade(grade);

        enrollmentsRepository.save(enrollment);
    }
}
