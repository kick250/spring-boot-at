package br.edu.at.App.requests;

import jakarta.validation.constraints.NotEmpty;

public record CourseCreateRequest(
    @NotEmpty
    String name,
    @NotEmpty
    String code
) {
}
