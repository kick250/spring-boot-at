package br.edu.at.App.repositories;

import br.edu.at.App.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentsRepository extends JpaRepository<Student, Long> {

}
