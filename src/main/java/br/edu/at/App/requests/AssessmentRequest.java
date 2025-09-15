package br.edu.at.App.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AssessmentRequest(
        @NotNull
        Long studentId,
        @NotNull
        Long courseId,
        @NotNull
        @Min(0)
        @Max(10)
        Double grade
) {
}
