package br.com.fiap.file_management.controller.dto.file;

import br.com.fiap.file_management.domain.FileStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FileResponse {
    private Long id;
    private String name;
    private FileStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
