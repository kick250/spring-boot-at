package br.edu.at.App.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
        @Email
        @NotEmpty
        String username,
        @NotEmpty
        String password
) {
}
