package br.edu.at.App.services;

import br.edu.at.App.entities.Course;
import br.edu.at.App.entities.Enrollment;
import br.edu.at.App.entities.Student;
import br.edu.at.App.exceptions.CourseNotFoundException;
import br.edu.at.App.exceptions.StudentNotFoundException;
import br.edu.at.App.repositories.CoursesRepository;
import br.edu.at.App.repositories.EnrollmentsRepository;
import br.edu.at.App.repositories.StudentsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CoursesService {
    private final CoursesRepository coursesRepository;
    private final StudentsRepository studentsRepository;
    private final EnrollmentsRepository enrollmentsRepository;

    public List<Course> getAll() {
        return coursesRepository.findAll();
    }

    public Course getById(Long courseId) throws CourseNotFoundException {
        return coursesRepository.findById(courseId).orElseThrow(CourseNotFoundException::new);
    }

    public void create(String name, String code) {
        Course course = new Course(name, code);
        coursesRepository.save(course);
    }

    public void addStudent(Long courseId, Long studentId) throws CourseNotFoundException, StudentNotFoundException {
        Course course = coursesRepository.findById(courseId).orElseThrow(CourseNotFoundException::new);
        Student student = studentsRepository.findById(studentId).orElseThrow(StudentNotFoundException::new);

        Enrollment enrollment = new Enrollment(student, course);
        enrollmentsRepository.save(enrollment);
    }
}
