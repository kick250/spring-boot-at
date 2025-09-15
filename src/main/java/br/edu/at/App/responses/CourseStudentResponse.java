package br.edu.at.App.responses;

import br.edu.at.App.entities.Student;

public record CourseStudentResponse(
    Long id,
    String name,
    String cpf,
    String email,
    String phone,
    String address
) {
    public CourseStudentResponse(Student student) {
        this(
            student.getId(),
            student.getName(),
            student.getCpf(),
            student.getEmail(),
            student.getPhone(),
            student.getAddress()
        );
    }
}
