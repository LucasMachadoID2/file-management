package br.com.fiap.file_management.useCase;

import br.com.fiap.file_management.domain.FileStatus;
import br.com.fiap.file_management.entity.FileEntity;
import br.com.fiap.file_management.entity.UserEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import br.com.fiap.file_management.usecase.FilesFilterUseCase;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class FilesFilterUseCaseTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldFilterByEmail() {

        UserEntity user = UserEntity.builder()
                .email("user@fiap.com")
                .password("123")
                .build();

        entityManager.persist(user);

        FileEntity file = FileEntity.builder()
                .name("file1")
                .status(FileStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user)
                .build();

        entityManager.persist(file);
        entityManager.flush();

        var result = FilesFilterUseCase.findFiltered(
                "user@fiap.com",
                null,
                entityManager
        );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("file1");
    }

    @Test
    void shouldFilterByStatus() {

        UserEntity user = UserEntity.builder()
                .email("user@fiap.com")
                .password("123")
                .build();

        entityManager.persist(user);

        entityManager.persist(FileEntity.builder()
                .name("file1")
                .status(FileStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user)
                .build());

        entityManager.persist(FileEntity.builder()
                .name("file2")
                .status(FileStatus.PROCESSING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user)
                .build());

        entityManager.flush();

        var result = FilesFilterUseCase.findFiltered(
                null,
                FileStatus.PENDING.name(),
                entityManager
        );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("file1");
    }
}