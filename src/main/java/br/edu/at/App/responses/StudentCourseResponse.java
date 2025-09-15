package br.edu.at.App.responses;

import br.edu.at.App.entities.Enrollment;

public record StudentCourseResponse(
        Long id,
        String name,
        String code,
        Double grade
) {
    public StudentCourseResponse(Enrollment enrollment) {
        this(
                enrollment.getCourse().getId(),
                enrollment.getCourse().getName(),
                enrollment.getCourse().getCode(),
                enrollment.getGrade()
        );
    }
}
