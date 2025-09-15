package br.edu.at.App.repositories;

import br.edu.at.App.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursesRepository extends JpaRepository<Course, Long> {

}
