package br.com.fiap.file_management.security;

import br.com.fiap.file_management.config.ClientWithAcessEnum;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.io.PrintWriter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JwtAuthFilterTest {

    private JwtService jwtService;
    private UserDetailsService userDetailsService;
    private JwtAuthFilter filter;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;

    @BeforeEach
    void setup() throws Exception {
        jwtService = mock(JwtService.class);
        userDetailsService = mock(UserDetailsService.class);
        filter = new JwtAuthFilter(jwtService, userDetailsService);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);

        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldContinueFilterWhenNoAuthorizationHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    void shouldIgnoreInvalidToken() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid");
        when(jwtService.extractUsername("invalid")).thenThrow(new RuntimeException());

        filter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
        verifyNoInteractions(userDetailsService);
    }

    @Test
    void shouldAuthenticateWithValidToken() throws Exception {
        UserDetails user = User.withUsername("marjory").password("123").roles("USER").build();

        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        when(jwtService.extractUsername("valid-token")).thenReturn("marjory");
        when(userDetailsService.loadUserByUsername("marjory")).thenReturn(user);
        when(jwtService.isTokenValid("valid-token", user)).thenReturn(true);

        filter.doFilterInternal(request, response, chain);

        verify(userDetailsService).loadUserByUsername("marjory");
        verify(chain).doFilter(request, response);
    }

    @Test
    void shouldReturnUnauthorizedWhenIntegrationHeadersMissing() throws Exception {

        ClientWithAcessEnum client = ClientWithAcessEnum.values()[0];

        when(request.getHeader("integration-name")).thenReturn(client.name());
        when(request.getRequestURI()).thenReturn(client.getPath());

        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        filter.doFilterInternal(request, response, chain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    void shouldAuthenticateIntegrationClient() throws Exception {
        ClientWithAcessEnum client = ClientWithAcessEnum.values()[0];

        when(request.getHeader("integration-name")).thenReturn(client.name());
        when(request.getHeader("integration-key")).thenReturn(client.getToken());
        when(request.getRequestURI()).thenReturn(client.getPath());

        filter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
    }
}