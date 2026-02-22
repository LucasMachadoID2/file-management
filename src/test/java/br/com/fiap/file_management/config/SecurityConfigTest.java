package br.com.fiap.file_management.config;

import br.com.fiap.file_management.security.JwtAuthFilter;

import org.junit.jupiter.api.Test;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityConfigTest {

    private final SecurityConfig securityConfig = new SecurityConfig();

    @Test
    void shouldCreatePasswordEncoder() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder);
    }

    @Test
    void shouldCreateAuthenticationManager() throws Exception {

        AuthenticationConfiguration configuration = mock(AuthenticationConfiguration.class);
        AuthenticationManager manager = mock(AuthenticationManager.class);

        when(configuration.getAuthenticationManager()).thenReturn(manager);

        AuthenticationManager result =
                securityConfig.authenticationManager(configuration);

        assertNotNull(result);
        assertEquals(manager, result);
    }
}