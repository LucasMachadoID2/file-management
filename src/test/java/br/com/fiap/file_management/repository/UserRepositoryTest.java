package br.com.fiap.file_management.repository;

import br.com.fiap.file_management.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve encontrar usuário pelo email")
    void shouldFindUserByEmail() {
        // Arrange
        UserEntity user = UserEntity.builder()
                .email("teste@fiap.com")
                .password("123456")
                .build();

        userRepository.save(user);

        // Act
        Optional<UserEntity> result = userRepository.findByEmail("teste@fiap.com");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("teste@fiap.com");
    }

    @Test
    @DisplayName("Não deve encontrar usuário com email inexistente")
    void shouldNotFindUserByEmail() {
        // Act
        Optional<UserEntity> result = userRepository.findByEmail("naoexiste@fiap.com");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar true quando email existir")
    void shouldReturnTrueWhenEmailExists() {
        // Arrange
        UserEntity user = UserEntity.builder()
                .email("existe@fiap.com")
                .password("123456")
                .build();

        userRepository.save(user);

        // Act
        boolean exists = userRepository.existsByEmail("existe@fiap.com");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false quando email não existir")
    void shouldReturnFalseWhenEmailDoesNotExist() {
        // Act
        boolean exists = userRepository.existsByEmail("inexistente@fiap.com");

        // Assert
        assertThat(exists).isFalse();
    }
}