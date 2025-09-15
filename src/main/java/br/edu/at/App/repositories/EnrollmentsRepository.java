package br.edu.at.App.repositories;

import br.edu.at.App.entities.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnrollmentsRepository extends JpaRepository<Enrollment, Long> {

    public Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);
}
