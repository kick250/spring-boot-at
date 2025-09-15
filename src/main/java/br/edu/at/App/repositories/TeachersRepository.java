package br.edu.at.App.repositories;

import br.edu.at.App.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeachersRepository extends JpaRepository<Teacher, Long> {

}
