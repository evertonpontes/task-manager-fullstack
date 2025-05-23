package com.example.taskmanager.config.auth.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.taskmanager.utils.exceptions.TokenValidateException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final SecureRandom secureRandom;
    private final Base64.Encoder base64Encoder;
    @Value("${app.jwt.token.secret-key}")
    private String secretKey;
    @Value("${app.jwt.token.issuer}")
    private String issuer;

    public String generateToken(String subject) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            return JWT.create()
                    .withIssuer(issuer)
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(expiresAt())
                    .withSubject(subject)
                    .sign(algorithm);
        } catch (IllegalArgumentException | JWTCreationException e) {
            throw new RuntimeException("Failed generating token", e);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (IllegalArgumentException | JWTVerificationException e) {
            System.out.println(e.getMessage());
            throw new TokenValidateException("Invalid token provided.");
        }
    }

    public Instant expiresAt() {
        return LocalDateTime.now().plusMinutes(5).toInstant(ZoneOffset.of("-03:00"));
    }

    public String generateRandomCode() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
