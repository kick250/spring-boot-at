package br.edu.at.App.responses;

import br.edu.at.App.entities.Course;

import java.util.List;

public record CourseWithFailedStudentsResponse(
        Long id,
        String name,
        String code,
        List<CourseStudentResponse> students
) {
    public CourseWithFailedStudentsResponse(Course course) {
        this(
                course.getId(),
                course.getName(),
                course.getCode(),
                course.getFailedStudents().stream().map(CourseStudentResponse::new).toList()
        );
    }
}
