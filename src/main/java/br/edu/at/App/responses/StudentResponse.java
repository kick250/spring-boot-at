package br.edu.at.App.responses;

import br.edu.at.App.entities.Student;

import java.util.List;

public record StudentResponse(
        Long id,
        String name,
        String cpf,
        String email,
        String phone,
        String address,
        List<StudentCourseResponse> courses
) {
    public StudentResponse(Student student) {
        this(
                student.getId(),
                student.getName(),
                student.getCpf(),
                student.getEmail(),
                student.getPhone(),
                student.getAddress(),
                student.getEnrollments().stream().map(StudentCourseResponse::new).toList()
        );
    }
}
