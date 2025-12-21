package com.pragma.powerup.infrastructure.out.jpa.security.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final Algorithm algorithm;

    public JwtUtil(@Value("${jwt.secret}") String jwtSecret){
        this.algorithm = Algorithm.HMAC256(jwtSecret);
    }

    public boolean isValid(String jwt) {
        try {
            JWT.require(algorithm).build().verify(jwt);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public Long getId(String token) {
        try {
            return JWT.require(algorithm).build().verify(token).getClaim("id").asLong();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public String getRole(String token) {
        try {
            return JWT.require(algorithm).build().verify(token).getClaim("role").asString();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public String getUsername(String jwt) {
        return JWT.require(algorithm).build().verify(jwt).getSubject();
    }
}
