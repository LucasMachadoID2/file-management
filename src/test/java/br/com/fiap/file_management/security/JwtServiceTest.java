package br.com.fiap.file_management.security;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.ExpiredJwtException;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    // segredo precisa ter tamanho suficiente para HS256 (>= 32 bytes)
    private static final String SECRET =
            "12345678901234567890123456789012";

    @Test
    void shouldGenerateTokenAndExtractUsername() {
        JwtService jwtService = new JwtService(SECRET, 3600);

        UserDetails user = User.withUsername("marjory")
                .password("123")
                .roles("USER")
                .build();

        String token = jwtService.generateToken(user);

        assertThat(token).isNotNull();
        assertThat(jwtService.extractUsername(token)).isEqualTo("marjory");
    }

    @Test
    void shouldValidateCorrectToken() {
        JwtService jwtService = new JwtService(SECRET, 3600);

        UserDetails user = User.withUsername("marjory")
                .password("123")
                .roles("USER")
                .build();

        String token = jwtService.generateToken(user);

        assertThat(jwtService.isTokenValid(token, user)).isTrue();
    }

    @Test
    void shouldInvalidateTokenForDifferentUser() {
        JwtService jwtService = new JwtService(SECRET, 3600);

        UserDetails user1 = User.withUsername("user1")
                .password("123")
                .roles("USER")
                .build();

        UserDetails user2 = User.withUsername("user2")
                .password("123")
                .roles("USER")
                .build();

        String token = jwtService.generateToken(user1);

        assertThat(jwtService.isTokenValid(token, user2)).isFalse();
    }

    @Test
    void shouldDetectExpiredToken() throws InterruptedException {

        JwtService jwtService = new JwtService(SECRET, 1);

        UserDetails user = User.withUsername("marjory")
                .password("123")
                .roles("USER")
                .build();

        String token = jwtService.generateToken(user);

        Thread.sleep(1500);

        assertThrows(ExpiredJwtException.class,
                () -> jwtService.extractUsername(token));
    }
}