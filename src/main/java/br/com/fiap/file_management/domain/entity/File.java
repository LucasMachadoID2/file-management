package br.com.fiap.file_management.domain.entity;

import br.com.fiap.file_management.domain.enums.FileStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class File {
    private UUID id;
    private String userId;
    private String email;
    private String originalFileName;
    private String contentType;
    private Long size;
    private byte[] fileData;
    private FileStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
