package br.edu.at.App.services;

import br.edu.at.App.entities.Course;
import br.edu.at.App.entities.Enrollment;
import br.edu.at.App.entities.Student;
import br.edu.at.App.exceptions.CourseNotFoundException;
import br.edu.at.App.repositories.CoursesRepository;
import br.edu.at.App.repositories.EnrollmentsRepository;
import br.edu.at.App.repositories.StudentsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class StudentsService {
    private final StudentsRepository studentsRepository;
    private final CoursesRepository coursesRepository;
    private final EnrollmentsRepository enrollmentsRepository;

    public List<Student> getAll() {
        return studentsRepository.findAll();
    }

    public void create(String name, String cpf, String email, String phone, String address, Set<Long> courseIds) throws CourseNotFoundException {
        if (courseIds.isEmpty()) throw new IllegalArgumentException("O aluno deve estar matriculado em pelo menos um curso.");

        List<Course> courses = getCourses(courseIds);

        Student student = new Student(name, cpf, email, phone, address);
        studentsRepository.save(student);

        for (Course course : courses) {
            Enrollment enrollment = new Enrollment(student, course);
            enrollmentsRepository.save(enrollment);
        }
    }

    private List<Course> getCourses(Set<Long> courseIds) throws CourseNotFoundException {
        List<Course> courses = coursesRepository.findAllById(courseIds);

        if (courses.size() != courseIds.size()) throw new CourseNotFoundException();

        return courses;
    }
}
