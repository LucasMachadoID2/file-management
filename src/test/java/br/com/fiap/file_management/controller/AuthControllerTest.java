package br.com.fiap.file_management.controller;

import br.com.fiap.file_management.controller.dto.user.*;
import br.com.fiap.file_management.security.JwtService;
import br.com.fiap.file_management.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController controller;

    // -------------------------
    // REGISTER - SUCCESS
    // -------------------------
   @Test
    void shouldRegisterUserSuccessfully() {

        RegisterRequest request =
                new RegisterRequest("test@mail.com", "123");

        ResponseEntity<?> response = controller.register(request);

        verify(userService).register("test@mail.com", "123");
        assertEquals(200, response.getStatusCodeValue());
    }

    // -------------------------
    // REGISTER - ERROR
    // -------------------------
    @Test
    void shouldReturnBadRequestWhenRegisterFails() {

        RegisterRequest request =
                new RegisterRequest("test@mail.com", "123");

        doThrow(new IllegalArgumentException("Usuário já existe"))
                .when(userService).register(anyString(), anyString());

        ResponseEntity<?> response = controller.register(request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Usuário já existe", response.getBody());
    }

    // -------------------------
    // LOGIN - SUCCESS
    // -------------------------
    @Test
    void shouldLoginAndReturnToken() {

        LoginRequest request =
                new LoginRequest("test@mail.com", "123");

        Authentication authentication = mock(Authentication.class);

        UserDetails userDetails = User
                .withUsername("test@mail.com")
                .password("123")
                .authorities("USER")
                .build();

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        when(authentication.getPrincipal())
                .thenReturn(userDetails);

        when(jwtService.generateToken(userDetails))
                .thenReturn("fake-jwt-token");

        ResponseEntity<AuthResponse> response = controller.login(request);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("fake-jwt-token", response.getBody().getToken());
    }
}