package br.edu.at.App.infra;

import br.edu.at.App.repositories.TeachersRepository;
import br.edu.at.App.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    private final TeachersRepository teachersRepository;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = this.getToken(request);

        if (!jwtToken.isBlank()) {
            var decodedToken = tokenService.decodeToken(jwtToken);
            this.authenticate(decodedToken.getSubject());
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token == null)
            return "";

        return token.replace("Bearer ", "");
    }

    private void authenticate(String subject) {
        this.teachersRepository.findByEmail(subject).ifPresent(teacher -> {
            var authentication = new UsernamePasswordAuthenticationToken(teacher, null, teacher.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        });
    }
}
