package br.edu.at.App.repositories;

import br.edu.at.App.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CoursesRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c FROM Course c JOIN FETCH c.enrollments e JOIN FETCH e.student")
    Optional<Course> findByIdWithEnrollments(Long id);
}
