package br.com.fiap.file_management.converter;

import br.com.fiap.file_management.controller.dto.file.FileResponse;
import br.com.fiap.file_management.domain.File;
import br.com.fiap.file_management.domain.FileStatus;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileResponseConverterTest {

    @Test
    void shouldConvertDomainToResponse() {

        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = createdAt.plusHours(1);

        File domain = File.builder()
                .id(1L)
                .name("video.mp4")
                .status(FileStatus.FINISHED)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .downloadUrl("http://download")
                .build();

        FileResponse response = FileResponseConverter.toResponse(domain);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("video.mp4", response.getName());
        assertEquals(FileStatus.FINISHED, response.getStatus());
        assertEquals(createdAt, response.getCreatedAt());
        assertEquals(updatedAt, response.getUpdatedAt());
        assertEquals("http://download", response.getDownloadUrl());
    }
}