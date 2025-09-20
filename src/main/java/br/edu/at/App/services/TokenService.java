package br.edu.at.App.services;

import br.edu.at.App.entities.Teacher;
import br.edu.at.App.exceptions.InvalidTokenException;
import br.edu.at.App.infra.TimeConfig;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class TokenService {
    private final String issuer;
    private final String secret;
    private final TimeConfig timeConfig;

    public TokenService(Environment env, TimeConfig timeConfig) {
        this.issuer = env.getProperty("spring.application.name");
        this.secret = env.getProperty("spring.application.security.token.secret");
        this.timeConfig = timeConfig;
    }

    public String generateToken(Teacher teacher) {
        var algorithm = Algorithm.HMAC256(secret);

        return JWT
                .create()
                .withIssuer(issuer)
                .withSubject(teacher.getUsername())
                .withExpiresAt(this.getExpiresAT())
                .sign(algorithm);
    }

    private Instant getExpiresAT() {
        return LocalDateTime.now().plusHours(2).toInstant(timeConfig.zoneOffset());
    }

    public DecodedJWT decodeToken(String jwtToken) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(jwtToken);
        } catch (JWTVerificationException exception) {
            throw new InvalidTokenException();
        }
    }
}
