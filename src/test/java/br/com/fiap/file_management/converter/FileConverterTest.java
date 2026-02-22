package br.com.fiap.file_management.converter;

import br.com.fiap.file_management.domain.File;
import br.com.fiap.file_management.domain.FileStatus;
import br.com.fiap.file_management.domain.User;
import br.com.fiap.file_management.entity.FileEntity;
import br.com.fiap.file_management.entity.UserEntity;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileConverterTest {

    @Test
    void shouldConvertFileEntityToDomain() {

        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = createdAt.plusHours(1);

        UserEntity userEntity = UserEntity.builder()
                .id(10L)
                .email("user@test.com")
                .password("123")
                .build();

        FileEntity entity = FileEntity.builder()
                .id(1L)
                .user(userEntity)
                .name("video.mp4")
                .status(FileStatus.FINISHED)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .downloadUrl("http://download")
                .build();

        User fakeDomainUser = User.builder()
                .id(10L)
                .email("user@test.com")
                .build();

        try (MockedStatic<UserConverter> userConverterMock =
                     mockStatic(UserConverter.class)) {

            userConverterMock
                    .when(() -> UserConverter.toDomain(userEntity))
                    .thenReturn(fakeDomainUser);

            File result = FileConverter.toDomain(entity);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("video.mp4", result.getName());
            assertEquals(FileStatus.FINISHED, result.getStatus());
            assertEquals(createdAt, result.getCreatedAt());
            assertEquals(updatedAt, result.getUpdatedAt());
            assertEquals("http://download", result.getDownloadUrl());

            assertNotNull(result.getUser());
            assertEquals("user@test.com", result.getUser().getEmail());
        }
    }
}