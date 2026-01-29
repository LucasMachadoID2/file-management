package br.com.fiap.file_management.infrastructure.persistence.entity;

import br.com.fiap.file_management.domain.enums.FileStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "files")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "user_id")
    private String userId;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "original_file_name")
    private String originalFileName;
    
    @Column(name = "content_type")
    private String contentType;
    
    private Long size;
    
    @Column(name = "file_data", columnDefinition = "bytea")
    private byte[] fileData;
    
    @Enumerated(EnumType.STRING)
    private FileStatus status;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
