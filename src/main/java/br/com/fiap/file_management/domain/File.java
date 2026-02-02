package br.com.fiap.file_management.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class File {
    private Long id;
    private User user;
    private String name;
    private FileStatus status;
    private byte[] fileData;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
