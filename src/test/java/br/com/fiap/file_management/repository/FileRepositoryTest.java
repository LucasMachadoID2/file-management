package br.com.fiap.file_management.repository;

import br.com.fiap.file_management.domain.FileStatus;
import br.com.fiap.file_management.entity.FileEntity;
import br.com.fiap.file_management.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FileRepositoryTest {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Should save and retrieve file entity")
    void shouldSaveAndFindFile() {

        UserEntity user = UserEntity.builder()
                .email("test@email.com")
                .password("123456")
                .build();

        entityManager.persist(user);

        FileEntity file = FileEntity.builder()
                .user(user)
                .name("video.mp4")
                .status(FileStatus.PROCESSING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .downloadUrl("http://url.com")
                .build();

        FileEntity savedFile = fileRepository.save(file);

        entityManager.flush();
        entityManager.clear();

        FileEntity foundFile = fileRepository.findById(savedFile.getId()).orElse(null);

        assertThat(foundFile).isNotNull();
        assertThat(foundFile.getName()).isEqualTo("video.mp4");
        assertThat(foundFile.getStatus()).isEqualTo(FileStatus.PROCESSING);
        assertThat(foundFile.getDownloadUrl()).isEqualTo("http://url.com");
    }
}