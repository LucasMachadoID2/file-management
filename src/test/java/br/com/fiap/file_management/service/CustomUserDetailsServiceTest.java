package br.com.fiap.file_management.service;

import br.com.fiap.file_management.entity.UserEntity;
import br.com.fiap.file_management.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService service;

    @Test
    void shouldReturnUserWhenEmailExists() {
        UserEntity user = UserEntity.builder()
                .email("marjory@fiap.com")
                .password("123")
                .build();

        when(userRepository.findByEmail("marjory@fiap.com"))
                .thenReturn(Optional.of(user));

        var result = service.loadUserByUsername("marjory@fiap.com");

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("marjory@fiap.com");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByEmail("naoexiste@fiap.com"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("naoexiste@fiap.com"));
    }
}