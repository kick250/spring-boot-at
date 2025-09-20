package br.edu.at.App.repositories;

import br.edu.at.App.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeachersRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByEmail(String email);
}
