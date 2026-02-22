package br.com.fiap.file_management.converter;

import br.com.fiap.file_management.domain.File;
import br.com.fiap.file_management.domain.FileStatus;
import br.com.fiap.file_management.entity.FileEntity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileEntityConverterTest {

    @Test
    void shouldConvertDomainToEntity() {

        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = createdAt.plusMinutes(10);

        File domain = File.builder()
                .id(99L) // não deve ser mapeado
                .name("video.mp4")
                .status(FileStatus.PROCESSING)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .downloadUrl("http://download")
                .build();

        FileEntity entity = FileEntityConverter.toEntity(domain);

        assertNotNull(entity);

        // Campos mapeados
        assertEquals("video.mp4", entity.getName());
        assertEquals(FileStatus.PROCESSING, entity.getStatus());
        assertEquals(createdAt, entity.getCreatedAt());
        assertEquals(updatedAt, entity.getUpdatedAt());
        assertEquals("http://download", entity.getDownloadUrl());

        // Campos NÃO mapeados (importante para coverage real)
        assertNull(entity.getId());
        assertNull(entity.getUser());
    }
}