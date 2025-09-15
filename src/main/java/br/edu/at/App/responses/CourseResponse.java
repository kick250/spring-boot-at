package br.edu.at.App.responses;

import br.edu.at.App.entities.Course;

import java.util.List;

public record CourseResponse(
    Long id,
    String name,
    String code,
    List<CourseStudentResponse> students
) {
    public CourseResponse(Course course) {
        this(
            course.getId(),
            course.getName(),
            course.getCode(),
            course.getStudents().stream().map(CourseStudentResponse::new).toList()
        );
    }
}
