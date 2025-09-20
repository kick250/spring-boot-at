package br.edu.at.App.controllers;

import br.edu.at.App.entities.Student;
import br.edu.at.App.exceptions.CourseNotFoundException;
import br.edu.at.App.exceptions.InvalidCoursesQuantity;
import br.edu.at.App.exceptions.StudentCpfAlreadyExists;
import br.edu.at.App.exceptions.StudentEmailAlreadyExists;
import br.edu.at.App.requests.StudentCreateRequest;
import br.edu.at.App.responses.StudentResponse;
import br.edu.at.App.services.StudentsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@AllArgsConstructor
public class StudentsController {
    private final StudentsService studentsService;

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAll() {
        List<Student> students = studentsService.getAll();

        return ResponseEntity.ok(students.stream().map(StudentResponse::new).toList());
    }

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody StudentCreateRequest request) {
        try {
            studentsService.create(request.name(), request.cpf(), request.email(), request.phone(), request.address(), request.getUniqueCourseIds());
            return ResponseEntity.status(HttpStatus.CREATED).body("Aluno criado com sucesso.");
        } catch (CourseNotFoundException | InvalidCoursesQuantity | StudentEmailAlreadyExists | StudentCpfAlreadyExists e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
