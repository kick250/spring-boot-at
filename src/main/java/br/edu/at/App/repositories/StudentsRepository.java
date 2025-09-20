package br.edu.at.App.repositories;

import br.edu.at.App.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentsRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s FROM Student s JOIN FETCH s.enrollments e JOIN FETCH e.course")
    public List<Student> findAllWithEnrollments();

    public boolean existsByEmail(String email);

    public boolean existsByCpf(String cpf);
}
