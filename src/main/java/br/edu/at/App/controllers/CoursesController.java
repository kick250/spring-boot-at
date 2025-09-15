package br.edu.at.App.controllers;

import br.edu.at.App.entities.Course;
import br.edu.at.App.exceptions.CourseNotFoundException;
import br.edu.at.App.exceptions.StudentNotFoundException;
import br.edu.at.App.requests.CourseCreateRequest;
import br.edu.at.App.requests.AddStudentRequest;
import br.edu.at.App.responses.CourseResponse;
import br.edu.at.App.responses.CourseWithApprovedStudentsResponse;
import br.edu.at.App.responses.CourseWithFailedStudentsResponse;
import br.edu.at.App.services.CoursesService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@AllArgsConstructor
public class CoursesController {
    private final CoursesService coursesService;

    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAll() {
        List<Course> courses = coursesService.getAll();

        return ResponseEntity.ok(courses.stream().map(CourseResponse::new).toList());
    }

    @GetMapping("/{id}/approved_students")
    public ResponseEntity<CourseWithApprovedStudentsResponse> getApprovedStudents(@PathVariable("id") Long courseId) {
        try {
            Course course = coursesService.getById(courseId);
            return ResponseEntity.ok(new CourseWithApprovedStudentsResponse(course));
        } catch (CourseNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/failed_students")
    public ResponseEntity<CourseWithFailedStudentsResponse> getFailedStudents(@PathVariable("id") Long courseId) {
        try {
            Course course = coursesService.getById(courseId);
            return ResponseEntity.ok(new CourseWithFailedStudentsResponse(course));
        } catch (CourseNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody CourseCreateRequest request) {
        coursesService.create(request.name(), request.code());
        return ResponseEntity.status(HttpStatus.CREATED).body("Curso criado com sucesso.");
    }

    @PostMapping("/{id}/add_student")
    public ResponseEntity<String> addStudent(@PathVariable("id") Long courseId, @Valid @RequestBody AddStudentRequest request) {
        try {
            coursesService.addStudent(courseId, request.studentId());
            return ResponseEntity.ok("Aluno adicionado ao curso com sucesso.");
        } catch (CourseNotFoundException | StudentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
