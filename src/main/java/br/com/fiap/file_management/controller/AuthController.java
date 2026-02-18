package br.com.fiap.file_management.controller;

import br.com.fiap.file_management.controller.dto.user.AuthResponse;
import br.com.fiap.file_management.controller.dto.user.LoginRequest;
import br.com.fiap.file_management.controller.dto.user.RegisterRequest;
import br.com.fiap.file_management.security.JwtService;
import br.com.fiap.file_management.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/register")
    @Operation(summary = "Registrar novo usuário", description = "Cria uma nova conta de usuário")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            userService.register(request.getEmail(), request.getPassword());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário", description = "Realiza login e retorna token JWT")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(principal);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
