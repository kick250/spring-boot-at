package br.edu.at.App.responses;

import br.edu.at.App.entities.Course;

import java.util.List;

public record CourseWithApprovedStudentsResponse(
        Long id,
        String name,
        String code,
        List<CourseStudentResponse> students
) {
    public CourseWithApprovedStudentsResponse(Course course) {
        this(
                course.getId(),
                course.getName(),
                course.getCode(),
                course.getApprovedStudents().stream().map(CourseStudentResponse::new).toList()
        );
    }
}
