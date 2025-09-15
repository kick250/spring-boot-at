package br.edu.at.App.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.List;
import java.util.Set;

public record StudentCreateRequest(
        @NotEmpty
        String name,
        @NotEmpty
        @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", message = "O CPF deve estar no formato XXX.XXX.XXX-XX")
        String cpf,
        @Email
        String email,
        @NotEmpty
        @Pattern(regexp = "^\\d{10,11}$", message = "O telefone deve conter apenas números e ter entre 10 e 11 dígitos")
        String phone,
        @NotEmpty
        String address,
        @NotEmpty
        List<Long> courseIds
) {
    public Set<Long> getUniqueCourseIds() {
        return Set.copyOf(courseIds);
    }
}
