package br.com.fiap.file_management.application.usecase;

import br.com.fiap.file_management.domain.entity.File;
import br.com.fiap.file_management.domain.enums.FileStatus;
import br.com.fiap.file_management.domain.repository.FileRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public class UpdateFileStatusUseCase {
    
    private final FileRepository repository;
    
    public UpdateFileStatusUseCase(FileRepository repository) {
        this.repository = repository;
    }
    
    public File execute(String email, String fileName, FileStatus status) {
        File file = repository.findByEmailAndOriginalFileName(email, fileName)
                .orElseThrow(() -> new IllegalArgumentException(
                    "File not found with email: " + email + " and fileName: " + fileName));
        
        file.setStatus(status);
        file.setUpdatedAt(LocalDateTime.now());
        
        return repository.save(file);
    }
}
