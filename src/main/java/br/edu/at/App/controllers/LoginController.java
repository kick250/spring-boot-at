package br.edu.at.App.controllers;

import br.edu.at.App.entities.Teacher;
import br.edu.at.App.requests.LoginRequest;
import br.edu.at.App.responses.TokenResponse;
import br.edu.at.App.services.TokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@AllArgsConstructor
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            var token = new UsernamePasswordAuthenticationToken(request.username(), request.password());
            var authentication = this.authenticationManager.authenticate(token);

            String jwtToken = this.tokenService.generateToken((Teacher) authentication.getPrincipal());
            return ResponseEntity.ok(new TokenResponse(jwtToken));
        } catch (AuthenticationException exception) {
            return ResponseEntity.status(401).body("Usuário ou senha inválidos");
        }
    }
}
