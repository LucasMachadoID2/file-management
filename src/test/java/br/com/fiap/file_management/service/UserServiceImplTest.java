package br.com.fiap.file_management.service;

import br.com.fiap.file_management.entity.UserEntity;
import br.com.fiap.file_management.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private UserServiceImpl service;

    @Test
    void shouldRegisterUserSuccessfully() {

        when(userRepository.existsByEmail("user@fiap.com")).thenReturn(false);
        when(passwordEncoder.encode("123")).thenReturn("encoded-password");

        UserEntity savedUser = UserEntity.builder()
                .id(1L)
                .email("user@fiap.com")
                .password("encoded-password")
                .build();

        when(userRepository.save(any())).thenReturn(savedUser);

        UserEntity result = service.register("user@fiap.com", "123");

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("user@fiap.com");
        assertThat(result.getPassword()).isEqualTo("encoded-password");

        verify(passwordEncoder).encode("123");
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        when(userRepository.existsByEmail("user@fiap.com")).thenReturn(true);

        assertThatThrownBy(() -> service.register("user@fiap.com", "123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("email já existe");

        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void shouldFindUserByEmail() {

        UserEntity user = UserEntity.builder()
                .id(1L)
                .email("user@fiap.com")
                .password("123")
                .build();

        when(userRepository.findByEmail("user@fiap.com"))
                .thenReturn(Optional.of(user));

        UserEntity result = service.findByEmail("user@fiap.com");

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("user@fiap.com");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        when(userRepository.findByEmail("naoexiste@fiap.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findByEmail("naoexiste@fiap.com"))
                .isInstanceOf(RuntimeException.class);
    }
}